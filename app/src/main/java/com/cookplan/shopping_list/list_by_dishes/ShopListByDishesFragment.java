package com.cookplan.shopping_list.list_by_dishes;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.cookplan.BaseFragment;
import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShopListStatus;

import java.util.List;
import java.util.Map;


public class ShopListByDishesFragment extends BaseFragment implements ShopListByDishesView {

    private ShopListByDishPresenter presenter;
    private ProgressBar progressBar;

    public ShopListByDishesFragment() {
    }

    public static ShopListByDishesFragment newInstance() {
        ShopListByDishesFragment fragment = new ShopListByDishesFragment();
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
        presenter = new ShopListByDishesPresenterImpl(this, getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = (ViewGroup) inflater.inflate(R.layout.fragment_shop_list_by_dish, container, false);

        progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);

        if (presenter != null) {
            presenter.getShoppingList();
        }

        progressBar.setVisibility(View.VISIBLE);


        return mainView;
    }

    @Override
    public void setErrorToast(String error) {
        Snackbar.make(mainView, getString(R.string.error_load_shop_list), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setEmptyView() {
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.VISIBLE);
        setContentVisability(View.GONE);
    }

    private void setContentVisability(int visability) {
        ViewGroup contentLayout = (ViewGroup) mainView.findViewById(R.id.main_content_layout);
        if (contentLayout != null) {
            contentLayout.setVisibility(visability);
        }
    }

    @Override
    public void setIngredientListToRecipe(Map<Recipe, List<Ingredient>> recipeToingredientsMap) {
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.GONE);
        setContentVisability(View.VISIBLE);

        ExpandableListView expandableListView = (ExpandableListView) mainView.findViewById(R.id.shop_list_by_dish_expListView);
        ShopListExpandableListAdapter needToBuyAdapter = new ShopListExpandableListAdapter(getActivity(),
                recipeToingredientsMap, new ShopListExpandableListAdapter.OnItemClickListener() {
            @Override
            public void onChildClick(Ingredient ingredient) {
                ShopListStatus newStatus;
                if (ingredient.getShopListStatus() == ShopListStatus.NEED_TO_BUY) {
                    newStatus = ShopListStatus.ALREADY_BOUGHT;
                } else {
                    newStatus = ShopListStatus.NEED_TO_BUY;
                }
                if (presenter != null) {
                    presenter.setIngredientBought(ingredient, newStatus);
                }
            }

            @Override
            public void onGroupClick(Recipe recipe, List<Ingredient> ingredientList) {
                new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle).setTitle(R.string.attention_title)
                        .setMessage(R.string.delete_recipe_from_shop_list_question)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            if (presenter != null) {
                                presenter.setRecipeIngredBought(recipe, ingredientList);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

            }
        }
        );
        expandableListView.setAdapter(needToBuyAdapter);
        int count = recipeToingredientsMap.keySet().size();
        for (int position = 1; position <= count; position++) {
            expandableListView.expandGroup(position - 1);
        }
    }
}