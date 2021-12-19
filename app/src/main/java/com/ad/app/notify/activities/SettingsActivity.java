package com.ad.app.notify.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.ad.app.notify.R;

public class SettingsActivity extends AppCompatActivity {

    // TODO - REDIRECT TO PLAY STORE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Settings);
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

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);


            ListPreference theme = (ListPreference) findPreference(getString(R.string.theme_title));
            SwitchPreferenceCompat switch_AttachPin = (SwitchPreferenceCompat) findPreference(getString(R.string.attach_pin_title));
            ListPreference temporaryNotesTime = (ListPreference) findPreference(getString(R.string.temporary_notes_title));
            Preference btn_clearAll = (Preference) findPreference(getString(R.string.clear_title));
            Preference btn_Email = (Preference) findPreference(getString(R.string.email_title));
            Preference btn_Donate = (Preference) findPreference(getString(R.string.donate_title));
            Preference btn_Feedback = (Preference) findPreference(getString(R.string.feedback_title));
            Preference btn_AboutApp = (Preference) findPreference(getString(R.string.about_app_title));
            Preference btn_PrivacyPolicy = (Preference) findPreference(getString(R.string.privacy_policy_title));
//            Preference btn_Help = (Preference) findPreference(getString(R.string.help_title));


            assert theme != null;
            theme.setOnPreferenceChangeListener((preference, newValue) -> {

                if (newValue.equals("light")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (newValue.equals("dark")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }

                return true;
            });

            assert switch_AttachPin != null;
            switch_AttachPin.setOnPreferenceChangeListener((preference, newValue) -> {

                Toast.makeText(getContext(), newValue.toString(), Toast.LENGTH_SHORT).show();
                return true;
            });

            assert temporaryNotesTime != null;
            temporaryNotesTime.setOnPreferenceChangeListener((preference, newValue) -> {


                Toast.makeText(getContext(), newValue.toString(), Toast.LENGTH_SHORT).show();
                return true;
            });

            assert btn_clearAll != null;
            btn_clearAll.setOnPreferenceClickListener(preference -> {

                new AlertDialog.Builder(requireContext())
                        .setMessage("Remove all active notes?")
                        .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                            //TODO - CLEAR ALL NOTES
                            Toast.makeText(requireContext(), "Clear all", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(android.R.string.no, null).show();

                return true;
            });

            assert btn_Email != null;
            btn_Email.setOnPreferenceClickListener(preference -> {

                String to = "adityakhot6838@gmail.com";
                String subject = "";
                String body = "";

                String mailTo = "mailto:" + to +
                        "?&subject=" + Uri.encode(subject) +
                        "&body=" + Uri.encode(body);

                Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                emailIntent.setData(Uri.parse(mailTo));

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                } catch (Exception e) {
                    //TODO - LOG EXCEPTION
                }

                return true;
            });

            assert btn_Donate != null;
            btn_Donate.setOnPreferenceClickListener(preference -> {

                startActivity(new Intent(requireContext(), DonateActivity.class));
                requireActivity().overridePendingTransition(R.anim.slide_in_from_right,
                        R.anim.slide_out_to_left);

                return true;
            });

            assert btn_Feedback != null;
            btn_Feedback.setOnPreferenceClickListener(preference -> {

                startActivity(new Intent(requireActivity(), FeedbackActivity.class));
                requireActivity().overridePendingTransition(R.anim.slide_in_from_right,
                        R.anim.slide_out_to_left);
                return true;
            });

        }
    }
}