package com.cookplan.recipe_import.web_browser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.recipe_import.parsing_url.ImportRecipeActivity;

public class WebBrowserActivity extends BaseActivity {

    public static final String URL_KEY = "URL_KEY";
    private String recipeUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);
        setNavigationArrow();
        setTitle("Импорт");
        recipeUrl = getIntent().getStringExtra(URL_KEY);
        if (recipeUrl.isEmpty()) {
            finish();
        }

        WebView webView = (WebView) findViewById(R.id.recipe_webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                recipeUrl = url;
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.GONE);
                super.onPageCommitVisible(view, url);
            }
        });
        webView.loadUrl(recipeUrl);


        Button importButton = (Button) findViewById(R.id.import_btn);
        if (importButton != null) {
            importButton.setOnClickListener(view -> {
                Intent intent = new Intent(this, ImportRecipeActivity.class);
                intent.putExtra(ImportRecipeActivity.URL_TO_IMPORT_KEY, recipeUrl);
                startActivityWithLeftAnimation(intent);
            });
        }
    }
}
