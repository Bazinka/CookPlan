package com.cookplan.shopping_list.list_by_dishes;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShopListStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShopListExpandableListAdapter extends BaseExpandableListAdapter {

    private static final String WITHOUT_RECIPE_KEY = "without_recipe";
    private Context context;
    private Map<String, List<Ingredient>> recipeIdsToIngredientMap;
    private List<Recipe> recipeList;
    private OnItemClickListener listener;

    public ShopListExpandableListAdapter(Context context,
                                         Map<Recipe, List<Ingredient>> recipeToIngredientMap,
                                         OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        recipeList = new ArrayList<>();
        recipeIdsToIngredientMap = new HashMap<>();

        for (Map.Entry<Recipe, List<Ingredient>> entry : recipeToIngredientMap.entrySet()) {
            if (entry.getValue() != null) {
                if (entry.getKey() == null || entry.getKey().getId() == null) {
                    recipeList.add(new Recipe(context.getString(R.string.without_recipe_title),
                                              context.getString(R.string.recipe_desc_is_not_needed_title)));
                    recipeIdsToIngredientMap.put(WITHOUT_RECIPE_KEY, entry.getValue());

                } else {
                    recipeList.add(entry.getKey());
                    recipeIdsToIngredientMap.put(entry.getKey().getId(), entry.getValue());
                }
            }
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        String recipeId;
        if (recipeList.get(groupPosition).getId() == null) {
            recipeId = WITHOUT_RECIPE_KEY;
        } else {
            recipeId = recipeList.get(groupPosition).getId();
        }
        return recipeIdsToIngredientMap.get(recipeId).get(childPosititon);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String recipeId;
        if (recipeList.get(groupPosition).getId() == null) {
            recipeId = WITHOUT_RECIPE_KEY;
        } else {
            recipeId = recipeList.get(groupPosition).getId();
        }
        return recipeIdsToIngredientMap.get(recipeId).size();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.shop_list_by_dish_item_layout, null);
        }
        Ingredient ingredient = (Ingredient) getChild(groupPosition, childPosition);

        if (ingredient != null) {
            TextView nameTextView = (TextView) convertView.findViewById(R.id.ingredient_item_name);
            TextView amountTextView = (TextView) convertView.findViewById(R.id.ingredient_item_amount);
            View mainView = convertView.findViewById(R.id.main_view);

            if (ingredient.getShopListStatus() == ShopListStatus.ALREADY_BOUGHT) {
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
            nameTextView.setText(ingredient.getName());

            if (ingredient.getUserName() != null && !ingredient.getUserName().isEmpty()) {
                nameTextView.setText(nameTextView.getText().toString() +
                                             " (by " + " " + ingredient.getUserName() + ")");
            }

            if (ingredient.getMainAmount() != 0 && ingredient.getMainMeasureUnit() != null) {
                amountTextView.setVisibility(View.VISIBLE);
                String amount = ingredient.getMainMeasureUnit().toValueString(ingredient.getMainAmount());
                amountTextView.setText(amount);
            } else {
                amountTextView.setVisibility(View.GONE);
            }

            View.OnClickListener clickListener = view -> {
                Ingredient selectIngredient = (Ingredient) view.getTag();

                if (listener != null) {
                    listener.onChildClick(selectIngredient);
                }
            };
            mainView.setTag(ingredient);
            mainView.setOnClickListener(clickListener);
        }
        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return recipeList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return recipeList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Recipe recipe = (Recipe) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.shop_list_by_dish_group_item_layout, null);
        }
        TextView headerTextView
                = (TextView) convertView
                .findViewById(R.id.recipe_item_name);
        headerTextView.setText(recipe.getName());

        View.OnClickListener clickListener = view -> {
            Recipe selectedRecipe = (Recipe) view.getTag();
            List<Ingredient> ingredients;
            if (selectedRecipe.getId() == null) {
                ingredients = recipeIdsToIngredientMap.get(WITHOUT_RECIPE_KEY);
            } else {
                ingredients = recipeIdsToIngredientMap.get(selectedRecipe.getId());
            }
            if (listener != null) {
                listener.onGroupClick(selectedRecipe, ingredients);
            }
        };

        View mainView = convertView.findViewById(R.id.main_view);
        mainView.setTag(recipe);
        mainView.setOnClickListener(clickListener);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public interface OnItemClickListener {
        public void onChildClick(Ingredient ingredient);

        public void onGroupClick(Recipe recipe, List<Ingredient> ingredientList);
    }
}
