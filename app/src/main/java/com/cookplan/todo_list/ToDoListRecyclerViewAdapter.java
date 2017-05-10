package com.cookplan.todo_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.ToDoItem;

import java.util.List;

public class ToDoListRecyclerViewAdapter extends RecyclerView.Adapter<ToDoListRecyclerViewAdapter.ViewHolder> {

    private final List<ToDoItem> items;

    public ToDoListRecyclerViewAdapter(List<ToDoItem> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_todo_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ToDoItem toDoItem = items.get(position);
        //        holder.mIdView.setText(toDoItem.getId());
        holder.nameTextView.setText(toDoItem.getName());

        holder.mainView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void update(List<ToDoItem> todoList) {
        items.clear();
        items.addAll(todoList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        //        public final TextView commentTextView;
        public final TextView nameTextView;

        public ViewHolder(View view) {
            super(view);
            mainView = view;
            //            mIdView = (TextView) view.findViewById(R.id.id);
            nameTextView = (TextView) view.findViewById(R.id.content);
        }

    }
}
