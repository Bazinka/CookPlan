package com.cookplan.shopping_list.total_list;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cookplan.R;
import com.cookplan.add_ingredient_view.AddIngredientViewFragment;
import com.cookplan.models.Ingredient;
import com.cookplan.models.ShopListStatus;
import com.cookplan.share.BaseShareFragment;

import java.util.ArrayList;
import java.util.List;


public class TotalShoppingListFragment extends BaseShareFragment implements TotalShoppingListView {

    private TotalShoppingListPresenter presenter;
    private TotalShopListRecyclerViewAdapter needToBuyAdapter;
    private ProgressBar progressBar;

    public TotalShoppingListFragment() {
    }

    public static TotalShoppingListFragment newInstance() {
        TotalShoppingListFragment fragment = new TotalShoppingListFragment();
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
        presenter = new TotalShoppingListPresenterImpl(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = (ViewGroup) inflater.inflate(R.layout.fragment_total_shop_list, container, false);

        progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);

        RecyclerView needToBuyRecyclerView = (RecyclerView) mainView.findViewById(R.id.total_need_to_buy_recycler);
        needToBuyRecyclerView.setHasFixedSize(true);
        needToBuyRecyclerView.setNestedScrollingEnabled(false);
        needToBuyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        needToBuyAdapter = new TotalShopListRecyclerViewAdapter(new ArrayList<>(), ingredient -> {
            if (presenter != null) {
                if (ingredient.getShopListStatus() == ShopListStatus.NEED_TO_BUY) {
                    ingredient.setShopListStatus(ShopListStatus.ALREADY_BOUGHT);
                    presenter.changeShopListStatus(ingredient, ShopListStatus.ALREADY_BOUGHT);
                } else if (ingredient.getShopListStatus() == ShopListStatus.ALREADY_BOUGHT) {
                    ingredient.setShopListStatus(ShopListStatus.NEED_TO_BUY);
                    presenter.changeShopListStatus(ingredient, ShopListStatus.NEED_TO_BUY);
                }
                needToBuyAdapter.notifyDataSetChanged();
            }
        });
        needToBuyRecyclerView.setAdapter(needToBuyAdapter);

        AddIngredientViewFragment fragment = AddIngredientViewFragment.newInstance(true);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        ImageView deleteImageButton = (ImageView) mainView.findViewById(R.id.delete_image_view);
        deleteImageButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                    .setTitle(R.string.delete_shop_list_title)
                    .setMessage(R.string.choose_right_delete_mode)
                    .setPositiveButton(R.string.delete_already_bought, (dialog, which) -> {
                        List<Ingredient> ingredients = new ArrayList<>();
                        for (Ingredient ingredient : needToBuyAdapter.getIngredients()) {
                            if (ingredient.getShopListStatus() == ShopListStatus.ALREADY_BOUGHT) {
                                ingredients.add(ingredient);
                            }
                        }
                        progressBar.setVisibility(View.VISIBLE);
                        if (presenter != null) {
                            presenter.deleteIngredients(ingredients);
                        }
                    })
                    .setNeutralButton(android.R.string.cancel, null)
                    .setNegativeButton(R.string.delete_all_items_title, (dialog, which) -> {
                        progressBar.setVisibility(View.VISIBLE);
                        if (presenter != null) {
                            presenter.deleteIngredients(needToBuyAdapter.getIngredients());
                        }
                    })
                    .show();
        });
        return mainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.getShoppingList();
        }
        setEmptyView();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void setIngredientLists(List<Ingredient> allIngredientList) {
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.GONE);
        setContentVisability(View.VISIBLE);
        ViewGroup needToBuyLayout = (ViewGroup) mainView.findViewById(R.id.need_to_buy_layout);
        if (!allIngredientList.isEmpty()) {
            setLayoutVisability(needToBuyLayout, View.VISIBLE);
            if (needToBuyAdapter != null) {
                needToBuyAdapter.update(allIngredientList);
            }
        } else {
            setLayoutVisability(needToBuyLayout, View.GONE);
            setEmptyView();
        }
    }

    private void setLayoutVisability(ViewGroup layoutView, int visability) {
        layoutView.setVisibility(visability);
    }
}