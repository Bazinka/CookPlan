package com.cookplan.voice_interaction;

import android.app.VoiceInteractor;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cookplan.BaseActivity;
import com.cookplan.R;

import static com.google.android.gms.actions.NoteIntents.ACTION_CREATE_NOTE;

public class VoiceLaunchActivity extends BaseActivity implements VoiceLauncherView {

    private VoiceLauncherPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_launch);

        presenter = new VoiceLauncherPresenterImpl(this);
        Intent intent = getIntent();
        String action = intent.getAction();
        Log.d("VOICE:", "VoiceLaunchActivity launched");
        if (action.equals(ACTION_CREATE_NOTE)) {
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            handleVoiceQuery(text);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    private void handleVoiceQuery(String query) {
        Log.d("VOICE:", query);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            presenter.handleText(query);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void startVoiceTrigger(VoiceInteractor.PickOptionRequest request) {
        getVoiceInteractor().submitRequest(request);
    }

    @Override
    public void setSuccessOperationResult() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION);
        intent.setComponent(new ComponentName("com.cookplan", "com.cookplan.auth.ui.AuthActivity"));
        startActivity(intent);
    }

    @Override
    public void setErrorString(String errorString) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        TextView textView = (TextView) findViewById(R.id.info_textview);
        textView.setText(errorString);
    }
}