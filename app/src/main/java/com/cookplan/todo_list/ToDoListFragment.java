package com.cookplan.todo_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cookplan.BaseActivity;
import com.cookplan.BaseFragment;
import com.cookplan.R;
import com.cookplan.models.ToDoCategory;
import com.cookplan.models.ToDoItem;
import com.cookplan.todo_list.edit_item.EditToDoItemActivity;

import java.util.List;

public class ToDoListFragment extends BaseFragment implements ToDoListView {

    private ToDoListPresenter presenter;
    private ToDoListRecyclerViewAdapter adapter;

    public static ToDoListFragment newInstance() {
        ToDoListFragment fragment = new ToDoListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
        presenter = new ToDoListPresenterImpl(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.getToDoList();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = (ViewGroup) inflater.inflate(R.layout.fragment_todo_list, container, false);

        RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.todo_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ToDoListRecyclerViewAdapter(new ToDoListRecyclerViewAdapter.OnToDoItemClickListener() {
            @Override
            public void OnToDoItemClick(ToDoItem toDoItem) {
                if (presenter != null) {
                    presenter.updateToDoItem(toDoItem);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.add_todo_item_fab);
        fab.setOnClickListener(view -> {
            startNewToDoItemActivity();
        });
        return mainView;
    }

    private void startNewToDoItemActivity() {
        Intent intent = new Intent(getActivity(), EditToDoItemActivity.class);
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).startActivityWithLeftAnimation(intent);
        }
    }

    @Override
    public void setToDoList(List<ToDoItem> todoList) {
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.GONE);
        setRecyclerViewVisability(View.VISIBLE);
        adapter.updateToDoList(todoList);
    }

    @Override
    public void setToDoCategoryList(List<ToDoCategory> toDoCategoryList) {
        setEmptyViewVisability(View.GONE);
        setRecyclerViewVisability(View.VISIBLE);
        adapter.updateCategories(toDoCategoryList);
    }

    @Override
    public void setEmptyView() {
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.VISIBLE);
        setRecyclerViewVisability(View.GONE);
    }

    private void setRecyclerViewVisability(int visability) {
        View recyclerView = mainView.findViewById(R.id.todo_list_recycler_view);
        if (recyclerView != null) {
            recyclerView.setVisibility(visability);
        }
    }
}
