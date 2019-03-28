package com.cookplan

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 * Created by DariaEfimova on 13.10.17.
 */

open class BaseFragment : Fragment(), BaseView {

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

    override fun getContext(): Context {
        return activity as Context
    }
}
