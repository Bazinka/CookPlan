package com.cookplan.todo_list;

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

import java.util.ArrayList;
import java.util.List;

import static com.cookplan.todo_list.ToDoListRecyclerViewAdapter.ItemType.CATEGORY;
import static com.cookplan.todo_list.ToDoListRecyclerViewAdapter.ItemType.TODO_ITEM;

public class ToDoListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Object> objectsList;
    private final List<ToDoCategory> toDoCategoriesList;

    public ToDoListRecyclerViewAdapter() {
        this.objectsList = new ArrayList<>();
        toDoCategoriesList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        Object object = objectsList.get(position);
        if (object instanceof ToDoItem) {
            return TODO_ITEM.getId();
        } else {
            return CATEGORY.getId();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemType type = ItemType.getItemTypeById(viewType);
        if (type != null) {
            switch (type) {
                case TODO_ITEM: {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.todo_list_item_layout, parent, false);
                    return new TodoItemViewHolder(view);
                }
                case CATEGORY: {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.todo_category_list_item_layout, parent, false);
                    return new TodoCategoryViewHolder(view);
                }
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemType type = ItemType.getItemTypeById(holder.getItemViewType());
        if (type != null) {
            switch (type) {
                case TODO_ITEM: {
                    ToDoItem toDoItem = (ToDoItem) objectsList.get(position);
                    if (toDoItem != null) {
                        TodoItemViewHolder itemHolder = (TodoItemViewHolder) holder;

                        itemHolder.nameTextView.setText(toDoItem.getName());
                        ToDoCategory toDoCategory = null;
                        for (ToDoCategory category : toDoCategoriesList) {
                            if (category.getId().equals(toDoItem.getCategoryId())) {
                                toDoCategory = category;
                                break;
                            }
                        }
                        if (toDoCategory != null) {
                            itemHolder.categoryView.setBackgroundResource(toDoCategory.getColor().getColorId());
                        } else {
                            itemHolder.categoryView.setBackgroundResource(android.R.color.transparent);
                        }

                        itemHolder.mainView.setOnClickListener(v -> {

                        });
                    }
                }
                break;

                case CATEGORY: {
                    ToDoCategory toDoCategory = (ToDoCategory) objectsList.get(position);
                    if (toDoCategory != null) {
                        TodoCategoryViewHolder categoryHolder = (TodoCategoryViewHolder) holder;

                        categoryHolder.nameTextView.setText(toDoCategory.getName());
                        categoryHolder.nameTextView.setTextColor(
                                ContextCompat.getColor(RApplication.getAppContext(),
                                                       toDoCategory.getColor().getColorId()));
                        categoryHolder.categoryView.setBackgroundResource(toDoCategory.getColor().getColorId());

                        categoryHolder.mainView.setOnClickListener(v -> {

                        });
                    }
                }
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return objectsList.size();
    }

    public void updateToDoList(List<ToDoItem> todoList) {
        objectsList.clear();
        for (ToDoItem item : todoList) {
            if (item.getCategoryId() == null) {
                objectsList.add(item);
            }
        }
        for (ToDoCategory category : toDoCategoriesList) {
            objectsList.add(category);
            for (ToDoItem item : todoList) {
                if (item.getCategoryId() != null && category.getId().equals(item.getCategoryId())) {
                    objectsList.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    public void updateCategories(List<ToDoCategory> categoryList) {
        objectsList.clear();
        toDoCategoriesList.clear();
        objectsList.addAll(new ArrayList<>(categoryList));
        toDoCategoriesList.addAll(new ArrayList<>(categoryList));
        notifyDataSetChanged();
    }

    public class TodoItemViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        public final TextView commentTextView;
        public final View categoryView;
        public final TextView nameTextView;

        public TodoItemViewHolder(View view) {
            super(view);
            mainView = view;
            commentTextView = (TextView) view.findViewById(R.id.todo_item_comment);
            nameTextView = (TextView) view.findViewById(R.id.todo_item_name);
            categoryView = view.findViewById(R.id.category_view);
        }

    }

    public class TodoCategoryViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        public final View categoryView;
        public final TextView nameTextView;

        public TodoCategoryViewHolder(View view) {
            super(view);
            mainView = view;
            categoryView = view.findViewById(R.id.category_view);
            nameTextView = (TextView) view.findViewById(R.id.todo_category_name);
        }

    }

    enum ItemType {
        CATEGORY(0),
        TODO_ITEM(1);

        private int id;

        ItemType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static ItemType getItemTypeById(int id) {
            if (id == CATEGORY.getId()) {
                return CATEGORY;
            }

            if (id == TODO_ITEM.getId()) {
                return TODO_ITEM;
            }
            return null;
        }
    }
}