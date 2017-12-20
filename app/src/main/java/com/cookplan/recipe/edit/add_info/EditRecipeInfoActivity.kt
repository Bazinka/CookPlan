package com.cookplan.recipe.edit.add_info

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.models.Recipe
import com.cookplan.recipe.edit.add_ingredients.EditRecipeIngredientsActivity
import com.cookplan.upload_image.UploadImageActivity
import com.cookplan.upload_image.UploadImageActivity.IMAGE_URL_KEY
import com.cookplan.utils.FirebaseImageLoader
import com.cookplan.utils.PermissionUtils
import com.cookplan.utils.Utils
import com.google.firebase.storage.FirebaseStorage

class EditRecipeInfoActivity : BaseActivity(), EditRecipeInfoView {
    private var mProgressDialog: ProgressDialog? = null
    private var presenter: EditRecipeInfoPresenter? = null

    private var language: String = String()

    private var recipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe_basics)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        setNavigationArrow()

        setTitle("")

        if (intent.hasExtra(RECIPE_OBJECT_KEY)) {
            recipe = intent.getSerializableExtra(RECIPE_OBJECT_KEY) as Recipe
        }
        val recipeNameEditText = findViewById<EditText>(R.id.recipe_name_edit_text)
        recipeNameEditText.requestFocus()
        val recipeDescEditText = findViewById<EditText>(R.id.recipe_process_edit_text)
        recipeDescEditText.setOnTouchListener { v, event ->
            if (v.id == R.id.recipe_process_edit_text) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }
        recipeNameEditText?.setText(recipe?.name)
        recipeDescEditText?.setText(recipe?.desc)

        val addImageViewGroup = findViewById<View>(R.id.add_image_view) as ViewGroup
        addImageViewGroup.setOnClickListener { startAddImageActivity() }

        fillImageLayout()

        val captureImg = findViewById<Button>(R.id.ocr_desc_btn)
        captureImg.setOnClickListener { view ->
            AlertDialog.Builder(this)
                    .setMessage(R.string.choose_recipe_language)
                    .setPositiveButton(R.string.english_lan_title) { dialog, which ->
                        language = getString(R.string.english_lan_string)
                        startCameraWithPermCheck()
                    }
                    .setNegativeButton(R.string.russian_lan_title) { dialog, which ->
                        language = getString(R.string.russian_lan_string)
                        startCameraWithPermCheck()
                    }
                    .show()

        }
        presenter = EditRecipeInfoPresenterImpl(this, this)

        val nextFab = findViewById<FloatingActionButton>(R.id.recipe_view_fab)
        nextFab.setOnClickListener { v -> onNextButtonClick() }
    }

    private fun fillImageLayout() {
        if (!(recipe?.imageUrls?.isEmpty() ?: true)) {
            val ll = findViewById<View>(R.id.existing_images_layout) as LinearLayout
            ll.removeAllViews()
            for (url in (recipe?.imageUrls ?: listOf())) {
                val imageView = ImageView(this)
                val lp = LinearLayout.LayoutParams(150,
                        LinearLayout.LayoutParams.MATCH_PARENT)
                lp.setMargins(5, 5, 5, 5)
                imageView.layoutParams = lp
                imageView.setPadding(5, 0, 5, 0)
                imageView.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                if (Utils.isStringUrl(url)) {
                    Glide.with(this)
                            .load(url)
                            .centerCrop()
                            .into(imageView)
                } else {
                    val imageRef = FirebaseStorage.getInstance().getReference(url)
                    Glide.with(this)
                            .using(FirebaseImageLoader())
                            .load(imageRef)
                            .centerCrop()
                            .into(imageView)
                }
                ll.addView(imageView)
            }
            ll.invalidate()
        }
    }

    private fun startCameraWithPermCheck() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtils.isPermissionsGranted(this, permission)) {
                PermissionUtils.requestPermissions(this, RC_IMAGE_PERMS, permission)
            } else {
                startCameraActivity()
            }
        }
    }

    private fun onNextButtonClick() {
        val recipeNameEditText = findViewById<EditText>(R.id.recipe_name_edit_text)
        val recipeDescEditText = findViewById<EditText>(R.id.recipe_process_edit_text)
        val name = recipeNameEditText.text.toString()
        var desc = recipeDescEditText.text.toString()
        desc = if (desc.isEmpty()) getString(R.string.recipe_desc_is_not_needed_title) else desc
        if (!name.isEmpty()) {
            presenter?.saveRecipe(recipe, name, desc)
        } else {
            val recipeEditLayout = findViewById<TextInputLayout>(R.id.recipe_name_edit_layout)
            recipeEditLayout.error = getString(R.string.error_required_field)
        }
    }


    /**
     * to get high resolution image from camera
     */
    private fun startCameraActivity() {
        if (PermissionUtils.isPermissionsGranted(this, permission)) {
            val outputFileUri = presenter?.getOutputImagePath()
            if (outputFileUri != null) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)

                if (takePictureIntent.resolveActivity(packageManager) != null) {
                    startActivityForResult(takePictureIntent, PHOTO_REQUEST_CODE)
                }
            } else {
                setErrorToast(getString(R.string.error_generate_image_path))
            }
        } else {
            setErrorToSnackBar(getString(R.string.permission_denied))
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int,
                                         data: Intent) {
        //making photo
        if (resultCode == Activity.RESULT_OK && presenter != null) {
            if (requestCode == PHOTO_REQUEST_CODE) {
                if (mProgressDialog == null) {
                    mProgressDialog = ProgressDialog.show(this, getString(R.string.processing_title),
                            getString(R.string.processing_ocr_message), true)
                } else {
                    mProgressDialog?.show()
                }
                presenter?.doOCR(language)
            } else if (requestCode == GET_IMAGE_URL_LIST_REQUEST) {
                recipe?.imageUrls = data.getStringArrayListExtra(IMAGE_URL_KEY)
                fillImageLayout()
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RC_IMAGE_PERMS -> {

                var permGranded = true
                for (grantResult in grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        permGranded = false
                    }
                }

                if (grantResults.size == permission.size && permGranded) {
                    startCameraActivity()
                } else {
                    setErrorToSnackBar(getString(R.string.permission_denied))
                }
            }
        }
    }

    override fun setErrorToSnackBar(error: String) {
        Snackbar.make(findViewById(R.id.root_view), error, Snackbar.LENGTH_LONG).show()
    }

    override fun setErrorToast(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    override fun setAsyncTextResult(result: String) {
        runOnUiThread {
            if (result != "") {
                val textView = findViewById<EditText>(R.id.recipe_process_edit_text)
                textView.setText(result)
            }
            mProgressDialog?.dismiss()
        }
    }

    override fun showProgressBar() {
        val progressBar = findViewById<View>(R.id.progress_bar_layout)
        progressBar.visibility = View.VISIBLE

    }

    override fun hideProgressBar() {
        val progressBar = findViewById<View>(R.id.progress_bar_layout)
        progressBar.visibility = View.GONE
    }

    override fun setAsyncErrorToSnackBar(error: String) {
        runOnUiThread {
            Snackbar.make(findViewById(R.id.root_view), error, Snackbar.LENGTH_LONG).show()
            mProgressDialog?.dismiss()
        }
    }

    override fun setNextActivity(recipe: Recipe) {
        val intent = Intent(this, EditRecipeIngredientsActivity::class.java)
        intent.putExtra(EditRecipeIngredientsActivity.RECIPE_OBJECT_KEY, recipe)
        startActivityWithLeftAnimation(intent)
        finish()
    }

    private fun startAddImageActivity() {
        val intent = Intent(this, UploadImageActivity::class.java)
        intent.putStringArrayListExtra(IMAGE_URL_KEY, recipe?.imageUrlArrayList)
        startActivityForResultWithLeftAnimation(intent, GET_IMAGE_URL_LIST_REQUEST)
    }

    companion object {
        val RECIPE_OBJECT_KEY = "recipe_name"
        val GET_IMAGE_URL_LIST_REQUEST = 16

        private val PHOTO_REQUEST_CODE = 101
        private val RC_IMAGE_PERMS = 102
        private val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    }
}
