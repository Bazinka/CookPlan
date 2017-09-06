package com.cookplan.shopping_list.add_from_notify;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShopListStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToShopListRecipesRecyclerAdapter extends RecyclerView.Adapter<ToShopListRecipesRecyclerAdapter.ViewHolder> {

    private final Map<Recipe, List<Ingredient>> map;
    private final List<Recipe> values;
    private Context context;

    public ToShopListRecipesRecyclerAdapter(Context context) {
        this.context = context;
        map = new HashMap<>();
        values = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.to_shop_list_recipe_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Recipe recipe = values.get(position);

        ToShopListIngredientsRecyclerAdapter adapter = new ToShopListIngredientsRecyclerAdapter(map.get(recipe));

        holder.ingredientsRecyclerView.setHasFixedSize(true);
        holder.ingredientsRecyclerView.setNestedScrollingEnabled(false);
        holder.ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.ingredientsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.ingredientsRecyclerView.setAdapter(adapter);

        holder.recipeNameTextView.setText(recipe.getName());
        holder.addAllSwitchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buttonView.setText(R.string.remove_all_products_title);
            } else {
                buttonView.setText(R.string.add_all_products_title);
            }
            adapter.updateItems(isChecked);
            notifyDataSetChanged();
        });
    }


    @Override
    public int getItemCount() {
        return values.size();
    }

    public void addItem(Recipe recipe, List<Ingredient> ingredientList) {
        if (ingredientList != null && !ingredientList.isEmpty()) {
            if (recipe == null) {
                recipe = new Recipe(context.getString(R.string.without_recipe_title),
                                    context.getString(R.string.recipe_desc_is_not_needed_title), null);
            }
            map.put(recipe, ingredientList);
            values.add(recipe);
            notifyDataSetChanged();
        }
    }

    public List<Ingredient> getSelectedIngredList() {
        List<Ingredient> selectedIngredList = new ArrayList<>();
        for (Recipe recipe : values) {
            for (Ingredient ingredient : map.get(recipe)) {
                if (ingredient.getShopListStatus() == ShopListStatus.NEED_TO_BUY) {
                    selectedIngredList.add(ingredient);
                }
            }
        }
        return selectedIngredList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        final TextView recipeNameTextView;
        final RecyclerView ingredientsRecyclerView;
        final SwitchCompat addAllSwitchCompat;


        ViewHolder(View view) {
            super(view);
            mainView = view.findViewById(R.id.main_view);
            addAllSwitchCompat = (SwitchCompat) view.findViewById(R.id.add_all_switch_compat);
            recipeNameTextView = (TextView) view.findViewById(R.id.recipe_name_textview);
            ingredientsRecyclerView = (RecyclerView) view.findViewById(R.id.ingredients_to_shop_list_recycler_view);
        }
    }
}
