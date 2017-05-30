package com.cookplan.companies.review.todo_fragment;

import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.ToDoCategory;
import com.cookplan.models.ToDoItem;

import java.util.List;

import static com.cookplan.models.ToDoItemStatus.HAVE_DONE;

public class CompanyToDoRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ToDoItem> toDoList;

    public CompanyToDoRecyclerViewAdapter(List<ToDoItem> toDoList) {
        this.toDoList = toDoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_todo_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ToDoItem toDoItem = toDoList.get(position);
        ViewHolder itemHolder = (ViewHolder) holder;

        itemHolder.nameTextView.setText(toDoItem.getName());
        ToDoCategory toDoCategory = toDoItem.getCategory();
        if (toDoCategory != null) {
            itemHolder.categoryView.setBackgroundResource(toDoCategory.getColor().getColorId());
        } else {
            itemHolder.categoryView.setBackgroundResource(android.R.color.transparent);
        }

        if (toDoItem.getToDoStatus() == HAVE_DONE) {
            itemHolder.nameTextView.setTextColor(ContextCompat.getColor(RApplication.getAppContext(),
                                                                        R.color.white));
            itemHolder.nameTextView.setPaintFlags(
                    itemHolder.nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            if (toDoCategory != null) {
                itemHolder.mainView.setBackgroundResource(toDoCategory.getColor().getColorId());
            } else {
                itemHolder.mainView.setBackgroundResource(R.color.primary_light);
            }
        } else {
            itemHolder.nameTextView.setTextColor(ContextCompat.getColor(RApplication.getAppContext(),
                                                                        R.color.primary_text_color));
            itemHolder.nameTextView.setPaintFlags(itemHolder.nameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            itemHolder.mainView.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        public final TextView commentTextView;
        public final View categoryView;
        public final TextView nameTextView;

        public ViewHolder(View view) {
            super(view);
            mainView = view;
            commentTextView = (TextView) view.findViewById(R.id.todo_item_comment);
            nameTextView = (TextView) view.findViewById(R.id.todo_item_name);
            categoryView = view.findViewById(R.id.category_view);
        }
    }
}