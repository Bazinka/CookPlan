package com.cookplan.todo_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private Menu mMenu;

    public static ToDoListFragment newInstance() {
        ToDoListFragment fragment = new ToDoListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        if (mMenu != null) {
            MenuItem menuItem = mMenu.findItem(R.id.action_remove);
            if (menuItem != null) {
                menuItem.setVisible(true);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
        hideMenu();
    }

    private void hideMenu() {
        if (mMenu != null) {
            MenuItem menuItem = mMenu.findItem(R.id.action_remove);
            if (menuItem != null) {
                menuItem.setVisible(false);
            }
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
        if (todoList.isEmpty()) {
            hideMenu();
        }
    }

    @Override
    public void setToDoCategoryList(List<ToDoCategory> toDoCategoryList) {
        setEmptyViewVisability(View.GONE);
        setRecyclerViewVisability(View.VISIBLE);
        adapter.updateCategories(toDoCategoryList);
        if (toDoCategoryList.isEmpty()) {
            hideMenu();
        }
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

    @Override
    public void onCreateOptionsMenu(Menu _menu, MenuInflater inflater) {
        inflater.inflate(R.menu.remove_menu, _menu);
        mMenu = _menu;
        super.onCreateOptionsMenu(_menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_remove) {
            new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                    .setTitle(R.string.delete_todo_list_title)
                    .setMessage(R.string.choose_right_delete_mode)
                    .setPositiveButton(R.string.delete_have_done_items, (dialog, which) -> {
                        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
                        progressBar.setVisibility(View.VISIBLE);
                        if (presenter != null) {
                            presenter.deleteToDoItems(adapter.getHaveDoneItems());
                        }
                    })
                    .setNeutralButton(android.R.string.cancel, null)
                    .setNegativeButton(R.string.delete_all_items_title, (dialog, which) -> {
                        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
                        progressBar.setVisibility(View.VISIBLE);
                        if (presenter != null) {
                            presenter.deleteToDoCategories(adapter.getAllToDoCategory());
                            presenter.deleteToDoItems(adapter.getAllToDoItems());
                        }
                    })
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
