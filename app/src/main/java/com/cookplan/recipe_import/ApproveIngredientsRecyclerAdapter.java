package com.cookplan.recipe_import;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DariaEfimova on 09.06.17.
 */

public class ApproveIngredientsRecyclerAdapter extends RecyclerView.Adapter<ApproveIngredientsRecyclerAdapter.ViewHolder> {
    private List<String> values;
    private Map<String, List<Ingredient>> recipeToingredientsMap;
    private ApproveIngredientsEventListener listener;
    private Context context;

    public ApproveIngredientsRecyclerAdapter(Map<String, List<Ingredient>> recipeToingredientsMap,
                                             ApproveIngredientsEventListener listener, Context context) {
        this.values = new ArrayList<>(recipeToingredientsMap.keySet());
        this.recipeToingredientsMap = recipeToingredientsMap;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ApproveIngredientsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.approve_ingredients_item_layout, parent, false);
        return new ApproveIngredientsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ApproveIngredientsRecyclerAdapter.ViewHolder holder, int position) {
        String key = values.get(position);
        holder.howItWasTextView.setText(key);
        if (recipeToingredientsMap.get(key).size() != 0) {
            holder.parsedIngredListLayout.setVisibility(View.VISIBLE);
            holder.ingredDontExistLayout.setVisibility(View.GONE);
            holder.localingredRecyclerView.setHasFixedSize(true);
            holder.localingredRecyclerView.setNestedScrollingEnabled(false);
            holder.localingredRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.localingredRecyclerView.setItemAnimator(new DefaultItemAnimator());
            ParsedIngredientsRecyclerAdapter adapter = new ParsedIngredientsRecyclerAdapter(recipeToingredientsMap.get(key));
            holder.localingredRecyclerView.setAdapter(adapter);

            holder.saveButton.setTag(key);
            holder.saveButton.setOnClickListener(v -> {
                if (adapter.getSelectedIngred() != null) {
                    String tag = (String) v.getTag();
                    if (tag != null) {
                        values.remove(key);
                        recipeToingredientsMap.remove(key);
                        notifyDataSetChanged();
                        if (values.size() == 0 && listener != null) {
                            listener.allItemsDone();
                        }
                    }
                }
            });
        } else {
            holder.parsedIngredListLayout.setVisibility(View.GONE);
            holder.ingredDontExistLayout.setVisibility(View.VISIBLE);
        }
        //        holder.mainView.setTag(company);
        //        holder.mainView.setOnClickListener(v -> {
        //            Company localCompany = (Company) v.getTag();
        //            if (listener != null && localCompany != null) {
        //                listener.onCompanyClick(localCompany);
        //            }
        //        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        public final TextView howItWasTextView;
        public final RecyclerView localingredRecyclerView;
        public final ViewGroup ingredDontExistLayout;
        public final ViewGroup parsedIngredListLayout;
        public final Button saveButton;

        public ViewHolder(View view) {
            super(view);
            mainView = view.findViewById(R.id.main_view);
            howItWasTextView = (TextView) view.findViewById(R.id.approve_ingred_how_it_was);
            localingredRecyclerView = (RecyclerView) view.findViewById(R.id.local_ingredients_recycler_view);
            ingredDontExistLayout = (ViewGroup) view.findViewById(R.id.ingred_dont_exist_layout);
            parsedIngredListLayout = (ViewGroup) view.findViewById(R.id.parsed_list_layout);
            saveButton = (Button) view.findViewById(R.id.approve_button);
        }
    }

    public interface ApproveIngredientsEventListener {
        void allItemsDone();
        //
        //        void onCompanyGeoFenceIconClick(Company company);
        //
        //        void onCompanyLongClick(Company company);
    }
}