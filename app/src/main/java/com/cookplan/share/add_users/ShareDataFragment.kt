package com.cookplan.share.add_users

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import com.cookplan.BaseFragment
import com.cookplan.R
import com.cookplan.models.Contact

class ShareDataFragment : BaseFragment(), ShareDataView {

    private var adapter: ShareDataAdapter? = null
    private var presenter: ShareDataPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter = ShareDataPresenterImpl(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        mainView = inflater.inflate(R.layout.fragment_share_data, container, false) as ViewGroup

        val recyclerView = mainView?.findViewById<View>(R.id.users_recycler_view) as RecyclerView
        recyclerView.setHasFixedSize(true)

        recyclerView.layoutManager = LinearLayoutManager(activity)

        adapter = ShareDataAdapter {
            setVisibleLayouts(adapter?.contactList ?: mutableListOf())
            if (adapter?.contactList?.isEmpty() == false) {
                presenter?.shareData(getEmailList())
            } else {
                presenter?.turnOffFamilyMode()
            }
        }
        recyclerView.adapter = adapter

        val addUsersFab = mainView?.findViewById<FloatingActionButton>(R.id.add_user_fab)
        addUsersFab?.setOnClickListener {
            val contentDialogView = LayoutInflater.from(activity).inflate(R.layout.share_user_dialog_layout, null)
            val userNameEditTextView = contentDialogView.findViewById<EditText>(R.id.user_email_editText)
            AlertDialog.Builder(activity as Context)
                    .setView(contentDialogView)
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                        val emailText = getEmail(userNameEditTextView.text.toString())
                        if (!emailText.isEmpty()) {
                            val newContact = Contact(emailText)

                            var isContractExist = false
                            for ((email) in adapter?.contactList ?: listOf<Contact>()) {
                                if (newContact.email == email) {
                                    isContractExist = true
                                    break
                                }
                            }
                            if (!isContractExist) {
                                adapter?.addItem(newContact)
                            }
                        }
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, which -> dialog.cancel() }
                    .show()
        }

        presenter = ShareDataPresenterImpl(this)
        presenter?.getSharedUsers()
        return mainView
    }

    private fun getEmailList(): MutableList<String> {
        val contactList = adapter?.contactList ?: listOf<Contact>()
        val emails = mutableListOf<String>()
        contactList.mapTo(emails) { it.email }

        return emails
    }

    private fun getEmail(emailText: String): String {
        if (!emailText.isEmpty() && !emailText.contains(getString(R.string.gmail_ending_title))) {
            return emailText + getString(R.string.gmail_ending_title)
        } else return emailText
    }

    override fun setContactList(contactList: List<Contact>) {
        setVisibleLayouts(contactList)
        adapter?.updateList(contactList)
    }

    private fun setVisibleLayouts(contactList: List<Contact>) {
        val progressBarLayout = mainView?.findViewById<View>(R.id.progress_bar_layout)
        progressBarLayout?.visibility = GONE

        val mainContentLayout = mainView?.findViewById<View>(R.id.main_content_layout)
        val emptyLayoutLayout = mainView?.findViewById<View>(R.id.empty_view)
        if (!contactList.isEmpty()) {
            mainContentLayout?.visibility = VISIBLE
            emptyLayoutLayout?.visibility = GONE
        } else {
            mainContentLayout?.visibility = GONE
            emptyLayoutLayout?.visibility = VISIBLE
        }
    }

    override fun setError(errorResourceId: Int) {
        setErrorToast(getString(errorResourceId))
    }

    companion object {
        @JvmStatic
        fun newInstance(): ShareDataFragment {
            return ShareDataFragment().apply {
                arguments = Bundle().apply {}
            }
        }
    }
}
