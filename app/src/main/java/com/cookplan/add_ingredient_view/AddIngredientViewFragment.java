package com.cookplan.add_ingredient_view;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.cookplan.R;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.models.Recipe;

import java.util.ArrayList;
import java.util.List;


public class AddIngredientViewFragment extends Fragment implements AddIngredientView {

    public static final String RECIPE_OBJECT_KEY = "new_recipe_name";
    public static final String RECIPE_NEED_TO_BUY_KEY = "is_need_to_buy";

    private AddIngredientPresenter presenter;

    private ViewGroup unitAmountViewGroup;
    private ProgressBar progressBar;
    private View mainView;

    //    private OnListFragmentInteractionListener mListener;

    public AddIngredientViewFragment() {
    }

    public static AddIngredientViewFragment newInstance(boolean isNeedToBuy) {
        AddIngredientViewFragment fragment = new AddIngredientViewFragment();
        Bundle args = new Bundle();
        args.putBoolean(RECIPE_NEED_TO_BUY_KEY, isNeedToBuy);
        fragment.setArguments(args);
        return fragment;
    }

    public static AddIngredientViewFragment newInstance(Recipe recipe, boolean isNeedToBuy) {
        AddIngredientViewFragment fragment = new AddIngredientViewFragment();
        Bundle args = new Bundle();
        if (recipe != null) {
            args.putSerializable(RECIPE_OBJECT_KEY, recipe);
        }
        args.putBoolean(RECIPE_NEED_TO_BUY_KEY, isNeedToBuy);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new AddIngredientPresenterImpl(this);
        presenter.getAsyncProductList();
        if (getArguments() != null) {
            Recipe recipe = (Recipe) getArguments().getSerializable(RECIPE_OBJECT_KEY);
            if (recipe != null) {
                presenter.setRecipe(recipe);
            }
            presenter.setIsNeedToBuy(getArguments().getBoolean(RECIPE_NEED_TO_BUY_KEY, false));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_add_ingredient_view, container, false);

        unitAmountViewGroup = (ViewGroup) mainView.findViewById(R.id.unit_amount_main_layout);

        progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        AutoCompleteTextView unitNameEditText = (AutoCompleteTextView) mainView.findViewById(R.id.product_name_text);
        unitNameEditText.requestFocus();
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

        ImageButton saveProductButton = (ImageButton) mainView.findViewById(R.id.save_product_image_button);
        saveProductButton.setOnClickListener(view -> {
            EditText unitAmoutEditText = (EditText) mainView.findViewById(R.id.unit_amount_edit_text);
            if (checkProductField() && unitAmoutEditText != null) {
                String text = unitAmoutEditText.getText().toString();
                if (!text.isEmpty()) {
                    saveInrgedient(text);
                } else {
                    new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle).setTitle(R.string.attention_title)
                            .setMessage(R.string.product_amount_not_exist)
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> saveInrgedient(null))
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                }
            }
        });
        return mainView;
    }

    private boolean checkProductField() {
        TextInputLayout productLayout = (TextInputLayout) mainView.findViewById(R.id.product_name_edit_layout);
        AutoCompleteTextView unitNameEditText = (AutoCompleteTextView) mainView.findViewById(R.id.product_name_text);
        String name = unitNameEditText.getText().toString();
        Product selectedProduct = (Product) unitNameEditText.getTag();
        if (selectedProduct == null || !name.equals(selectedProduct.getName())) {
            productLayout.setError(getString(R.string.product_required_field));
            productLayout.setErrorEnabled(true);
            return false;
        } else {
            productLayout.setError(null);
            productLayout.setErrorEnabled(false);
            return true;
        }
    }

    void saveInrgedient(String amount) {
        Spinner spinner = (Spinner) mainView.findViewById(R.id.measure_list_spinner);
        AutoCompleteTextView unitNameEditText = (AutoCompleteTextView) mainView.findViewById(R.id.product_name_text);
        String name = unitNameEditText.getText().toString();
        Product selectedProduct = (Product) unitNameEditText.getTag();
        if (!name.isEmpty() && presenter != null) {
            progressBar.setVisibility(View.VISIBLE);
            presenter.saveIngredient(selectedProduct,
                                     amount != null ? Double.valueOf(amount) : 0,
                                     (MeasureUnit) spinner.getSelectedItem());
            unitNameEditText.setText(null);
            EditText unitAmoutEditText = (EditText) mainView.findViewById(R.id.unit_amount_edit_text);
            unitAmoutEditText.setText(null);
        }
    }

    @Override
    public void setErrorToast(String error) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setErrorToast(int errorId) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), getString(errorId), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setProductsList(List<Product> productsList) {
        AutoCompleteTextView unitNameEditText = (AutoCompleteTextView) mainView.findViewById(R.id.product_name_text);
        ProductListAdapter adapter = new ProductListAdapter(getActivity(), productsList);
        unitNameEditText.setAdapter(adapter);

        unitNameEditText.setOnItemClickListener((parent, arg1, pos, id) -> {
            Product product = productsList.get(pos);
            if (product != null) {
                unitNameEditText.setTag(product);
                EditText unitAmountEditText = (EditText) mainView.findViewById(R.id.unit_amount_edit_text);
                unitAmountEditText.requestFocus();
                setSpinnerValues();
            }
        });

        unitNameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                setSpinnerValues();
            }
        });

        progressBar.setVisibility(View.GONE);

        View mainViewGroup = mainView.findViewById(R.id.main_view);
        mainViewGroup.setVisibility(View.VISIBLE);
    }

    void setSpinnerValues() {
        AutoCompleteTextView unitNameEditText = (AutoCompleteTextView) mainView.findViewById(R.id.product_name_text);
        Spinner spinner = (Spinner) mainView.findViewById(R.id.measure_list_spinner);
        if (spinner != null) {
            Product selectedProduct = (Product) unitNameEditText.getTag();
            if (selectedProduct != null) {
                List<MeasureUnit> measureUnits = new ArrayList<>();
                measureUnits.addAll(selectedProduct.getMeasureUnitList());
                MeasureUnitsSpinnerAdapter adapter = new MeasureUnitsSpinnerAdapter(getActivity(),
                                                                                    measureUnits,
                                                                                    selectedProduct.getMainMeasureUnitList());
                spinner.setAdapter(adapter);
                spinner.setSelection(0);
            }
        }
    }

    @Override
    public void setSuccessSaveIngredient() {
        progressBar.setVisibility(View.GONE);
        unitAmountViewGroup.setVisibility(View.GONE);
    }

    @Override
    public boolean isAddedToActivity() {
        return isAdded();
    }


    //    public interface OnListFragmentInteractionListener {
    //        void onListFragmentInteraction();
    //    }
}
