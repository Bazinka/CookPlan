package com.cookplan.recipe.list.main;

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
import android.widget.TextView;
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.BaseFragment;
import com.cookplan.R;
import com.cookplan.main.MainActivity;
import com.cookplan.models.Recipe;
import com.cookplan.recipe.edit.add_info.EditRecipeInfoActivity;
import com.cookplan.recipe.import_recipe.search_url.SearchRecipeUrlActivity;
import com.cookplan.recipe.view_item.RecipeViewActivity;
import com.cookplan.utils.Constants;
import com.cookplan.views.ChooseCookingTimeView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class RecipeListFragment extends BaseFragment implements MainRecipeListView {

    private RecipeListRecyclerViewAdapter adapter;
    private MainRecipeListPresenter presenter;

    public RecipeListFragment() {
    }


    public static RecipeListFragment newInstance() {
        RecipeListFragment fragment = new RecipeListFragment();
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
        presenter = new MainRecipeListPresenterImpl(this);

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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new RecipeListRecyclerViewAdapter(
                new ArrayList<>(),
                new RecipeListRecyclerViewAdapter.RecipeListClickListener() {
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

                    @Override
                    public void onAddToCookPlanClick(Recipe recipe) {
                        boolean addingToCookPlan;
                        if (recipe.getCookingDate() != null && !recipe.getCookingDate().isEmpty()) {
                            addingToCookPlan = false;
                        } else {
                            addingToCookPlan = true;
                        }
                        if (addingToCookPlan) {
                            addRecipeToCookPlan(recipe);
                        } else {
                            removeRecipeToCookPlan(recipe);
                        }
                    }
                }, getActivity());
        recyclerView.setAdapter(adapter);

        FloatingActionButton addRecipeFab = (FloatingActionButton) mainView.findViewById(R.id.add_recipe_fab);
        addRecipeFab.setOnClickListener(view -> {
            FloatingActionMenu menu = (FloatingActionMenu) mainView.findViewById(R.id.menu_yellow);
            menu.setClosedOnTouchOutside(true);
            startNewRecipeActivity();
        });

        FloatingActionButton importRecipeFab = (FloatingActionButton) mainView.findViewById(R.id.import_recipe_fab);
        importRecipeFab.setOnClickListener(view -> {
            FloatingActionMenu menu = (FloatingActionMenu) mainView.findViewById(R.id.menu_yellow);
            menu.setClosedOnTouchOutside(true);
            startSearchRecipeUrlActivity();
        });

        return mainView;
    }

    private void addRecipeToCookPlan(Recipe recipe) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_recipe_to_cookplan_layout, null);

        String message = getString(R.string.choose_date_for_cooking_dialog) + " " + recipe.getName();

        TextView messageTextView = (TextView) dialogView.findViewById(R.id.choose_time_dialog_message);
        messageTextView.setText(message);
        ChooseCookingTimeView chooseCookingTimeView = (ChooseCookingTimeView) dialogView.findViewById(
                R.id.choose_cooking_time_dialog_view);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        dialogBuilder
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    dialog.dismiss();
                    if (chooseCookingTimeView != null && recipe != null) {
                        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
                        progressBar.setVisibility(View.VISIBLE);
                        Constants.TypeOfTime selectedTime = chooseCookingTimeView.getSelectedTime();
                        DateTime choosenDate = chooseCookingTimeView.getSelectedDate();
                        if (selectedTime != null && choosenDate != null) {
                            presenter.addRecipeToCookPlan(
                                    recipe,
                                    choosenDate.withHourOfDay(selectedTime.getHour())
                                            .withMinuteOfHour(selectedTime.getMinute()));
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void removeRecipeToCookPlan(Recipe recipe) {
        String message = getString(R.string.are_you_sure_remove_recipe_from_cookplan_part1)
                + " " + recipe.getName() + " " +
                getString(R.string.are_you_sure_remove_recipe_from_cookplan_part2);
        new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                .setTitle(R.string.attention_title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    dialog.dismiss();
                    ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
                    progressBar.setVisibility(View.VISIBLE);
                    if (presenter != null) {
                        presenter.removeRecipeFromCookPlan(recipe);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
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
    public void setError(String error) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
        }
    }
}
