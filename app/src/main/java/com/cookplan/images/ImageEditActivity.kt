package com.cookplan.images

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.cookplan.BaseActivity
import com.cookplan.R
import com.google.android.material.snackbar.Snackbar

class ImageEditActivity : BaseActivity(), ChangeImagesView, ActivityCompat.OnRequestPermissionsResultCallback {

    private var adapter: RecipeDescImagesPagerAdapter? = null
    private var presenter: ChangeImagesPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)
        setNavigationArrow()
        setTitle(getString(R.string.edit_image_list_title))

        val urls = intent.getStringArrayListExtra(START_IMAGE_URL_KEY)

        val viewPager = findViewById<ViewPager>(R.id.images_viewpager)
        adapter = RecipeDescImagesPagerAdapter(urls)

        val deleteImageLayout = findViewById<ViewGroup>(R.id.delete_image_layout)
        deleteImageLayout.setOnClickListener {

            AlertDialog.Builder(this)
                    .setMessage("Вы точно хотите удалить картинку? Вы не сможете восстановить удаленную фотографию.")
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        val progressBar = findViewById<View>(R.id.progress_bar) as ProgressBar
                        progressBar.visibility = View.VISIBLE
                        val currentImage = adapter?.getArrayListItems()?.get(viewPager.currentItem) ?: String()
                        presenter?.removePhoto(currentImage)
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
                    .show()
        }

        viewPager.adapter = adapter
        presenter = ChangeImagesPresenterImpl(this, this)
    }

    override fun setError(error: String) {
        val progressBar = findViewById<View>(R.id.progress_bar) as ProgressBar
        progressBar.visibility = View.GONE
        val mainView = findViewById<View>(R.id.main_view)
        if (mainView != null) {
            Snackbar.make(mainView, error, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun setImageSaved(url: String) {
    }

    override fun setImageRemoved(imageId: String) {
        val progressBar = findViewById<View>(R.id.progress_bar) as ProgressBar
        progressBar.visibility = View.GONE
        adapter?.removeImage(imageId)
        val viewPager = findViewById<ViewPager>(R.id.images_viewpager)
        viewPager?.destroyDrawingCache()
        if (adapter?.getArrayListItems()?.isEmpty() == true) {
            setResultIntent()
        }
    }

    override fun onCreateOptionsMenu(_menu: Menu): Boolean {
        menuInflater.inflate(R.menu.done_menu, _menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.app_bar_done) {
            setResultIntent()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setResultIntent() {
        val intent = Intent()
        intent.putStringArrayListExtra(RESULT_IMAGE_URL_KEY, adapter?.getArrayListItems())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        setResultIntent()
        super.onBackPressed()
    }

    companion object {

        val START_IMAGE_URL_KEY = "START_IMAGE_URL_KEY"
        val RESULT_IMAGE_URL_KEY = "RESULT_IMAGE_URL_KEY"
    }
}
