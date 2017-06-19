package com.cookplan.recipe.grid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.BaseFragment;
import com.cookplan.R;
import com.cookplan.main.MainActivity;
import com.cookplan.models.Recipe;
import com.cookplan.recipe.import_recipe.search_url.SearchRecipeUrlActivity;
import com.cookplan.recipe.edit.add_info.EditRecipeInfoActivity;
import com.cookplan.recipe.view_item.RecipeViewActivity;
import com.cookplan.utils.GridSpacingItemDecoration;
import com.cookplan.utils.Utils;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RecipeGridFragment extends BaseFragment implements RecipeGridView {

    private RecipeGridRecyclerViewAdapter adapter;
    private RecipeGridPresenter presenter;

    public RecipeGridFragment() {
    }


    public static RecipeGridFragment newInstance() {
        RecipeGridFragment fragment = new RecipeGridFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        setRetainInstance(true);
        presenter = new RecipeGridPresenterImpl(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.getRecipeList();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = (ViewGroup) inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Set the adapter
        RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.recipe_list_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dpToPx(getActivity(), 16), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new RecipeGridRecyclerViewAdapter(new ArrayList<>(), new RecipeGridRecyclerViewAdapter.RecipeListClickListener() {
            @Override
            public void onRecipeClick(Recipe recipe) {
                Activity activity = getActivity();
                if (activity instanceof BaseActivity) {
                    Intent intent = new Intent(activity, RecipeViewActivity.class);
                    intent.putExtra(RecipeViewActivity.RECIPE_OBJECT_KEY, recipe);
                    ((BaseActivity) activity).startActivityForResultWithLeftAnimation(intent,
                                                                                      MainActivity.OPEN_SHOP_LIST_REQUEST);
                }
            }

            @Override
            public void onRecipeLongClick(Recipe recipe) {
                new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle).setTitle(R.string.attention_title)
                        .setMessage(R.string.are_you_sure_remove_recipe)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
                            progressBar.setVisibility(View.VISIBLE);
                            if (presenter != null) {
                                presenter.removeRecipe(recipe);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        }, getActivity());
        recyclerView.setAdapter(adapter);

        FloatingActionButton addRecipeFab = (FloatingActionButton) mainView.findViewById(R.id.add_recipe_fab);
        addRecipeFab.setOnClickListener(view -> {
            startNewRecipeActivity();
        });

        FloatingActionButton importRecipeFab = (FloatingActionButton) mainView.findViewById(R.id.import_recipe_fab);
        importRecipeFab.setOnClickListener(view -> {
            startSearchRecipeUrlActivity();
        });

        return mainView;
    }

    void startSearchRecipeUrlActivity() {
        Intent intent = new Intent(getActivity(), SearchRecipeUrlActivity.class);
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).startActivityWithLeftAnimation(intent);
        }
    }

    void startNewRecipeActivity() {
        Intent intent = new Intent(getActivity(), EditRecipeInfoActivity.class);
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).startActivityWithLeftAnimation(intent);
        }
    }

    @Override
    public void setEmptyView() {
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.VISIBLE);
        setRecyclerViewVisability(View.GONE);
    }

    private void setRecyclerViewVisability(int visability) {
        View recyclerView = mainView.findViewById(R.id.recipe_list_recycler);
        if (recyclerView != null) {
            recyclerView.setVisibility(visability);
        }
    }

    @Override
    public void setRecipeList(List<Recipe> recipeList) {
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.GONE);
        setRecyclerViewVisability(View.VISIBLE);
        adapter.updateItems(recipeList);
    }

    @Override
    public void setErrorToast(String error) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
        }
    }
}
