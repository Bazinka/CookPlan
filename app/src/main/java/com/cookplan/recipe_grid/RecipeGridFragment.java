package com.cookplan.recipe_grid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cookplan.R;
import com.cookplan.models.Recipe;
import com.cookplan.utils.GridSpacingItemDecoration;
import com.cookplan.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class RecipeGridFragment extends Fragment implements RecipeGridView {

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
        presenter = new RecipeGridPresenterImpl(this);
        presenter.getAsyncRecipeList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dpToPx(getActivity(), 16), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            adapter = new RecipeGridRecyclerViewAdapter(new ArrayList<>());
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void setRecipeList(List<Recipe> recipeList) {
        adapter.updateItems(recipeList);
    }

    @Override
    public void setErrorToast(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }


}
