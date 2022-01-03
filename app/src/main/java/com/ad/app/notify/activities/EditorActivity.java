package com.ad.app.notify.activities;

import static com.ad.app.notify.utils.Constants.NOTIFICATION_MODEL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.ad.app.notify.ColorPaletteView;
import com.ad.app.notify.R;
import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.utils.TextProcessor;
import com.ad.app.notify.utils.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class EditorActivity extends AppCompatActivity {

    private static NotificationModel model;
    private static TextInputEditText edt_EditorActivity_Message;
    private MaterialTextView txt_Category;
    private MaterialTextView txt_TimeDate;

    private ColorPaletteView colorPalette_app_color;
    private ColorPaletteView colorPalette_white;
    private ColorPaletteView colorPalette_black;
    private ColorPaletteView colorPalette_ice_cold;
    private ColorPaletteView colorPalette_rusty_red;
    private ColorPaletteView colorPalette_pastel_violet;
    private ColorPaletteView colorPalette_cold_purple;
    private ColorPaletteView colorPalette_dodger_blue;
    private ColorPaletteView colorPalette_medium_turquoise;
    private ColorPaletteView colorPalette_aquamarine;
    private ColorPaletteView colorPalette_dark_pastel_green;
    private ColorPaletteView colorPalette_light_gold;
    private ColorPaletteView colorPalette_chardonnay;
    private ColorPaletteView colorPalette_geraldine;
    private ColorPaletteView colorPalette_lavender_pinocchio;


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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Editor);
        edt_EditorActivity_Message = (TextInputEditText) findViewById(R.id.edt_Message_EditorActivity);
        txt_Category = (MaterialTextView) findViewById(R.id.txt_Category);
        txt_TimeDate = (MaterialTextView) findViewById(R.id.txt_TimeDate);
        colorPalette_app_color = (ColorPaletteView) findViewById(R.id.colorPalette_app_color);
        colorPalette_white = (ColorPaletteView) findViewById(R.id.colorPalette_white);
        colorPalette_black = (ColorPaletteView) findViewById(R.id.colorPalette_black);
        colorPalette_ice_cold = (ColorPaletteView) findViewById(R.id.colorPalette_ice_cold);
        colorPalette_rusty_red = (ColorPaletteView) findViewById(R.id.colorPalette_rusty_red);
        colorPalette_pastel_violet = (ColorPaletteView) findViewById(R.id.colorPalette_pastel_violet);
        colorPalette_cold_purple = (ColorPaletteView) findViewById(R.id.colorPalette_cold_purple);
        colorPalette_dodger_blue = (ColorPaletteView) findViewById(R.id.colorPalette_dodger_blue);
        colorPalette_medium_turquoise = (ColorPaletteView) findViewById(R.id.colorPalette_medium_turquoise);
        colorPalette_aquamarine = (ColorPaletteView) findViewById(R.id.colorPalette_aquamarine);
        colorPalette_dark_pastel_green = (ColorPaletteView) findViewById(R.id.colorPalette_dark_pastel_green);
        colorPalette_light_gold = (ColorPaletteView) findViewById(R.id.colorPalette_light_gold);
        colorPalette_chardonnay = (ColorPaletteView) findViewById(R.id.colorPalette_chardonnay);
        colorPalette_geraldine = (ColorPaletteView) findViewById(R.id.colorPalette_geraldine);
        colorPalette_lavender_pinocchio = (ColorPaletteView) findViewById(R.id.colorPalette_lavender_pinocchio);

