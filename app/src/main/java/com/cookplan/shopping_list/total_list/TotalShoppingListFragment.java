package com.cookplan.shopping_list.total_list;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cookplan.R;
import com.cookplan.add_ingredient_view.AddIngredientViewFragment;
import com.cookplan.models.Ingredient;
import com.cookplan.models.ShopListStatus;

import java.util.ArrayList;
import java.util.List;


public class TotalShoppingListFragment extends Fragment implements TotalShoppingListView {

    private ViewGroup mainView;
    private TotalShoppingListPresenter presenter;
    private TotalShopListRecyclerViewAdapter needToBuyAdapter;
    private TotalShopListRecyclerViewAdapter alreadyBoughtAdapter;
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
        needToBuyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        needToBuyAdapter = new TotalShopListRecyclerViewAdapter(new ArrayList<>(), ingredient -> {
            if (presenter != null) {
                presenter.changeShopListStatus(ingredient, ShopListStatus.ALREADY_BOUGHT);
            }
        });
        needToBuyRecyclerView.setAdapter(needToBuyAdapter);

        RecyclerView alreadyBoughtRecyclerView = (RecyclerView) mainView.findViewById(R.id.total_bought_recycler);
        alreadyBoughtRecyclerView.setHasFixedSize(true);
        alreadyBoughtRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        alreadyBoughtAdapter = new TotalShopListRecyclerViewAdapter(new ArrayList<>(), ingredient -> {
            if (presenter != null) {
                presenter.changeShopListStatus(ingredient, ShopListStatus.NEED_TO_BUY);
            }
        });
        alreadyBoughtRecyclerView.setAdapter(alreadyBoughtAdapter);

        AddIngredientViewFragment fragment = AddIngredientViewFragment.newInstance(true);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        if (presenter != null) {
            presenter.getShoppingList();
        }
        progressBar.setVisibility(View.VISIBLE);
        return mainView;
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
    public void setIngredientLists(List<Ingredient> needToBuyIngredientList,
                                   List<Ingredient> alreadyBoughtIngredientList) {
        progressBar.setVisibility(View.GONE);
        ViewGroup needToBuyLayout = (ViewGroup) mainView.findViewById(R.id.need_to_buy_layout);
        if (!needToBuyIngredientList.isEmpty()) {
            setLayoutVisability(needToBuyLayout, View.VISIBLE);
            if (needToBuyAdapter != null) {
                needToBuyAdapter.update(needToBuyIngredientList);
            }
        } else {
            setLayoutVisability(needToBuyLayout, View.GONE);
        }

        ViewGroup alreadyBoughtLayout = (ViewGroup) mainView.findViewById(R.id.already_bought_layout);
        if (!alreadyBoughtIngredientList.isEmpty()) {
            setLayoutVisability(alreadyBoughtLayout, View.VISIBLE);
            if (alreadyBoughtAdapter != null) {
                alreadyBoughtAdapter.update(alreadyBoughtIngredientList);
            }
        } else {
            setLayoutVisability(alreadyBoughtLayout, View.GONE);
        }
    }

    private void setLayoutVisability(ViewGroup layoutView, int visability) {
        layoutView.setVisibility(visability);
    }
}
