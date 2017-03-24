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

import java.util.ArrayList;
import java.util.List;


public class TotalShoppingListFragment extends Fragment implements TotalShoppingListView {

    private OnListFragmentInteractionListener mListener;
    private ViewGroup mainView;
    private TotalShoppingListPresenter presenter;
    private TotalShopListRecyclerViewAdapter adapter;

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
        presenter = new TotalShoppingListPresenterImpl(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = (ViewGroup) inflater.inflate(R.layout.fragment_total_shop_list, container, false);

        RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.total_shop_list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new TotalShopListRecyclerViewAdapter(new ArrayList<>(), mListener);
        recyclerView.setAdapter(adapter);

        AddIngredientViewFragment fragment = AddIngredientViewFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        if (presenter != null) {
            presenter.getShoppingList();
        }
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
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
    public void setIngredientList(List<Ingredient> ingredientList) {
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        if (adapter != null) {
            adapter.update(ingredientList);
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Ingredient ingredient);
    }
}
