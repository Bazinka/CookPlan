package com.cookplan.companies.review.todo_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cookplan.BaseActivity;
import com.cookplan.BaseFragment;
import com.cookplan.R;
import com.cookplan.models.Company;
import com.cookplan.models.ToDoCategory;
import com.cookplan.models.ToDoItem;
import com.cookplan.todo_list.edit_item.EditToDoItemActivity;

import java.util.List;

public class CompanyToDoListFragment extends BaseFragment implements CompanyToDoListView {

    private static final String COMPANY_OBJECT_KEY = "COMPANY_OBJECT_KEY";

    private CompanyToDoListPresenter presenter;
    private Company company;

    public static CompanyToDoListFragment newInstance(Company company) {
        CompanyToDoListFragment fragment = new CompanyToDoListFragment();
        Bundle args = new Bundle();
        args.putSerializable(COMPANY_OBJECT_KEY, company);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            company = (Company) getArguments().getSerializable(COMPANY_OBJECT_KEY);
        }
        setRetainInstance(true);
        presenter = new CompanyToDoListPresenterImpl(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null && company != null) {
            presenter.getCompanyToDoList(company);
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
        mainView = (ViewGroup) inflater.inflate(R.layout.fragment_company_todo_list, container, false);

        RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.todo_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.add_todo_item_fab);
        //        fab.setOnClickListener(view -> {
        //            startNewToDoItemActivity();
        //        });
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
        for (ToDoItem item : todoList) {
            item.setCategory(presenter.getToDoCategoryById(item.getCategoryId()));
        }
        CompanyToDoRecyclerViewAdapter adapter = new CompanyToDoRecyclerViewAdapter(todoList);
        RecyclerView needToBuyRecyclerView = (RecyclerView) mainView.findViewById(R.id.todo_list_recycler_view);
        needToBuyRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setToDoCategoryList(List<ToDoCategory> toDoCategoryList) {
        setEmptyViewVisability(View.GONE);
        setRecyclerViewVisability(View.VISIBLE);
        if (presenter != null) {
            presenter.setCompanyCategoryToDoList(toDoCategoryList);
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

}
