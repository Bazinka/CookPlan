package com.cookplan.recipe.list.select;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cookplan.BaseFragment;
import com.cookplan.R;
import com.cookplan.models.Recipe;
import com.cookplan.recipe.list.RecipeListPresenter;
import com.cookplan.recipe.list.RecipeListView;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

public class RecipeSelectListFragment extends BaseFragment implements RecipeListView {

    private RecipeSelectListRecyclerAdapter adapter;
    private RecipeListPresenter presenter;

    private RecipeSelectEventListener listener;

    public RecipeSelectListFragment() {
    }


    public static RecipeSelectListFragment newInstance() {
        RecipeSelectListFragment fragment = new RecipeSelectListFragment();
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
        presenter = new RecipeSelectListPresenterImpl(this);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecipeSelectListRecyclerAdapter(new ArrayList<>(), new RecipeSelectListRecyclerAdapter.RecipeListClickListener() {
            @Override
            public void onRecipeClick(Recipe recipe) {
                if (listener != null) {
                    listener.onRecipeSelected(recipe);
                }
            }
        }, getActivity());
        recyclerView.setAdapter(adapter);

        FloatingActionMenu menu = (FloatingActionMenu) mainView.findViewById(R.id.menu_yellow);
        menu.setVisibility(View.GONE);

        return mainView;
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

    public void setListener(RecipeSelectEventListener listener) {
        this.listener = listener;
    }

    public interface RecipeSelectEventListener {

        void onRecipeSelected(Recipe recipe);
    }
}
