package com.cookplan.add_ingredient_view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.cookplan.R
import com.cookplan.models.MeasureUnit
import com.cookplan.models.Product
import com.cookplan.models.ProductCategory
import com.cookplan.models.Recipe


//    private OnListFragmentInteractionListener mListener;

class AddIngredientViewFragment : Fragment(), AddIngredientView {

    private var presenter: AddIngredientPresenter? = null

    private var unitAmountViewGroup: ViewGroup? = null
    private var progressBar: ProgressBar? = null
    private var mainView: View? = null

    override val isAddedToActivity: Boolean
        get() = isAdded

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AddIngredientPresenterImpl(this)
        if (arguments?.containsKey(RECIPE_OBJECT_KEY) ?: false) {
            val recipe = arguments?.getSerializable(RECIPE_OBJECT_KEY) as Recipe
            presenter?.setRecipe(recipe)
        }
        presenter?.isNeedToBuy = arguments?.getBoolean(RECIPE_NEED_TO_BUY_KEY, false) ?: false
    }

    override fun onStart() {
        super.onStart()
        presenter?.getAsyncProductList()
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.fragment_add_ingredient_view, container, false)

        unitAmountViewGroup = mainView?.findViewById<ViewGroup>(R.id.unit_amount_main_layout)

        progressBar = mainView?.findViewById<ProgressBar>(R.id.progress_bar)

        val unitNameEditText = mainView?.findViewById<AutoCompleteTextView>(R.id.product_name_text)
        unitNameEditText?.requestFocus()
        unitNameEditText?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                unitAmountViewGroup?.visibility = if (count == 0) View.GONE else View.VISIBLE
            }
        })

        val saveProductButton = mainView?.findViewById<ImageButton>(R.id.save_product_image_button)
        saveProductButton?.setOnClickListener { view ->
            val unitAmoutEditText = mainView?.findViewById<EditText>(R.id.unit_amount_edit_text)
            val name = unitNameEditText?.text.toString()
            if (!name.isEmpty()) {
                val text = unitAmoutEditText?.text.toString()
                if (!text.isEmpty()) {
                    saveInrgedient(text.toDouble())
                } else {
                    AlertDialog.Builder(activity as Context, R.style.AppCompatAlertDialogStyle).setTitle(R.string.attention_title)
                            .setMessage(R.string.product_amount_not_exist)
                            .setPositiveButton(android.R.string.ok) { dialog, which -> saveInrgedient(0.toDouble()) }
                            .setNegativeButton(android.R.string.cancel, null)
                            .show()
                }
            }
        }
        setCategorySpinnerValues()
        setMeasureSpinnerValues()
        return mainView
    }

    internal fun saveInrgedient(amount: Double) {
        progressBar?.visibility = View.VISIBLE
        val measureSpinner = mainView?.findViewById<Spinner>(R.id.measure_list_spinner)
        val categorySpinner = mainView?.findViewById<Spinner>(R.id.category_list_spinner)
        val unitNameEditText = mainView?.findViewById<AutoCompleteTextView>(R.id.product_name_text)
        val name = unitNameEditText?.text.toString()

        val selectedProduct = unitNameEditText?.tag as Product?
        if (selectedProduct != null && name == selectedProduct.toStringName()) {
            presenter?.saveIngredient(selectedProduct, amount, measureSpinner?.selectedItem as MeasureUnit)
        } else {
            presenter?.saveProductAndIngredient(categorySpinner?.selectedItem as ProductCategory,
                    name, amount, measureSpinner?.selectedItem as MeasureUnit)
        }
        unitNameEditText?.setText(null)
        unitNameEditText?.requestFocus()
        val unitAmoutEditText = mainView?.findViewById<EditText>(R.id.unit_amount_edit_text)
        unitAmoutEditText?.setText(null)
    }

    override fun setErrorToast(error: String) {
        progressBar?.visibility = View.GONE
        Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
    }

    override fun setErrorToast(errorId: Int) {
        progressBar?.visibility = View.GONE
        Toast.makeText(activity, getString(errorId), Toast.LENGTH_LONG).show()
    }

    override fun setProductsList(productsList: List<Product>) {
        val unitNameEditText = mainView?.findViewById<AutoCompleteTextView>(R.id.product_name_text)
        val adapter = ProductListAdapter(activity as Context, productsList)
        unitNameEditText?.setAdapter(adapter)
        unitNameEditText?.setOnItemClickListener { parent, arg1, pos, id ->
            unitNameEditText.tag = productsList[pos]
            val unitAmountEditText = mainView?.findViewById<EditText>(R.id.unit_amount_edit_text)
            unitAmountEditText?.requestFocus()
            setMeasureSpinnerValues()
            setCategorySpinnerValues()
        }

        unitNameEditText?.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val name = unitNameEditText.text.toString()
                val selectedProduct = unitNameEditText.tag as Product?
                if (selectedProduct == null || name != selectedProduct.toStringName()) {
                    adapter.filter.filter(name) { count ->
                        if (count == 1) {
                            unitNameEditText.tag = adapter.getItem(0)
                        }
                        setMeasureSpinnerValues()
                        setCategorySpinnerValues()
                        adapter.filter.filter("")
                    }
                } else {
                    setMeasureSpinnerValues()
                    setCategorySpinnerValues()
                }
            }
        }

        progressBar?.visibility = View.GONE

        mainView?.findViewById<View>(R.id.main_view)?.visibility = View.VISIBLE
    }


    internal fun setCategorySpinnerValues() {
        val unitNameEditText = mainView?.findViewById<AutoCompleteTextView>(R.id.product_name_text)
        val name = unitNameEditText?.text.toString()
        val spinner = mainView?.findViewById<Spinner>(R.id.category_list_spinner)
        val product = unitNameEditText?.tag as Product?

        if (activity != null) {
            val adapter = ProductCategoriesSpinnerAdapter(activity as Context,
                    if (name == product?.toStringName()) mutableListOf<ProductCategory>(product.category)
                    else ProductCategory.values().toMutableList())
            spinner?.adapter = adapter
            spinner?.setSelection(0)
        }
    }

    internal fun setMeasureSpinnerValues() {
        val unitNameEditText = mainView?.findViewById<AutoCompleteTextView>(R.id.product_name_text)
        val name = unitNameEditText?.text.toString()
        val spinner = mainView?.findViewById<Spinner>(R.id.measure_list_spinner)
        val selectedProduct = unitNameEditText?.tag as Product?
        var mainMeasureUnits = mutableListOf<MeasureUnit>()
        val measureUnits = mutableListOf<MeasureUnit>()
        if (selectedProduct != null && name == selectedProduct.toStringName()) {
            measureUnits.addAll(selectedProduct.mainMeasureUnitList)
            for (unit in selectedProduct.measureUnitList) {
                var isMainUnit = false
                for (existUnit in selectedProduct.mainMeasureUnitList) {
                    if (unit === existUnit) {
                        isMainUnit = true
                    }
                }
                if (!isMainUnit) {
                    measureUnits.add(unit)
                }
            }
            mainMeasureUnits = selectedProduct.mainMeasureUnitList
        } else {
            measureUnits.addAll(MeasureUnit.values().toMutableList())
        }
        if (activity != null) {
            val adapter = MeasureUnitsSpinnerAdapter(activity as Context,
                    measureUnits,
                    mainMeasureUnits)
            spinner?.adapter = adapter
            spinner?.setSelection(0)
        }
    }

    override fun setSuccessSaveIngredient() {
        progressBar?.visibility = View.GONE
        unitAmountViewGroup?.visibility = View.GONE
    }

    companion object {

        val RECIPE_OBJECT_KEY = "new_recipe_name"
        val RECIPE_NEED_TO_BUY_KEY = "is_need_to_buy"

        fun newInstance(isNeedToBuy: Boolean): AddIngredientViewFragment {
            val fragment = AddIngredientViewFragment()
            val args = Bundle()
            args.putBoolean(RECIPE_NEED_TO_BUY_KEY, isNeedToBuy)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(recipe: Recipe?, isNeedToBuy: Boolean): AddIngredientViewFragment {
            val fragment = AddIngredientViewFragment()
            val args = Bundle()
            if (recipe != null) {
                args.putSerializable(RECIPE_OBJECT_KEY, recipe)
            }
            args.putBoolean(RECIPE_NEED_TO_BUY_KEY, isNeedToBuy)
            fragment.arguments = args
            return fragment
        }
    }
}