//        colorPalette_white.setEnabled(true);

        if (getIntent().hasExtra(NOTIFICATION_MODEL)) {
            model = (NotificationModel) getIntent().getSerializableExtra(NOTIFICATION_MODEL);
            edt_EditorActivity_Message.setText(model.getNotificationSubText());
            txt_Category.setText(model.getNotificationTags());
            txt_TimeDate.setText(String.format("%s • %s",
                    model.getNotificationTime(),
                    model.getNotificationDate()));

//            Toast.makeText(this, String.valueOf(), Toast.LENGTH_SHORT).show();

            setBackgroundColor(getActiveColorPalette(model.getNotificationBgColor()));
        }

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
                        .putExtra("subject", "Help & Feedback"));
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

            Preference preference_Search = (Preference) findPreference(getString(R.string.search_as_query));
            Preference preference_CopyToClipboard = (Preference) findPreference(getString(R.string.copy_to_clipboard));
            Preference preference_SendEmail = (Preference) findPreference(getString(R.string.send_email));
            Preference preference_SendMessage = (Preference) findPreference(getString(R.string.send_message));
            Preference preference_MakeACall = (Preference) findPreference(getString(R.string.make_a_call));
            Preference preference_AddToContact = (Preference) findPreference(getString(R.string.add_to_contact));
            Preference preference_Share = (Preference) findPreference(getString(R.string.share));

            if (!model.getNotificationSubText().equals("")) {
                if (preference_Search != null)
                    preference_Search.setOnPreferenceClickListener(preference -> {
                        /*
                            TODO -
                                IF ANY URL FOUND IN SUBTEXT THEN SHOW IT IN RECYCLER VIEW AND
                                GIVE OPTION TO SEARCH IT, ELSE SEARCH WHOLE BODY.
                         */
                        return true;
                    });

                if (preference_CopyToClipboard != null)
                    preference_CopyToClipboard.setOnPreferenceClickListener(preference -> {
                        new Utils(requireContext()).copyToClipboard(Objects.requireNonNull(
                                edt_EditorActivity_Message.getText()).toString());
                        return true;
                    });

                if (preference_SendEmail != null)
                    preference_SendEmail.setOnPreferenceClickListener(preference -> {
                        /*
                            TODO -
                                IF EMAIL FOUND IN SUBTEXT THEN SET IT AS EMAIL ELSE SET SUBTEXT
                                AS BODY. USE RECYCLER VIEW TO SHOW MULTIPLE EMAIL ADDRESSES.
                         */
                        return true;
                    });

                if (preference_SendMessage != null)
                    preference_SendMessage.setOnPreferenceClickListener(preference -> {
                        /*
                            TODO -
                                IF ANY NUMBER FOUND IN SUBTEXT THEN USE IT AS SENDER ELSE USE
                                WHOLE SUBTEXT AS BODY. USE RECYCLER VIEW TO SHOW MULTIPLE PHONE NUMBERS.
                         */
                        return true;
                    });

                if (preference_MakeACall != null)
                    preference_MakeACall.setOnPreferenceClickListener(preference -> {
                        /*
                            TODO -
                                OPEN DIALER WITH PHONE NUMBER INSERTED IF ANY, ELSE SHOW TOAST
                                MESSAGE PHONE NUMBER NOT FOUND. USE RECYCLER VIEW TO SHOW
                                MULTIPLE PHONE NUMBERS.

                         */
                        return true;
                    });

                if (preference_AddToContact != null)
                    preference_AddToContact.setOnPreferenceClickListener(preference -> {
                        /*
                            TODO -
                                OPEN ADD TO CONTACT SCREEN WITH PHONE NUMBER INSERTED, IF NOT
                                FOUND ANY SHOW TOAST. USE RECYCLER VIEW TO SHOW MULTIPLE PHONE
                                NUMBERS TO SAVE.
                         */
                        return true;
                    });

                if (preference_Share != null)
                    preference_Share.setOnPreferenceClickListener(preference -> {
                        new Utils(requireContext()).shareText(model.getNotificationSubText());
                        return true;
                    });
            } else {
                Toast.makeText(requireContext(), "Field Empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private View getActiveColorPalette(int color) {

        if (color == getColor(R.color.color_app)) return colorPalette_app_color;
        else if (color == getColor(R.color.color_white)) return colorPalette_white;
        else if (getColor(R.color.color_black) == color) return colorPalette_black;
        else if (getColor(R.color.color_ice_cold) == color) return colorPalette_ice_cold;
        else if (getColor(R.color.color_rusty_red) == color) return colorPalette_rusty_red;
        else if (getColor(R.color.color_pastel_violet) == color) return colorPalette_pastel_violet;
        else if (getColor(R.color.color_cold_purple) == color) return colorPalette_cold_purple;
        else if (getColor(R.color.color_dodger_blue) == color) return colorPalette_dodger_blue;
        else if (getColor(R.color.color_medium_turquoise) == color)
            return colorPalette_medium_turquoise;
        else if (getColor(R.color.color_aquamarine) == color) return colorPalette_aquamarine;
        else if (getColor(R.color.color_dark_pastel_green) == color)
            return colorPalette_dark_pastel_green;
        else if (getColor(R.color.color_light_gold) == color) return colorPalette_light_gold;
        else if (getColor(R.color.color_chardonnay) == color) return colorPalette_chardonnay;
        else if (getColor(R.color.color_geraldine) == color) return colorPalette_geraldine;
        else if (getColor(R.color.color_lavender_pinocchio) == color)
            return colorPalette_lavender_pinocchio;
        else return colorPalette_app_color;

    }

    public void setBackgroundColor(View view) {
        int id = view.getId();
        int color = 0;

        colorPalette_app_color.setEnabled(id == R.id.colorPalette_app_color);
        colorPalette_white.setEnabled(id == R.id.colorPalette_white);
        colorPalette_black.setEnabled(id == R.id.colorPalette_black);
        colorPalette_ice_cold.setEnabled(id == R.id.colorPalette_ice_cold);
        colorPalette_rusty_red.setEnabled(id == R.id.colorPalette_rusty_red);
        colorPalette_pastel_violet.setEnabled(id == R.id.colorPalette_pastel_violet);
        colorPalette_cold_purple.setEnabled(id == R.id.colorPalette_cold_purple);
        colorPalette_dodger_blue.setEnabled(id == R.id.colorPalette_dodger_blue);
        colorPalette_medium_turquoise.setEnabled(id == R.id.colorPalette_medium_turquoise);
        colorPalette_aquamarine.setEnabled(id == R.id.colorPalette_aquamarine);
        colorPalette_dark_pastel_green.setEnabled(id == R.id.colorPalette_dark_pastel_green);
        colorPalette_light_gold.setEnabled(id == R.id.colorPalette_light_gold);
        colorPalette_chardonnay.setEnabled(id == R.id.colorPalette_chardonnay);
        colorPalette_geraldine.setEnabled(id == R.id.colorPalette_geraldine);
        colorPalette_lavender_pinocchio.setEnabled(id == R.id.colorPalette_lavender_pinocchio);

        if (id == R.id.colorPalette_app_color) color = getColor(R.color.color_app);
        if (id == R.id.colorPalette_white) color = getColor(R.color.color_white);
        else if (id == R.id.colorPalette_black) color = getColor(R.color.color_black);
        else if (id == R.id.colorPalette_ice_cold) color = getColor(R.color.color_ice_cold);
        else if (id == R.id.colorPalette_rusty_red) color = getColor(R.color.color_rusty_red);
        else if (id == R.id.colorPalette_pastel_violet)
            color = getColor(R.color.color_pastel_violet);
        else if (id == R.id.colorPalette_cold_purple) color = getColor(R.color.color_cold_purple);
        else if (id == R.id.colorPalette_dodger_blue) color = getColor(R.color.color_dodger_blue);
        else if (id == R.id.colorPalette_medium_turquoise)
            color = getColor(R.color.color_medium_turquoise);
        else if (id == R.id.colorPalette_aquamarine) color = getColor(R.color.color_aquamarine);
        else if (id == R.id.colorPalette_dark_pastel_green)
            color = getColor(R.color.color_dark_pastel_green);
        else if (id == R.id.colorPalette_light_gold) color = getColor(R.color.color_light_gold);
        else if (id == R.id.colorPalette_chardonnay) color = getColor(R.color.color_chardonnay);
        else if (id == R.id.colorPalette_geraldine) color = getColor(R.color.color_geraldine);
        else if (id == R.id.colorPalette_lavender_pinocchio)
            color = getColor(R.color.color_lavender_pinocchio);

        model.setNotificationBgColor(color);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
    }
}