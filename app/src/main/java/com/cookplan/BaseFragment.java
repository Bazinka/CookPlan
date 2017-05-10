package com.cookplan;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by DariaEfimova on 28.03.17.
 */

public class BaseFragment extends Fragment {

    protected ViewGroup mainView;

    protected void setEmptyViewVisability(int visability) {
        if (mainView != null) {
            ViewGroup emptyViewGroup = (ViewGroup) mainView.findViewById(R.id.empty_view);
            if (emptyViewGroup != null) {
                emptyViewGroup.setVisibility(visability);
            }
        }
    }

    public void setErrorToast(String error) {
        View view = mainView.findViewById(R.id.main_view);
        if (view != null) {
            Snackbar.make(view, error, Snackbar.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
        }
    }
}
