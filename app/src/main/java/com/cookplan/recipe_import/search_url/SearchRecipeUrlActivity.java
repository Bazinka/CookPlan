package com.cookplan.recipe_import.search_url;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.models.network.GoogleRecipe;
import com.cookplan.recipe_import.web_browser.WebBrowserActivity;

import java.util.List;

public class SearchRecipeUrlActivity extends BaseActivity implements SearchRecipeUrlView {

    private SearchRecipeUrlPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe_url);
        setNavigationArrow();
        setTitle(getString(R.string.search_recipe_title));

        presenter = new SearchRecipeUrlPresenterImpl(this);
        Button searchButton = (Button) findViewById(R.id.search_btn);
        searchButton.setOnClickListener(view -> {
            EditText queryEdiText = (EditText) findViewById(R.id.text_editText);
            String query = queryEdiText.getText().toString();
            if (!query.isEmpty() && presenter != null) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                                     InputMethodManager.HIDE_NOT_ALWAYS);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                presenter.searchRecipes(query);
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_results_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public void setResultGoogleSearchList(List<GoogleRecipe> googleRecipes) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        GoogleRecipeListRecyclerAdapter adapter = new GoogleRecipeListRecyclerAdapter(
                googleRecipes, url -> {
            Intent intent = new Intent(this, WebBrowserActivity.class);
            intent.putExtra(WebBrowserActivity.URL_KEY, url);
            startActivityWithLeftAnimation(intent);
        }, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_results_recycler_view);
        recyclerView.setAdapter(adapter);
    }
}