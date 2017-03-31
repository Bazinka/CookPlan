package com.cookplan.add_ingredient_view;

import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.cookplan.R;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.models.ProductCategory;
import com.cookplan.models.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
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
            if (unitAmoutEditText != null) {
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

    void saveInrgedient(String amount) {
        Spinner spinner = (Spinner) mainView.findViewById(R.id.measure_list_spinner);
        MeasureUnit measureUnit = (MeasureUnit) spinner.getSelectedItem();
        AutoCompleteTextView unitNameEditText = (AutoCompleteTextView) mainView.findViewById(R.id.product_name_text);
        String name = unitNameEditText.getText().toString();
        Product selectedProduct = (Product) unitNameEditText.getTag();
        if (selectedProduct == null || !name.equals(selectedProduct.getName())) {
            selectedProduct = new Product(measureUnit, name, ProductCategory.WITHOUT_CATEGORY);
        }
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
            String text = unitNameEditText.getText().toString();
            Product selectedProduct = (Product) unitNameEditText.getTag();
            //if this product is already exist
            if (!text.isEmpty()
                    && selectedProduct != null
                    && text.equals(selectedProduct.getName())) {
                List<MeasureUnit> measureUnits = new ArrayList<>();
                measureUnits.add(selectedProduct.getMainMeasureUnit());
                measureUnits.addAll(selectedProduct.getMeasureUnitToAmoutMap().keySet());
                List<MeasureUnit> productsUnits = new ArrayList<>(measureUnits);
                for (MeasureUnit unit : MeasureUnit.values()) {
                    if (!measureUnits.contains(unit)) {
                        measureUnits.add(unit);
                    }
                }
                MeasureUnitsSpinnerAdapter adapter = new MeasureUnitsSpinnerAdapter(getActivity(),
                                                                                    measureUnits,
                                                                                    productsUnits);
                spinner.setAdapter(adapter);
                spinner.setSelection(0);
            } else if (!text.isEmpty()) {//if this product doesn't  exist
                MeasureUnitsSpinnerAdapter adapter = new MeasureUnitsSpinnerAdapter(getActivity(),
                                                                                    Arrays.asList(MeasureUnit.values()), null);
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
    public void needMoreDataAboutProduct(Product product, MeasureUnit unit) {
        if (product != null && unit != null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.input_amount_dialog, null);
            dialogBuilder.setView(dialogView);
            TextView nameTextView = (TextView) dialogView.findViewById(R.id.product_name_textview);
            if (nameTextView != null) {
                nameTextView.setText(product.getName());
            }
            TextView mainUnitTextView = (TextView) dialogView.findViewById(R.id.main_measure_unit_name);
            if (mainUnitTextView != null) {
                mainUnitTextView.setText(product.getMainMeasureUnit().toString());
            }
            TextView secondUnitTextView = (TextView) dialogView.findViewById(R.id.second_measure_unit_name);
            if (secondUnitTextView != null) {
                secondUnitTextView.setText(unit.toString());
            }

            final EditText amountEditText = (EditText) dialogView.findViewById(R.id.unit_amount_edit_text);

            dialogBuilder.setTitle(R.string.attention_title);
            dialogBuilder.setPositiveButton(R.string.next_default, (dialog, whichButton) -> {
                //do something with
                Double amount = Double.valueOf(amountEditText.getText().toString());
                if (amount != null && presenter != null) {
                    presenter.addNewMeasureinfo(product, unit, amount);
                    dialog.dismiss();
                }
            });
            dialogBuilder.setNegativeButton(R.string.i_dont_know, (dialog, which) -> {
                if (presenter != null) {
                    presenter.addNewMeasureinfo(product, unit, -1.);
                    dialog.dismiss();
                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();
        }
    }

    @Override
    public boolean isAddedToActivity() {
        return isAdded();
    }


    //    public interface OnListFragmentInteractionListener {
    //        void onListFragmentInteraction();
    //    }
}
