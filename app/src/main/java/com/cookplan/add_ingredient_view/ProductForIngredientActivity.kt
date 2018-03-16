package com.cookplan.add_ingredient_view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.RApplication
import com.cookplan.models.MeasureUnit
import com.cookplan.models.MeasureUnit.UNITS
import com.cookplan.models.MeasureUnit.values
import com.cookplan.models.Product
import com.cookplan.models.ProductCategory
import com.cookplan.utils.MeasureUnitUtils

class ProductForIngredientActivity : BaseActivity(), ProductForIngredientView {

    private var presenter: ProductForIngredientPresenter? = null
    private var adapter: ProductForIngredientListAdapter? = null

    private var selectedProduct: Product? = null

    private var parsedAmount: Double? = null

    private var parsedMeasure: MeasureUnit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_product_for_ingredient)
        setNavigationArrow()
        setTitle(getString(R.string.product_for_ingredient_title))

        if (intent.hasExtra(RECIPE_ID_KEY)) {
            val recipe = intent.getStringExtra(RECIPE_ID_KEY)
            presenter = ProductForIngredientPresenterImpl(this)
            presenter?.setRecipeId(recipe)
            presenter?.isNeedToBuy = intent.getBooleanExtra(RECIPE_NEED_TO_BUY_KEY, false)

            val unitNameTextLayout = findViewById<ViewGroup>(R.id.product_name_text_layout)
            unitNameTextLayout.setOnClickListener {
                selectedProduct = null
                findViewById<TextView>(R.id.product_name_text).text = getString(R.string.enter_product_title)

                setMainChooseViewVisability(VISIBLE)
                parsedMeasure = null
                parsedAmount = null
                val unitNameChooseEditText = findViewById<EditText>(R.id.product_choosing_name_text)
                unitNameChooseEditText.requestFocus()
            }

            setCategorySpinnerValues()
            setMeasureSpinnerValues()

            // Disable button if no recognition service is present
            val speakButton = findViewById<ImageButton>(R.id.speak_image_view)
            speakButton?.isEnabled = packageManager?.queryIntentActivities(
                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)?.size != 0
            speakButton?.setOnClickListener {
                runSpeakAction()
            }
            val speakChoosingButton = findViewById<ImageButton>(R.id.speak_choosing_image_view)
            speakChoosingButton?.isEnabled = packageManager?.queryIntentActivities(
                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)?.size != 0
            speakChoosingButton?.setOnClickListener {
                runSpeakAction()
            }

            val unitAmountEditText = findViewById<EditText>(R.id.unit_amount_edit_text)
            unitAmountEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkFieldsToSaveIngredient()
                }
                false
            }

        } else {
            finish()
        }
    }

    private fun runSpeakAction() {
        if (!RApplication.Companion.isUserSawVoiceAlert()) {
            RApplication.Companion.saveUserSawVoiceAlert(true)
            android.app.AlertDialog.Builder(this)
                    .setMessage(getString(R.string.format_voise_recognition_message))
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        runVoiceActivity()
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
        } else {
            runVoiceActivity()
        }
    }

    private fun runVoiceActivity() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voise_recognition_example))
        startActivityForResult(intent, SPEAK_VOICE_REQUEST_CODE)
    }

    override fun onStart() {
        super.onStart()
        showProgressBar()
        presenter?.getAsyncProductList()
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    private fun showProgressBar() {
        findViewById<ViewGroup>(R.id.progress_bar_layout)?.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        findViewById<ViewGroup>(R.id.progress_bar_layout)?.visibility = View.GONE
    }

    private fun setMainChooseViewVisability(visibility: Int) {
        findViewById<ViewGroup>(R.id.main_choose_view).visibility = visibility
        if (visibility == VISIBLE) {
            findViewById<View>(R.id.product_choosing_name_text).requestFocus()
        }
    }

    override fun setErrorToast(error: String) {
        hideProgressBar()
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    override fun setErrorToast(errorId: Int) {
        hideProgressBar()
        Toast.makeText(this, getString(errorId), Toast.LENGTH_LONG).show()
    }

    override fun setProductsList(productsList: List<Product>) {
        hideProgressBar()

        val unitNameChooseEditText = findViewById<EditText>(R.id.product_choosing_name_text)
        unitNameChooseEditText.requestFocus()
        unitNameChooseEditText?.setPrivateImeOptions("nm,com.google.android.inputmethod.latin.noMicrophoneKey");
        unitNameChooseEditText?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                showProgressBar()
                adapter?.updateItems(presenter?.filterProducts(s) ?: listOf())
                hideProgressBar()
            }
        })
        unitNameChooseEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                setMainChooseViewVisability(GONE)
                val name = unitNameChooseEditText.text.toString()
                if (!name.isEmpty()) {
                    selectedProduct = null
                    findViewById<TextView>(R.id.product_name_text).text = name
                }
                if (parsedMeasure == null && parsedAmount == null) {
                    setMeasureSpinnerValues()
                    findViewById<EditText>(R.id.unit_amount_edit_text).text = null
                }
                setCategorySpinnerValues()
                true
            }
            false
        }

        val recyclerView = findViewById<RecyclerView>(R.id.products_list_recycler)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        adapter = ProductForIngredientListAdapter(productsList.toMutableList()) { product ->
            setMainChooseViewVisability(GONE)
            selectedProduct = product
            unitNameChooseEditText.text = null
            findViewById<TextView>(R.id.product_name_text).text = product.toStringName()
            /*
                if we choose a product after parsing voice we don't need to reload measure spinner values
                because it's already loaded
            */
            if (parsedMeasure == null && parsedAmount == null) {
                setMeasureSpinnerValues()
                findViewById<EditText>(R.id.unit_amount_edit_text).text = null
            }
            setCategorySpinnerValues()
        }
        recyclerView?.adapter = adapter
        hideProgressBar()
    }


    private fun setCategorySpinnerValues() {
        val name = findViewById<TextView>(R.id.product_name_text).text.toString()
        val spinner = findViewById<Spinner>(R.id.category_list_spinner)

        val adapter = ProductCategoriesSpinnerAdapter(this,
                if (name == selectedProduct?.toStringName()) mutableListOf<ProductCategory>(selectedProduct?.category ?: ProductCategory.WITHOUT_CATEGORY)
                else ProductCategory.values().toMutableList())
        spinner?.adapter = adapter
        spinner?.setSelection(0)
    }

    private fun setMeasureSpinnerValues() {
        val name = findViewById<TextView>(R.id.product_name_text).text.toString()
        val spinner = findViewById<Spinner>(R.id.measure_list_spinner)

        var mainMeasureUnits = listOf<MeasureUnit>()
        val measureUnits = mutableListOf<MeasureUnit>()
        if (name == selectedProduct?.toStringName()) {
            measureUnits.addAll(selectedProduct?.mainMeasureUnitList ?: listOf<MeasureUnit>())
            for (unit in selectedProduct?.measureUnitList ?: listOf(UNITS)) {
                val isMainUnit = (selectedProduct?.mainMeasureUnitList ?: listOf<MeasureUnit>()).any { unit === it }
                if (!isMainUnit) {
                    measureUnits.add(unit)
                }
            }
            mainMeasureUnits = selectedProduct?.mainMeasureUnitList ?: listOf<MeasureUnit>()
        } else {
            measureUnits.addAll(values().toMutableList())
        }
        val adapter = MeasureUnitsSpinnerAdapter(this,
                measureUnits,
                mainMeasureUnits)
        spinner?.adapter = adapter
        spinner?.setSelection(0)
    }

    override fun setSuccessSaveIngredient() {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SPEAK_VOICE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val matches = data?.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS) ?: listOf<String>()
            var ingredientString = String()
            matches
                    .filter { presenter?.isIngredientString(it) == true }
                    .forEach { ingredientString = it }
            if (!ingredientString.isEmpty()) {

                setErrorToast(ingredientString)//TODO: убрать

                val productName = presenter?.parseProductNameFromString(ingredientString) ?: String()
                val unitNameChoosingEditText = findViewById<EditText>(R.id.product_choosing_name_text)

                val products = presenter?.filterProducts(productName) ?: listOf()
                when {
                    products.size == 1 -> {
                        selectedProduct = products[0]
                        unitNameChoosingEditText.text = null
                        findViewById<TextView>(R.id.product_name_text).text = selectedProduct?.toStringName()
                        setMainChooseViewVisability(GONE)
                    }
                    products.isEmpty() -> {
                        selectedProduct = null
                        unitNameChoosingEditText.text = null
                        findViewById<TextView>(R.id.product_name_text).text = productName
                        setMainChooseViewVisability(GONE)
                    }
                    else -> {
                        selectedProduct = null
                        findViewById<TextView>(R.id.product_name_text).text = productName
                        unitNameChoosingEditText.setText(productName)
                        setMainChooseViewVisability(VISIBLE)
                    }
                }

                setMeasureSpinnerValues()
                setCategorySpinnerValues()

                val measureSpinner = findViewById<Spinner>(R.id.measure_list_spinner)
                val unitAmountEditText = findViewById<EditText>(R.id.unit_amount_edit_text)

                parsedMeasure = presenter?.parseMeasureUnitFromString(ingredientString)
                var position = 0
                while (position < measureSpinner.count) {
                    val measureUnitAtPos = measureSpinner.getItemAtPosition(position) as MeasureUnit
                    if (measureUnitAtPos == parsedMeasure) {
                        break
                    } else {
                        position++
                    }
                }
                if (position < measureSpinner.count) {
                    measureSpinner.setSelection(position)
                } else {
                    setErrorToast(getString(R.string.unexisting_measure_for_product_title))
                }

                parsedAmount = presenter?.parseAmountFromString(ingredientString)
                unitAmountEditText.setText(MeasureUnitUtils.getStringOfValueInUnit(
                        parsedMeasure ?: UNITS,
                        parsedAmount ?: 0.toDouble()) ?: String())

            } else {
                setErrorToast(getString(R.string.voise_recognition_error))
            }
        }
    }

    override fun onCreateOptionsMenu(_menu: Menu): Boolean {
        menuInflater.inflate(R.menu.done_menu, _menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.app_bar_done) {
            checkFieldsToSaveIngredient()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkFieldsToSaveIngredient() {
        val name = findViewById<TextView>(R.id.product_name_text).text.toString()
        if (!name.isEmpty() || name == getString(R.string.enter_product_title)) {
            val text = findViewById<EditText>(R.id.unit_amount_edit_text).text.toString()
            if (!text.isEmpty()) {
                saveInrgedient(text.toDouble())
            } else {
                AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle).setTitle(R.string.attention_title)
                        .setMessage(R.string.product_amount_not_exist)
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            saveInrgedient(0.toDouble())
                        }
                        .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
            }
        }
    }

    private fun saveInrgedient(amount: Double) {
        showProgressBar()
        val measureSpinner = findViewById<Spinner>(R.id.measure_list_spinner)
        val categorySpinner = findViewById<Spinner>(R.id.category_list_spinner)

        val name = findViewById<TextView>(R.id.product_name_text).text.toString()

        if (selectedProduct != null) {
            presenter?.saveIngredient(selectedProduct, amount, measureSpinner?.selectedItem as MeasureUnit)
        } else {
            presenter?.saveProductAndIngredient(categorySpinner?.selectedItem as ProductCategory,
                    name, amount, measureSpinner?.selectedItem as MeasureUnit)
        }
    }

    companion object {

        val SPEAK_VOICE_REQUEST_CODE = 22

        val RECIPE_ID_KEY = "RECIPE_ID_KEY"
        val RECIPE_NEED_TO_BUY_KEY = "RECIPE_NEED_TO_BUY_KEY"
    }
}
