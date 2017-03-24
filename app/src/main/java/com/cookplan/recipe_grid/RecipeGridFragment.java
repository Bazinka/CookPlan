package com.cookplan.recipe_grid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.models.Recipe;
import com.cookplan.recipe_view.RecipeViewActivity;
import com.cookplan.utils.GridSpacingItemDecoration;
import com.cookplan.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class RecipeGridFragment extends Fragment implements RecipeGridView {

    private RecipeGridRecyclerViewAdapter adapter;
    private RecipeGridPresenter presenter;
    private ViewGroup mainView;

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
        presenter.getAsyncRecipeList();
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
        adapter = new RecipeGridRecyclerViewAdapter(new ArrayList<>(), recipe -> {
            Activity activity = getActivity();
            if (activity instanceof BaseActivity) {
                Intent intent = new Intent(activity, RecipeViewActivity.class);
                intent.putExtra(RecipeViewActivity.RECIPE_OBJECT_KEY, recipe);
                ((BaseActivity) activity).startActivityWithLeftAnimation(intent);
            }
        });
        recyclerView.setAdapter(adapter);


        return mainView;
    }

    @Override
    public void setRecipeList(List<Recipe> recipeList) {
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        adapter.updateItems(recipeList);
    }

    @Override
    public void setErrorToast(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }
}
