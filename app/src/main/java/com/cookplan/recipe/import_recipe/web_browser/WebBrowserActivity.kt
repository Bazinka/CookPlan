package com.cookplan.recipe.import_recipe.web_browser

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.recipe.import_recipe.approve_result.ImportRecipeActivity

class WebBrowserActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_browser)
        setNavigationArrow()
        setTitle(getString(R.string.recipe_in_internet_title))
        if (intent.hasExtra(URL_KEY)) {
            var recipeUrl = intent.getStringExtra(URL_KEY)
            val webView = findViewById<WebView>(R.id.recipe_webView)
            val webSettings = webView.settings
            webSettings.javaScriptEnabled = true
            webView.isVerticalScrollBarEnabled = true
            webView.isHorizontalScrollBarEnabled = true
            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    recipeUrl = url
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageCommitVisible(view: WebView?, url: String?) {
                    val progressBar = findViewById<View>(R.id.progress_bar) as ProgressBar
                    progressBar.visibility = View.GONE
                    super.onPageCommitVisible(view, url)
                }
            }
            webView.loadUrl(recipeUrl)


            val importButton = findViewById<Button>(R.id.import_btn) as Button
            importButton.setOnClickListener { view ->
                val intent = Intent(this, ImportRecipeActivity::class.java)
                intent.putExtra(ImportRecipeActivity.URL_TO_IMPORT_KEY, recipeUrl)
                startActivityWithLeftAnimation(intent)
                finish()
            }
        } else {
            finish()
        }
    }

    companion object {

        val URL_KEY = "URL_KEY"
    }
}
