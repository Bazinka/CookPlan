package com.cookplan.share;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cookplan.BaseFragment;
import com.cookplan.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by DariaEfimova on 01.05.17.
 */

public class BaseShareFragment extends BaseFragment implements ShareView {

    protected SharePresenter presenter;

    protected Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        presenter = new SharePresenterImpl(this);
    }

    public void onCreateOptionsMenu(Menu _menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, _menu);
        menu = _menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_share) {
            if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View layout = inflater.inflate(R.layout.share_with_google_dialog, null);
                alert.setView(layout);
                final EditText userEmailInput = (EditText) layout.findViewById(R.id.user_email_editText);
                final TextView titleTextView = (TextView) layout.findViewById(R.id.user_email_title);
                titleTextView.setText(R.string.family_mode_dialog_recipes_title);

                alert.setTitle(R.string.family_mode)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            String emailText = userEmailInput.getText().toString();
                            if (!emailText.isEmpty()) {
                                if (!emailText.contains(getString(R.string.gmail_ending_title))) {
                                    emailText = emailText + getString(R.string.gmail_ending_title);
                                }
                                if (presenter != null) {
                                    presenter.shareData(emailText);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            } else if (FirebaseAuth.getInstance().getCurrentUser() != null &&
                    FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                        .setTitle(R.string.attention_title)
                        .setMessage(R.string.need_to_auth)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            } else {
                new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                        .setTitle(R.string.attention_title)
                        .setMessage(R.string.cant_share_data)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setShareIcon() {
        if (menu != null) {
            menu.findItem(R.id.app_bar_share).setVisible(true);
        }
        Toast.makeText(getActivity(), R.string.share_success_title, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setShareError(int errorResourceId) {
        Toast.makeText(getActivity(), errorResourceId, Toast.LENGTH_LONG).show();
    }
}
