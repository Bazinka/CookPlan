package com.cookplan.shopping_list.total_list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookplan.R;
import com.cookplan.add_ingredient_view.AddIngredientViewFragment;
import com.cookplan.models.Ingredient;

import java.util.ArrayList;


public class TotalShoppingListFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    private ViewGroup mainView;

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
//        presenter = new (this);
//        presenter.getAsyncRecipeList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = (ViewGroup) inflater.inflate(R.layout.fragment_total_shop_list, container, false);

        RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.total_shop_list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(new TotalShopListRecyclerViewAdapter(new ArrayList<>(), mListener));

        AddIngredientViewFragment fragment = AddIngredientViewFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        return mainView;
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Ingredient ingredient);
    }
}
