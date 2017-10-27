package com.cookplan.recipe.import_recipe.approve_result;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.Ingredient;

import java.util.ArrayList;
import java.util.List;


public class ParsedIngredientsRecyclerAdapter extends RecyclerView.Adapter<ParsedIngredientsRecyclerAdapter.ViewHolder> {

    private final List<Ingredient> ingredients;
    private Ingredient selectedIngred;
    private OnItemSelectedListener listener;

    public ParsedIngredientsRecyclerAdapter(List<Ingredient> items, OnItemSelectedListener listener) {
        ingredients = new ArrayList<>(items);
        this.listener = listener;
        if (ingredients.size() == 1) {
            selectedIngred = ingredients.get(0);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parsed_ingred_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        if (selectedIngred != null && ingredient.getName().equals(selectedIngred.getName())) {
            holder.nameTextView.setTextColor(ContextCompat.getColor(RApplication.Companion.getAppContext(),
                                                                    R.color.white));
            holder.amountTextView.setTextColor(ContextCompat.getColor(RApplication.Companion.getAppContext(),
                                                                      R.color.white));
            if (ingredient.getCategory() != null) {
                holder.mainView.setBackgroundResource(ingredient.getCategory().getColorId());
            }
        } else {
            holder.nameTextView.setTextColor(ContextCompat.getColor(RApplication.Companion.getAppContext(),
                                                                    R.color.primary_text_color));
            holder.amountTextView.setTextColor(ContextCompat.getColor(RApplication.Companion.getAppContext(),
                                                                      R.color.primary_text_color));
            holder.mainView.setBackgroundResource(R.color.white);
        }

        holder.nameTextView.setText(ingredient.getName());

        String amount = ingredient.getMainMeasureUnit().toValueString(ingredient.getMainAmount());

        if (!amount.isEmpty()) {
            holder.amountTextView.setVisibility(View.VISIBLE);

            holder.amountTextView.setText(amount);
        } else {
            holder.amountTextView.setVisibility(View.GONE);
        }

        if (ingredient.getCategory() != null) {
            holder.categoryView.setBackgroundResource(ingredient.getCategory().getColorId());
        }

        holder.mainView.setTag(position);
        holder.mainView.setOnClickListener(view -> {
            int pos = (int) view.getTag();
            selectedIngred = ingredients.get(pos);
            notifyDataSetChanged();
            if (listener != null) {
                listener.OnItemSelect(selectedIngred);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void clearSelectedItem() {
        selectedIngred = null;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView amountTextView;
        View categoryView;
        public View mainView;

        ViewHolder(View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.ingredient_item_name);
            amountTextView = (TextView) v.findViewById(R.id.ingredient_item_amount);
            categoryView = v.findViewById(R.id.category_view);
            mainView = v.findViewById(R.id.main_view);
        }
    }

    public Ingredient getSelectedIngred() {
        return selectedIngred;
    }

    public interface OnItemSelectedListener {
        void OnItemSelect(Ingredient ingredient);
    }
}
