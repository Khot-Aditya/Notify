package com.ad.app.notify.activities;

import static com.ad.app.notify.utils.Constants.NOTIFICATION_MODEL;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.ad.app.notify.R;
import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.utils.TextProcessor;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class EditorActivity extends AppCompatActivity {

    private NotificationModel model;
    TextInputEditText edt_EditorActivity_Message;
    MaterialTextView txt_Category;
    MaterialTextView txt_TimeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.preferences, new EditorActivity.EditorFragment())
                    .commit();
        }

        edt_EditorActivity_Message = (TextInputEditText) findViewById(R.id.edt_Message_EditorActivity);
        txt_Category = (MaterialTextView) findViewById(R.id.txt_Category);
        txt_TimeDate = (MaterialTextView) findViewById(R.id.txt_TimeDate);

        if (getIntent().hasExtra(NOTIFICATION_MODEL)) {
            model = (NotificationModel) getIntent().getSerializableExtra(NOTIFICATION_MODEL);
            edt_EditorActivity_Message.setText(model.getNotificationSubText());
            txt_Category.setText(model.getNotificationTags());
            txt_TimeDate.setText(String.format("%s • %s",
                    model.getNotificationTime(),
                    model.getNotificationDate()));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Editor);

        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_SaveNote) {
                if (!edt_EditorActivity_Message.getText().equals("")) {

                    model.setNotificationSubText(edt_EditorActivity_Message.getText().toString());
                    //TODO - pin unpin
                    new TextProcessor(EditorActivity.this).update(model);
                    finish();
                }
            } else if (id == R.id.action_DeleteNote) {

                new MaterialAlertDialogBuilder(this)
                        .setMessage("Are you sure you want to delete this note?")
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            Toast.makeText(EditorActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                        })
                        .setPositiveButton("Delete", (dialog, which) -> {
                            NotificationManagerCompat.from(EditorActivity.this).cancel(model.getNotificationId());
                            finish();
                        })
                        .show();

            } else if (id == R.id.action_Settings) {

                startActivity(new Intent(EditorActivity.this, SettingsActivity.class));
                overridePendingTransition(R.anim.slide_in_from_right,
                        R.anim.slide_out_to_left);

            } else if (id == R.id.action_HelpFeedback) {

                startActivity(new Intent(EditorActivity.this, FeedbackActivity.class)
                        .putExtra("subject", "Help"));
                overridePendingTransition(R.anim.slide_in_from_right,
                        R.anim.slide_out_to_left);
            }

            return true;
        });
        toolbar.setNavigationOnClickListener(v -> {

            finish();
            overridePendingTransition(R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right);
        });

    }

    public static class EditorFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_editor_activity, rootKey);

            Preference preference_Search = (Preference) findPreference()
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
    }


}