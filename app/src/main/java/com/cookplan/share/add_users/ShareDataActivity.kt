package com.cookplan.share.add_users

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.main.MainActivity
import com.cookplan.models.Contact
import com.cookplan.share.contact_list.ContactListActivity

class ShareDataActivity : BaseActivity(), ShareDataView {

    private var adapter: ShareDataAdapter? = null
    private var presenter: ShareDataPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_for_sharing)
        setNavigationArrow()
        setTitle(getString(R.string.share_data_title))
        val recyclerView = findViewById<View>(R.id.users_recycler_view) as RecyclerView
        recyclerView.setHasFixedSize(true)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ShareDataAdapter()
        recyclerView.adapter = adapter

        val emailEditText = findViewById<View>(R.id.user_email_editText) as EditText
        emailEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val emailText = getEmail(emailEditText.text.toString())
                var errorText: String? = null
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
                    emailEditText.error = null
                    emailEditText.setText(null)
                } else {
                    errorText = getString(R.string.error_required_field)
                }
                val emailLayout = findViewById<TextInputLayout>(R.id.enter_email_edittext_layout)
                emailLayout.error = errorText
                emailLayout.isErrorEnabled = errorText != null
                true
            }
            false
        }

        val getFromContactListButton = findViewById<View>(R.id.get_from_contact_button) as Button
        getFromContactListButton.setOnClickListener {
            val intent = Intent(this, ContactListActivity::class.java)
            startActivityForResultWithLeftAnimation(intent, GET_USER_LIST_FROM_CONTACT_REQUEST)

        }
        presenter = ShareDataPresenterImpl(this)
        presenter?.getSharedUsers()
    }

    private fun getEmail(emailText: String): String {
        if (!emailText.contains(getString(R.string.gmail_ending_title))) {
            return emailText + getString(R.string.gmail_ending_title)
        } else return emailText
    }

    override fun onCreateOptionsMenu(_menu: Menu): Boolean {
        menuInflater.inflate(R.menu.done_menu, _menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.app_bar_done) {
            val contactList = adapter?.contactList ?: listOf<Contact>()
            val emails = ArrayList<String>()
            contactList.mapTo(emails) { it.email }

            val textFromEditView = getEmail(findViewById<EditText>(R.id.user_email_editText).text.toString())
            if (!textFromEditView.isEmpty()) {
                emails.add(textFromEditView)
            }

            findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE

            if (emails.isEmpty()) {
                presenter?.turnOffFamilyMode()
            } else {
                presenter?.shareData(emails)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setContactList(contactList: List<Contact>) {
        adapter?.updateList(contactList)
    }

    fun addContactList(contactList: List<Contact>) {
        adapter?.addItemList(contactList)

    }

    override fun setError(error: String) {
        Snackbar.make(findViewById<View>(R.id.main_view), error, Snackbar.LENGTH_LONG).show()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_USER_LIST_FROM_CONTACT_REQUEST && resultCode == Activity.RESULT_OK) {
            val contacts = data.getParcelableArrayListExtra<Contact>(GET_USER_LIST_FROM_CONTACT_KEY)
            addContactList(contacts)
        }
    }

    override fun setShareSuccess(isFamilyModeTurnOn: Boolean) {
        val intent = Intent()
        intent.putExtra(MainActivity.FAMILY_TURNED_ON_KEY, isFamilyModeTurnOn)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun setShareError(errorResourceId: Int) {
        Toast.makeText(this, errorResourceId, Toast.LENGTH_LONG).show()
    }

    companion object {

        val GET_USER_LIST_FROM_CONTACT_REQUEST = 12
        val GET_USER_LIST_FROM_CONTACT_KEY = "GET_USER_LIST_FROM_CONTACT_KEY"
    }
}
