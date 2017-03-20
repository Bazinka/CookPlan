package com.cookplan.shopping_list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Product;

import java.util.ArrayList;
import java.util.List;


public class ShoppingListFragment extends Fragment implements ShoppingListView {

    private ShoppingListPresenter presenter;
    private ShoppingListRecyclerAdapter adapter;

    private ViewGroup unitAmountViewGroup;
    private View mainView;

//    private OnListFragmentInteractionListener mListener;

    public ShoppingListFragment() {
    }

    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ShoppingListPresenterImpl(this);
        presenter.getAsyncProductList();
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.shopping_list_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ShoppingListRecyclerAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        unitAmountViewGroup = (ViewGroup) mainView.findViewById(R.id.unit_amount_main_layout);

        AutoCompleteTextView unitNameEditText = (AutoCompleteTextView) mainView.findViewById(R.id.product_name_text);
        unitNameEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (count == 0) {
                    unitAmountViewGroup.setVisibility(View.GONE);
                } else {
                    unitAmountViewGroup.setVisibility(View.VISIBLE);
                }
            }
        });

        return mainView;
    }

    @Override
    public void setErrorToast(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setProductsList(List<Product> productsList) {
        AutoCompleteTextView unitNameEditText = (AutoCompleteTextView) mainView.findViewById(R.id.product_name_text);
        ProductListAdapter adapter = new ProductListAdapter(getActivity(), productsList);
        unitNameEditText.setAdapter(adapter);

        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        View mainViewGroup = mainView.findViewById(R.id.main_view);
        mainViewGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void setIngredientList(List<Ingredient> ingredientList) {
        adapter.updateItems(ingredientList);
    }


//    public interface OnListFragmentInteractionListener {
//         void onListFragmentInteraction();
//    }
}
