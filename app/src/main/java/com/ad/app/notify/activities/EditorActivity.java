package com.ad.app.notify.activities;

import static com.ad.app.notify.utils.Constants.NOTIFICATION_MODEL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.ad.app.notify.R;
import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.utils.Constants;
import com.google.android.material.textfield.TextInputEditText;

public class EditorActivity extends AppCompatActivity {

    private NotificationModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        TextInputEditText edt_EditorActivity_Message = (TextInputEditText) findViewById(R.id.edt_EditorActivity_Message);

        if(getIntent().hasExtra(Constants.NOTIFICATION_MODEL)){
            model = (NotificationModel) getIntent().getSerializableExtra(NOTIFICATION_MODEL);
            edt_EditorActivity_Message.setText(model.getNotificationSubText());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Editor);
        toolbar.setNavigationOnClickListener(v -> {

            finish();
            overridePendingTransition(R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
    }
}