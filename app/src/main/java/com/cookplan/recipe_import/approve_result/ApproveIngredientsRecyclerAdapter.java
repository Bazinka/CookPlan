package com.cookplan.recipe_import.approve_result;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.add_ingredient_view.ProductListAdapter;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Product;
import com.cookplan.models.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cookplan.recipe_import.approve_result.ApproveIngredientsRecyclerAdapter.ItemType.INGREDIENT;
import static com.cookplan.recipe_import.approve_result.ApproveIngredientsRecyclerAdapter.ItemType.RECIPE;


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
            String key = getItemCount() == values.size() ? values.get(position) : values.get(position - 1);
            IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;
            ingredientViewHolder.howItWasTextView.setText(key);
            if (recipeToingredientsMap.get(key).size() != 0) {
                ingredientViewHolder.parsedIngredListLayout.setVisibility(View.VISIBLE);
                ingredientViewHolder.ingredDontExistLayout.setVisibility(View.GONE);
                ingredientViewHolder.localingredRecyclerView.setHasFixedSize(true);
                ingredientViewHolder.localingredRecyclerView.setNestedScrollingEnabled(false);
                ingredientViewHolder.localingredRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                ingredientViewHolder.localingredRecyclerView.setItemAnimator(new DefaultItemAnimator());
                ParsedIngredientsRecyclerAdapter adapter = new ParsedIngredientsRecyclerAdapter(recipeToingredientsMap.get(key));
                ingredientViewHolder.localingredRecyclerView.setAdapter(adapter);

                ingredientViewHolder.saveButton.setTag(key);
                ingredientViewHolder.saveButton.setOnClickListener(v -> {
                    String tag = (String) v.getTag();
                    if (tag != null) {
                        if (listener != null) {
                            if (values.size() == 0) {
                                listener.allItemsDone();
                            } else {
                                //                                if (adapter.getSelectedIngred() != null) {
                                //                                    listener.onIngredientSaveEvent(tag, adapter.getSelectedIngred());
                                //                                }//TODO: доделать возможность сохранять новый продукт
                            }
                        }
                    }
                });

                ingredientViewHolder.productNameEditText.setAdapter(new ProductListAdapter(context, productList));
                ingredientViewHolder.productNameEditText.setOnItemClickListener((parent, view, pos, id) -> {
                    Product product = productList.get(pos);
                    if (product != null) {
                        view.setTag(product);
                    }
                });
            } else {
                ingredientViewHolder.parsedIngredListLayout.setVisibility(View.GONE);
                ingredientViewHolder.ingredDontExistLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return recipe != null && recipe.getId() == null ? values.size() + 1 : values.size();
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setIdToRecipe(String idToRecipe) {
        if (recipe != null) {
            recipe.setId(idToRecipe);
        }
        notifyDataSetChanged();
    }

    public void removeIngredientItem(String key) {
        values.remove(key);
        recipeToingredientsMap.remove(key);
        notifyDataSetChanged();

    }

    private class IngredientViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        final TextView howItWasTextView;
        final RecyclerView localingredRecyclerView;
        final ViewGroup ingredDontExistLayout;
        final ViewGroup parsedIngredListLayout;
        final AutoCompleteTextView productNameEditText;
        final Button saveButton;

        IngredientViewHolder(View view) {
            super(view);
            mainView = view.findViewById(R.id.main_view);
            howItWasTextView = (TextView) view.findViewById(R.id.approve_ingred_how_it_was);
            localingredRecyclerView = (RecyclerView) view.findViewById(R.id.local_ingredients_recycler_view);
            ingredDontExistLayout = (ViewGroup) view.findViewById(R.id.ingred_dont_exist_layout);
            parsedIngredListLayout = (ViewGroup) view.findViewById(R.id.parsed_list_layout);
            productNameEditText = (AutoCompleteTextView) view.findViewById(R.id.product_name_text);
            saveButton = (Button) view.findViewById(R.id.approve_button);
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

        void onIngredientSaveEvent(Product product);

        void onIngredientSaveEvent(String key, Ingredient ingredient);
    }
}