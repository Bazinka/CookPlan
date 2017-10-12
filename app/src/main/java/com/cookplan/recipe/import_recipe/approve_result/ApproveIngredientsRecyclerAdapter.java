package com.cookplan.recipe.import_recipe.approve_result;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cookplan.R;
import com.cookplan.add_ingredient_view.ProductCategoriesSpinnerAdapter;
import com.cookplan.add_ingredient_view.ProductListAdapter;
import com.cookplan.models.Ingredient;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.models.ProductCategory;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShopListStatus;
import com.cookplan.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.cookplan.recipe.import_recipe.approve_result.ApproveIngredientsRecyclerAdapter.ItemType.INGREDIENT;
import static com.cookplan.recipe.import_recipe.approve_result.ApproveIngredientsRecyclerAdapter.ItemType.RECIPE;


/**
 * Created by DariaEfimova on 09.06.17.
 */

public class ApproveIngredientsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> values;
    private Map<String, List<Ingredient>> recipeToingredientsMap;
    private List<Product> productList;
    private Recipe recipe;
    private ApproveIngredientsEventListener listener;
    private Context context;

    public ApproveIngredientsRecyclerAdapter(Recipe recipe,
                                             Map<String, List<Ingredient>> recipeToingredientsMap,
                                             List<Product> productList,
                                             ApproveIngredientsEventListener listener,
                                             Context context) {
        this.values = new ArrayList<>(recipeToingredientsMap.keySet());
        this.recipeToingredientsMap = recipeToingredientsMap;
        this.productList = productList != null ? productList : new ArrayList<>();
        this.recipe = recipe;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && recipe != null && recipe.getId() == null) {
            return RECIPE.getId();
        } else {
            return INGREDIENT.getId();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == RECIPE.getId()) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.approve_recipe_info_item_layout, parent, false);
            return new RecipeViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.approve_ingredients_item_layout, parent, false);
            return new IngredientViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == RECIPE.getId()) {
            RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;
            if (recipe != null) {
                recipeViewHolder.nameTextView.setText(recipe.getName());
                recipeViewHolder.descTextView.setText(recipe.getDesc());
                recipeViewHolder.saveButton.setOnClickListener(view -> {
                    if (listener != null) {
                        listener.onRecipeSaveEvent(recipe);
                    }
                });
            }
        }
        if (getItemViewType(position) == INGREDIENT.getId()) {
            IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;
            String key = getItemCount() == values.size() ? values.get(position) : values.get(position - 1);
            fillIngredientItemView(key, ingredientViewHolder);
        }
    }

    @Override
    public int getItemCount() {
        return recipe != null && recipe.getId() == null ? values.size() + 1 : values.size();
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void updateRecipe(Recipe newRecipe) {
        recipe = newRecipe;
        notifyDataSetChanged();
    }

    public void removeIngredientItem(String key) {
        values.remove(key);
        recipeToingredientsMap.remove(key);
        if (values.size() == 0 && recipe.getId() != null) {
            listener.allItemsDone();
        } else {
            notifyDataSetChanged();
        }
    }

    private void fillIngredientItemView(String key, IngredientViewHolder ingredientViewHolder) {
        ingredientViewHolder.howItWasTextView.setText(key);

        ParsedIngredientsRecyclerAdapter adapter = new ParsedIngredientsRecyclerAdapter(
                recipeToingredientsMap.get(key),
                ingredient -> {
                    ingredientViewHolder.productNameEditText.setTag(null);
                    ingredientViewHolder.productNameEditText.setText(null);
                    setCategorySpinnerValues(null, ingredientViewHolder);
                });

        if (recipeToingredientsMap.get(key).size() != 0) {
            ingredientViewHolder.parsedIngredListLayout.setVisibility(View.VISIBLE);
            ingredientViewHolder.localingredRecyclerView.setHasFixedSize(true);
            ingredientViewHolder.localingredRecyclerView.setNestedScrollingEnabled(false);
            ingredientViewHolder.localingredRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            ingredientViewHolder.localingredRecyclerView.setItemAnimator(new DefaultItemAnimator());
            ingredientViewHolder.localingredRecyclerView.setAdapter(adapter);
        } else {
            ingredientViewHolder.parsedIngredListLayout.setVisibility(View.GONE);
        }
        ingredientViewHolder.saveButton.setTag(key);
        ingredientViewHolder.saveButton.setOnClickListener(v -> {
            if (recipe.getId() != null) {
                String tag = (String) v.getTag();
                if (tag != null) {
                    if (listener != null) {
                        if (adapter.getSelectedIngred() != null) {
                            Ingredient ingredient = adapter.getSelectedIngred();
                            ingredient.setRecipeId(recipe.getId());
                            listener.onIngredientSaveEvent(tag, ingredient);
                        } else {
                            String productName = ingredientViewHolder.productNameEditText.getText().toString();
                            if (!productName.isEmpty()) {
                                double amount = getAmountFromString(tag);
                                MeasureUnit unit = getMeasureUnitFromString(tag);
                                Product product = (Product) ingredientViewHolder.productNameEditText.getTag();
                                if (product == null || !productName.equals(product.toStringName())) {
                                    listener.onIngredientSaveEvent(
                                            tag,
                                            (ProductCategory) ingredientViewHolder.productCategorySpinner.getSelectedItem(),
                                            productName, amount, unit);
                                } else {
                                    Ingredient ingredient = new Ingredient(null, product.toStringName(),
                                                                           product, recipe.getId(),
                                                                           unit, amount, ShopListStatus.NONE);
                                    listener.onIngredientSaveEvent(tag, ingredient);
                                }
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Сначала подтвердите название и описание рецепта", Toast.LENGTH_LONG).show();
            }
        });

        ingredientViewHolder.productNameEditText.setAdapter(new ProductListAdapter(context, productList));
        ingredientViewHolder.productNameEditText.setOnItemClickListener((parent, view, pos, id) -> {
            Product product = productList.get(pos);
            if (product != null) {
                ingredientViewHolder.productNameEditText.setTag(product);
                setCategorySpinnerValues(product, ingredientViewHolder);
            }
        });
        ingredientViewHolder.productNameEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (count > 0) {
                    adapter.clearSelectedItem();
                }
            }
        });

        setCategorySpinnerValues(null, ingredientViewHolder);

        ingredientViewHolder.closeButton.setTag(key);
        ingredientViewHolder.closeButton.setOnClickListener(v -> {
            String tag = (String) v.getTag();
            if (tag != null) {
                removeIngredientItem(tag);
            }
        });
    }

    private MeasureUnit getMeasureUnitFromString(String tag) {
        String[] splits = tag.split("\\d+");
        return MeasureUnit.parseUnit(splits[splits.length - 1]);
    }

    private double getAmountFromString(String tag) {
        double amount = 0;
        String[] splits = tag.split(":");
        if (splits.length == 2) {
            String[] splitsSpace = splits[1].split("\\s+");
            amount = Utils.getDoubleFromString(splitsSpace[1]);
        }
        return amount;
    }

    private void setCategorySpinnerValues(Product selectedProduct, IngredientViewHolder holder) {
        List<ProductCategory> categoryList = new ArrayList<>();
        if (selectedProduct != null) {
            categoryList.add(selectedProduct.getCategory());
        } else {
            categoryList.addAll(Arrays.asList(ProductCategory.values()));
        }
        ProductCategoriesSpinnerAdapter adapter = new ProductCategoriesSpinnerAdapter(context, categoryList);
        holder.productCategorySpinner.setAdapter(adapter);
        holder.productCategorySpinner.setSelection(0);
    }

    private class IngredientViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        final TextView howItWasTextView;
        final RecyclerView localingredRecyclerView;
        final ViewGroup parsedIngredListLayout;
        final AutoCompleteTextView productNameEditText;
        final Spinner productCategorySpinner;
        final Button saveButton;
        final ImageView closeButton;

        IngredientViewHolder(View view) {
            super(view);
            mainView = view.findViewById(R.id.main_view);
            howItWasTextView = (TextView) view.findViewById(R.id.approve_ingred_how_it_was);
            localingredRecyclerView = (RecyclerView) view.findViewById(R.id.local_ingredients_recycler_view);
            parsedIngredListLayout = (ViewGroup) view.findViewById(R.id.parsed_list_layout);
            productNameEditText = (AutoCompleteTextView) view.findViewById(R.id.product_name_text);
            productCategorySpinner = (Spinner) view.findViewById(R.id.category_list_spinner);

            saveButton = (Button) view.findViewById(R.id.approve_button);
            closeButton = (ImageView) view.findViewById(R.id.close_btn);
        }
    }

    private class RecipeViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        final TextView nameTextView;
        final TextView descTextView;
        final Button saveButton;

        RecipeViewHolder(View view) {
            super(view);
            mainView = view.findViewById(R.id.main_view);
            nameTextView = (TextView) view.findViewById(R.id.approve_recipe_name);
            descTextView = (TextView) view.findViewById(R.id.approve_recipe_desc);
            saveButton = (Button) view.findViewById(R.id.approve_button);
        }
    }

    protected enum ItemType {
        RECIPE(0),
        INGREDIENT(1);

        private int id;

        ItemType(int id) {
            this.id = id;
        }

        protected int getId() {
            return id;
        }
    }

    public interface ApproveIngredientsEventListener {
        void allItemsDone();

        void onRecipeSaveEvent(Recipe recipe);

        void onIngredientSaveEvent(String key, ProductCategory category, String name, double amount, MeasureUnit measureUnit);

        void onIngredientSaveEvent(String key, Ingredient ingredient);
    }
}