package com.cookplan;

import android.support.v4.app.Fragment;
import android.view.ViewGroup;

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
}
