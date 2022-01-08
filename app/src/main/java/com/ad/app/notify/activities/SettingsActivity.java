package com.ad.app.notify.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.ad.app.notify.BuildConfig;
import com.ad.app.notify.R;
import com.ad.app.notify.database.NotificationDatabaseHandler;
import com.ad.app.notify.utils.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        new Utils(this).log("onCreate");

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

        TextView txt_app_version = (TextView) findViewById(R.id.txt_app_version);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Settings);
        toolbar.setNavigationOnClickListener(v -> {

            finish();
            overridePendingTransition(R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right);
        });

        txt_app_version.setText(
                getString(R.string.version) + " " +
                        BuildConfig.VERSION_NAME + " " +
                        getString(R.string.beta));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        new Utils(this).log("onBackPressed");

        overridePendingTransition(R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_settings_activity, rootKey);

            new Utils(requireContext()).log("onCreatePreference");

            ListPreference theme = (ListPreference) findPreference(getString(R.string.theme_title));
            SwitchPreferenceCompat switch_AttachPin = (SwitchPreferenceCompat) findPreference(getString(R.string.attach_pin_title));
            ListPreference temporaryNotesTime = (ListPreference) findPreference(getString(R.string.temporary_notes_title));
//            SwitchPreferenceCompat switch_GroupNotifications = (SwitchPreferenceCompat) findPreference(getString(R.string.groups_title));
            SwitchPreferenceCompat switch_CollapseNotifications = (SwitchPreferenceCompat) findPreference(getString(R.string.collapsed_view_title));
            Preference btn_clearAll = (Preference) findPreference(getString(R.string.clear_title));
//            Preference btn_Email = (Preference) findPreference(getString(R.string.email_title));
            Preference btn_Donate = (Preference) findPreference(getString(R.string.donate_title));
            Preference btn_Feedback = (Preference) findPreference(getString(R.string.feedback_title));
            Preference btn_AboutApp = (Preference) findPreference(getString(R.string.about_app_title));
            Preference btn_PrivacyPolicy = (Preference) findPreference(getString(R.string.privacy_policy_title));
//            Preference btn_Help = (Preference) findPreference(getString(R.string.help_title));


            if (theme != null)
                theme.setOnPreferenceChangeListener((preference, newValue) -> {

                    new Utils(requireContext()).log("Tap - theme");

                    if (newValue.equals("light")) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    } else if (newValue.equals("dark")) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    }

                    return true;
                });

            if (switch_AttachPin != null) {
                switch_AttachPin.setOnPreferenceChangeListener((preference, newValue) -> {

                    new Utils(requireContext()).log("Tap - switch_AttachPin");
                    return true;
                });
            }


            if (temporaryNotesTime != null) {
                temporaryNotesTime.setOnPreferenceChangeListener((preference, newValue) -> {

                    new Utils(requireContext()).log("Tap - temporaryNotesTime");
                    return true;
                });
            }

//            if (switch_GroupNotifications != null)
//            switch_GroupNotifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object newValue) {
//
//
//
//                    return true;
//                }
//            });

            if (switch_CollapseNotifications != null)
                switch_CollapseNotifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        if (newValue.equals(false)) {
                            new Utils(requireContext()).log("Tap - switch_CollapseNotifications");
                            new MaterialAlertDialogBuilder(requireContext())
                                    .setMessage("Turning off this feature may cause some errors.")
                                    .show();
                        }
                        return true;
                    }
                });


            if (btn_clearAll != null)
                btn_clearAll.setOnPreferenceClickListener(preference -> {

                    new MaterialAlertDialogBuilder(requireContext())
                            .setMessage("Remove all active notes?")
                            .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {

                                if (new NotificationDatabaseHandler(requireContext()).deleteAllNotifications()) {

                                    new Utils(requireContext()).log("Tap - btn_clearAll");

                                    try {
//                                    NotificationManager notificationManager =
//                                            (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//                                    notificationManager.cancelAll();

                                        NotificationManagerCompat.from(requireContext()).cancelAll();
                                        Toast.makeText(requireContext(), "Done", Toast.LENGTH_SHORT).show();
                                    } catch (Exception ignored) {

                                    }

                                } else {
                                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();

                    return true;
                });

//            if (btn_Email != null)
//                btn_Email.setOnPreferenceClickListener(preference -> {
//
//                    String to = "adityakhot6838@gmail.com";
//                    String subject = "";
//                    String body = "";
//
//                    String mailTo = "mailto:" + to +
//                            "?&subject=" + Uri.encode(subject) +
//                            "&body=" + Uri.encode(body);
//
//                    Intent emailIntent = new Intent(Intent.ACTION_VIEW);
//                    emailIntent.setData(Uri.parse(mailTo));
//
//                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
//                    return true;
//                });

            if (btn_Donate != null)
                btn_Donate.setOnPreferenceClickListener(preference -> {


                    new Utils(requireContext()).log("Tap - btn_Donate");

                    startActivity(new Intent(requireContext(), DonateActivity.class));
                    requireActivity().overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);

                    return true;
                });

            if (btn_Feedback != null)
                btn_Feedback.setOnPreferenceClickListener(preference -> {

                    new Utils(requireContext()).log("Tap - btn_FeedBack");

                    startActivity(new Intent(requireActivity(), FeedbackActivity.class));
                    requireActivity().overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                    return true;
                });

            if (btn_AboutApp != null)
                btn_AboutApp.setOnPreferenceClickListener(preference -> {

                    new Utils(requireContext()).log("Tap - btn_AboutApp");

                    // TODO - REDIRECT TO PLAY STORE
                    Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                });

            if (btn_PrivacyPolicy != null)
                btn_PrivacyPolicy.setOnPreferenceClickListener(preference -> {

                    new Utils(requireContext()).log("Tap - btn_PrivacyPolicy");

                    Intent intent;
                    intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, "https://khot-aditya.github.io/Privacy-Policy/Notify");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    return true;
                });
        }
    }
}