package com.cookplan.recipe.edit.description

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.recipe.view_item.RecipeViewActivity
import com.cookplan.utils.PermissionUtils

class EditRecipeDescActivity : BaseActivity(), EditRecipeDescView {
    private var mProgressDialog: ProgressDialog? = null
    private var presenter: EditRecipeDescPresenter? = null

    private var language: String = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe_description)
        setNavigationArrow()

        setTitle(R.string.recipe_description_title)


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
        if (intent.hasExtra(RECIPE_DESCRIPTION_KEY)) {
            val desc = intent.getStringExtra(RECIPE_DESCRIPTION_KEY)
            recipeDescEditText?.setText(desc)
        }

        val recognizeButton = findViewById<Button>(R.id.ocr_desc_btn)
        recognizeButton.setOnClickListener {
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
        presenter = EditRecipeDescPresenterImpl(this, this)
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
                                         data: Intent?) {
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
        Snackbar.make(findViewById(R.id.main_layout), error, Snackbar.LENGTH_LONG).show()
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

    override fun setAsyncErrorToSnackBar(error: String) {
        runOnUiThread {
            Snackbar.make(findViewById(R.id.main_layout), error, Snackbar.LENGTH_LONG).show()
            mProgressDialog?.dismiss()
        }
    }

    override fun onCreateOptionsMenu(_menu: Menu): Boolean {
        menuInflater.inflate(R.menu.done_menu, _menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.app_bar_done) {
            val recipeDescEditText = findViewById<EditText>(R.id.recipe_process_edit_text)
            var desc = recipeDescEditText.text.toString()
            desc = if (desc.isEmpty()) getString(R.string.recipe_desc_is_not_needed_title) else desc
            val intent = Intent()
            intent.putExtra(RecipeViewActivity.CHANGE_DESCRIPTION_KEY, desc)
            setResult(Activity.RESULT_OK, intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        val RECIPE_DESCRIPTION_KEY = "RECIPE_DESCRIPTION_KEY"

        private val PHOTO_REQUEST_CODE = 101
        private val RC_IMAGE_PERMS = 102
        private val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    }
}
