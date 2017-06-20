package com.cookplan.cooking_plan.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.BaseFragment;
import com.cookplan.R;
import com.cookplan.cooking_plan.add.AddCookingItemActivity;
import com.cookplan.main.MainActivity;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.recipe.view_item.RecipeViewActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookingPlanFragment extends BaseFragment implements CookingPlanView {

    private CookPlanMainRecyclerAdapter adapter;
    private CookingPlanPresenter presenter;

    public CookingPlanFragment() {
    }


    public static CookingPlanFragment newInstance() {
        CookingPlanFragment fragment = new CookingPlanFragment();
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
        presenter = new CookingPlanPresenterImpl(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.getCookingPlan();
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
        mainView = (ViewGroup) inflater.inflate(R.layout.fragment_cooking_plan_list, container, false);
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Set the adapter
        RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.cooking_list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new CookPlanMainRecyclerAdapter(
                new HashMap<>(),
                new CookPlanMainRecyclerAdapter.CookPlanClickListener() {
                    @Override
                    public void onRecipeClick(Recipe recipe) {
                        Activity activity = getActivity();
                        if (activity instanceof BaseActivity) {
                            Intent intent = new Intent(activity, RecipeViewActivity.class);
                            intent.putExtra(RecipeViewActivity.RECIPE_OBJECT_KEY, recipe);
                            ((BaseActivity) activity).startActivityForResultWithLeftAnimation(
                                    intent, MainActivity.OPEN_SHOP_LIST_REQUEST);
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
                                        presenter.removeRecipeFromCookingPlan(recipe);
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, null)
                                .show();
                    }

                    @Override
                    public void onIngredientLongClick(Ingredient ingredient) {
                        new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle).setTitle(R.string.attention_title)
                                .setMessage(R.string.are_you_sure_remove_ingredient)
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
                                    progressBar.setVisibility(View.VISIBLE);
                                    if (presenter != null) {
                                        presenter.removeIngredientFromCookingPlan(ingredient);
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, null)
                                .show();
                    }
                }, getActivity());
        recyclerView.setAdapter(adapter);

        FloatingActionButton addRecipeFab = (FloatingActionButton) mainView.findViewById(R.id.add_recipe_fab);
        addRecipeFab.setOnClickListener(view -> {
            FloatingActionMenu menu = (FloatingActionMenu) mainView.findViewById(R.id.menu_yellow);
            menu.setClosedOnTouchOutside(true);
            startAddCookingPlanItemActivity(true);
        });

        FloatingActionButton addIngredientFab = (FloatingActionButton) mainView.findViewById(R.id.add_ingredient_fab);
        addIngredientFab.setOnClickListener(view -> {
            FloatingActionMenu menu = (FloatingActionMenu) mainView.findViewById(R.id.menu_yellow);
            menu.setClosedOnTouchOutside(true);
            startAddCookingPlanItemActivity(false);
        });

        return mainView;
    }

    void startAddCookingPlanItemActivity(boolean isRecipeAdding) {
        Intent intent = new Intent(getActivity(), AddCookingItemActivity.class);
        intent.putExtra(AddCookingItemActivity.IS_RECIPE_NEEDED_TO_ADD_KEY, isRecipeAdding);
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).startActivityWithLeftAnimation(intent);
        }
    }

    @Override
    public void setEmptyView() {
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.VISIBLE);
        View recyclerView = mainView.findViewById(R.id.cooking_list_recycler);
        if (recyclerView != null) {
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setCookingList(Map<LocalDate, List<Object>> dateToObjectMap) {
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.GONE);
        adapter.updateItems(dateToObjectMap);
        RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.cooking_list_recycler);
        if (recyclerView != null) {
            recyclerView.scrollToPosition(adapter.getPositionForToday());
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setErrorToast(String error) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
        }
    }
}
