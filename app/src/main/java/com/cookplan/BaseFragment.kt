package com.cookplan

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

/**
 * Created by DariaEfimova on 13.10.17.
 */

open class BaseFragment : Fragment() {

    protected var mainView: ViewGroup? = null

    protected fun setEmptyViewVisability(visability: Int) {
        val emptyViewGroup = mainView?.findViewById<ViewGroup>(R.id.empty_view)
        emptyViewGroup?.visibility = visability
    }

    open fun setErrorToast(error: String) {
        val view = mainView?.findViewById<View>(R.id.main_view)
        if (view != null) {
            Snackbar.make(view, error, Snackbar.LENGTH_LONG).show()
        } else {
            Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
        }
    }
}
