package com.cookplan.cooking_plan.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CookPlanMainRecyclerAdapter extends RecyclerView.Adapter<CookPlanMainRecyclerAdapter.ViewHolder> {

    private final Map<LocalDate, List<Object>> map;
    private final List<LocalDate> values;
    private CookPlanClickListener listener;
    private Context context;

    public CookPlanMainRecyclerAdapter(Map<LocalDate, List<Object>> items, CookPlanClickListener listener, Context context) {
        map = new HashMap<>(items);
        values = new ArrayList<>();
        fillValues();
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cooking_plan_main_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        LocalDate date = values.get(position);

        CookPlanForTheDayRecyclerAdapter adapter = new CookPlanForTheDayRecyclerAdapter(
                map.get(date), listener, context);

        holder.cookingForDayRecyclerView.setHasFixedSize(true);
        holder.cookingForDayRecyclerView.setNestedScrollingEnabled(false);
        holder.cookingForDayRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        holder.cookingForDayRecyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.cookingForDayRecyclerView.setAdapter(adapter);

        holder.dayOfMonthTextView.setText(date.toString("dd"));
        holder.nameOfMonthTextView.setText(date.toString("MMMM", Locale.getDefault()));
        holder.dayOfWeekTextView.setText(date.toString("EEEE"));
        if (date.equals(new LocalDate())) {
            //it's today
            int todaysColorId = ContextCompat.getColor(context, R.color.accent_color);
            holder.nameOfMonthTextView.setTextColor(todaysColorId);
            holder.dayOfMonthTextView.setTextColor(todaysColorId);
            holder.dayOfWeekTextView.setTextColor(todaysColorId);
        }
    }

    public void updateItems(Map<LocalDate, List<Object>> dateToObjectMap) {
        map.clear();
        map.putAll(dateToObjectMap);
        values.clear();
        fillValues();
        notifyDataSetChanged();
    }

    //returns position of today's date or later
    public int getPositionForToday() {
        for (int pos = 0; pos < values.size(); pos++) {
            if (values.get(pos).equals(new LocalDate())
                    || values.get(pos).isAfter(new LocalDate())) {
                return pos;
            }
        }
        return 0;
    }

    //returns position of the date or later
    public int getPositionForDate(LocalDate date) {
        for (int pos = 0; pos < values.size(); pos++) {
            if (values.get(pos).equals(date)
                    || values.get(pos).isAfter(date)) {
                return pos;
            }
        }
        return 0;
    }

    private void fillValues() {
        for (LocalDate key : map.keySet()) {
            values.add(key);
        }
        Collections.sort(values, (calendar1, calendar2) -> {
            if (calendar1.equals(calendar2)) {
                return 0;
            } else if (calendar1.isAfter(calendar2)) {
                return 1;
            } else {
                return -1;
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        final TextView dayOfWeekTextView;
        final RecyclerView cookingForDayRecyclerView;
        final TextView dayOfMonthTextView;
        final TextView nameOfMonthTextView;


        ViewHolder(View view) {
            super(view);
            mainView = view.findViewById(R.id.main_view);
            dayOfMonthTextView = (TextView) view.findViewById(R.id.number_day_of_month_textview);
            dayOfWeekTextView = (TextView) view.findViewById(R.id.day_of_week_textview);
            nameOfMonthTextView = (TextView) view.findViewById(R.id.name_of_month_textview);

            cookingForDayRecyclerView = (RecyclerView) view.findViewById(R.id.cooking_items_recycler_view);
        }
    }

    public interface CookPlanClickListener {

        void onRecipeClick(Recipe recipe);

        void onRecipeLongClick(Recipe recipe);

        void onIngredientLongClick(Ingredient ingredient);
    }
}
