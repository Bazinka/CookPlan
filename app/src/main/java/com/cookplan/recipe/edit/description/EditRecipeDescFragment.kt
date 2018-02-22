package com.cookplan.recipe.edit.description

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.cookplan.BaseFragment
import com.cookplan.R
import com.cookplan.utils.PermissionUtils
import java.util.*


/**
 * Created by DariaEfimova on 19.01.2018.
 */
class EditRecipeDescFragment : BaseFragment(), EditRecipeDescView {

    private var mProgressDialog: ProgressDialog? = null
    private var presenter: EditRecipeDescPresenter? = null

    private var language: String = String()

    private var description: String = String()
    private var isTextNeedsToReload: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        description = arguments?.getString(RECIPE_DESCRIPTION_KEY) ?: String()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.edit_recipe_description_fragment, container, false) as ViewGroup

        val recipeDescEditText = mainView?.findViewById<EditText>(R.id.recipe_process_edit_text)
        recipeDescEditText?.setOnTouchListener { v, event ->
            if (v.id == R.id.recipe_process_edit_text) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }
        recipeDescEditText?.setText(description)

        val recognizeButton = mainView?.findViewById<View>(R.id.recognize_card_view)
        recognizeButton?.setOnClickListener {
            AlertDialog.Builder(activity)
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

        val voiceTypingButton = mainView?.findViewById<View>(R.id.micro_card_view)
        voiceTypingButton?.setOnClickListener {
            AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.desc_will_be_reload_title))
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                        startSpeechRecognitionActivity()
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
        }
        presenter = EditRecipeDescPresenterImpl(this, activity as Context)
        return mainView
    }

    private fun startSpeechRecognitionActivity() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speak_please_title))
        startActivityForResult(intent, VOICE_TYPE_CHECK_CODE)
    }

    private fun startCameraWithPermCheck() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtils.isPermissionsGranted(activity, permission)) {
                PermissionUtils.requestPermissions(activity, RC_IMAGE_PERMS, permission)
            } else {
                startCameraActivity()
            }
        }
    }

    /**
     * to get high resolution image from camera
     */
    private fun startCameraActivity() {
        if (PermissionUtils.isPermissionsGranted(activity, permission)) {
            val outputFileUri = presenter?.getOutputImagePath()
            if (outputFileUri != null) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)

                startActivityForResult(takePictureIntent, PHOTO_REQUEST_CODE)
            } else {
                setErrorToast(getString(R.string.error_generate_image_path))
            }
        } else {
            setErrorToSnackBar(getString(R.string.permission_denied))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        //making photo
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                VOICE_TYPE_CHECK_CODE -> {
                    if (resultCode == Activity.RESULT_OK) {
                        val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        val recipeDescEditText = mainView?.findViewById<EditText>(R.id.recipe_process_edit_text)
                        var string = result?.get(0) ?: String()
                        if (!isTextNeedsToReload) {
                            string = recipeDescEditText?.text.toString() + "\n " + string
                        }
                        recipeDescEditText?.setText(string)
                        AlertDialog.Builder(activity)
                                .setMessage(getString(R.string.next_vioce_typing_question))
                                .setPositiveButton(android.R.string.yes) { _, _ ->
                                    isTextNeedsToReload = false
                                    startSpeechRecognitionActivity()
                                }
                                .setNegativeButton(android.R.string.no) { _, _ ->
                                    isTextNeedsToReload = true
                                }
                                .show()
                    }
                }
                PHOTO_REQUEST_CODE -> {
                    if (mProgressDialog == null) {
                        mProgressDialog = ProgressDialog.show(activity, getString(R.string.processing_title),
                                getString(R.string.processing_ocr_message), true)
                    } else {
                        mProgressDialog?.show()
                    }
                    presenter?.doOCR(language)
                }
            }
        }
    }


    override fun setErrorToSnackBar(error: String) {
        super.setErrorToast(error)
    }


    override fun setAsyncTextResult(result: String) {
        activity?.runOnUiThread {
            if (result != "") {
                val textView = mainView?.findViewById<EditText>(R.id.recipe_process_edit_text)
                textView?.setText(result)
            }
            mProgressDialog?.dismiss()
        }
    }

    override fun setAsyncErrorToSnackBar(error: String) {
        activity?.runOnUiThread {
            setErrorToast(error)
            mProgressDialog?.dismiss()
        }
    }

    fun getDescription(): String? {
        val recipeDescEditText = mainView?.findViewById<EditText>(R.id.recipe_process_edit_text)
        var desc = recipeDescEditText?.text.toString()
        desc = if (desc.isEmpty()) getString(R.string.recipe_desc_is_not_needed_title) else desc
        return desc
    }

    fun requestPermissionsResult(grantResults: IntArray) {
        val permGranded = grantResults.none { it != PackageManager.PERMISSION_GRANTED }

        if (grantResults.size == permission.size && permGranded) {
            startCameraActivity()
        } else {
            setErrorToSnackBar(getString(R.string.permission_denied))
        }
    }

    companion object {
        private val RECIPE_DESCRIPTION_KEY = "RECIPE_DESCRIPTION_KEY"

        private val PHOTO_REQUEST_CODE = 101
        val RC_IMAGE_PERMS = 102

        private val VOICE_TYPE_CHECK_CODE = 104

        private val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

        @JvmStatic
        fun newInstance(desc: String): EditRecipeDescFragment {
            return EditRecipeDescFragment().apply {
                arguments = Bundle().apply {
                    putString(RECIPE_DESCRIPTION_KEY, desc)
                }
            }
        }

        @JvmStatic
        fun newInstance(): EditRecipeDescFragment {
            return EditRecipeDescFragment()
        }
    }
}
