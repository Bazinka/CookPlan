package com.cookplan.todo_list.edit_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.ToDoCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class ToDoCategoryListAdapter extends ArrayAdapter<ToDoCategory> {
    private List<ToDoCategory> items;
    private List<ToDoCategory> itemsAll;
    private List<ToDoCategory> suggestions;
    private onLookingForItemListener listener;

    public ToDoCategoryListAdapter(Context context, List<ToDoCategory> items, onLookingForItemListener listener) {
        super(context, R.layout.to_do_category_autocomplete_item_layout, items);
        this.items = items;
        this.itemsAll = new ArrayList<>(items);
        this.listener = listener;
        this.suggestions = new ArrayList<>();
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.to_do_category_autocomplete_item_layout, null);
        }
        ToDoCategory toDoCategory = items.get(position);
        if (toDoCategory != null) {
            TextView nameTextView = (TextView) v.findViewById(R.id.category_item_name);
            if (nameTextView != null) {
                //              Log.i(MY_DEBUG_TAG, "getView Customer Name:"+customer.getStringName());
                nameTextView.setText(toDoCategory.getName());
                nameTextView.setTextColor(ContextCompat.getColor(v.getContext(),
                                                                 toDoCategory.getColor().getColorId()));
            }

        }
        return v;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((ToDoCategory) (resultValue)).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ToDoCategory toDoCategory : itemsAll) {
                    if (toDoCategory.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(toDoCategory);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                if (suggestions.isEmpty()) {
                    listener.onItemDoesntExist();
                } else {
                    listener.onItemExist();
                }
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null && results.count > 0) {
                CopyOnWriteArrayList<ToDoCategory> filteredList = new CopyOnWriteArrayList<>((ArrayList) results.values);
                clear();
                for (ToDoCategory c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

    public interface onLookingForItemListener {
        void onItemDoesntExist();

        void onItemExist();
    }
}