package com.ad.app.notify.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ad.app.notify.R;
import com.ad.app.notify.adapter.NotificationRecyclerAdapter;
import com.ad.app.notify.database.NotificationDatabaseHandler;
import com.ad.app.notify.databinding.ActivityMainBinding;
import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.service.NotificationService;
import com.ad.app.notify.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setIcon(R.drawable.ic_toolbar_app_icon);


        List<NotificationModel> notificationModelList =
                new NotificationDatabaseHandler(this).getActiveNotificationList();

        RecyclerView recyclerView = findViewById(R.id.recyclerview_Home);

        LinearLayout linearLayout_NoData = (LinearLayout) findViewById(R.id.linearLayout_NoData);

        linearLayout_NoData.setVisibility(
                new NotificationDatabaseHandler(this).getItemCount() == 0 ?
                        View.VISIBLE : View.GONE);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new NotificationRecyclerAdapter(notificationModelList, this));

        }, 100);


        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(view -> {

            final View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_new_note, null);

            AlertDialog dialog = new MaterialAlertDialogBuilder(MainActivity.this)
                    .setView(dialogView)
                    .setCancelable(true)
                    .show();

            final TextInputEditText edt_Dialog_Message = dialogView.findViewById(R.id.edt_Dialog_Message);
            final MaterialButton btn_Dialog_Done = dialogView.findViewById(R.id.btn_Dialog_Done);

            edt_Dialog_Message.requestFocus();
            Window window = dialog.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


            btn_Dialog_Done.setOnClickListener(view2 -> {

                String message = Objects.requireNonNull(edt_Dialog_Message.getText()).toString();

                if (message.equals("")) {
                    //TODO - EXTRACT STRING
                    edt_Dialog_Message.setError("Empty Field");
                } else {

                    int id = new Utils().getNotificationId();

                    NotificationModel notificationModel = new NotificationModel();

                    notificationModel.setNotificationId(id);
                    notificationModel.setNotificationDate("Sun, 19 Dec"); //TODO - GET CURRENT DATE
                    notificationModel.setNotificationTime("6:34"); //TODO - GET CURRENT TIME
                    notificationModel.setNotificationSubText(message);
                    notificationModel.setNotificationCategory("Reminder"); //TODO - SET CATEGORY AFTER TEXT PROCESSING
                    notificationModel.setNotificationTags("sticky note - phone - email");  //TODO - SET TAGS AFTER TEXT PROCESSING
                    notificationModel.setNotificationPinned(true);

                    new NotificationDatabaseHandler(MainActivity.this).addNewNotification(notificationModel);

                    RecyclerView recyclerView1 = findViewById(R.id.recyclerview_Home);
                    recyclerView1.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView1.setAdapter(new NotificationRecyclerAdapter(
                            new NotificationDatabaseHandler(MainActivity.this)
                                    .getActiveNotificationList(), MainActivity.this));

                    linearLayout_NoData.setVisibility(
                            new NotificationDatabaseHandler(this).getItemCount() == 0 ?
                                    View.VISIBLE : View.GONE);

                    Intent intent = new Intent(MainActivity.this, NotificationService.class);
                    intent.putExtra("notification_id", id);
                    intent.putExtra("notification_body", message);
                    startService(intent);

                    dialog.dismiss();
                }


            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        SharedPreferences sharedConfig = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedConfig.getString(getString(R.string.theme_title), "system");

        if (theme.equals("light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (theme.equals("dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this, SettingsActivity.class));
            overridePendingTransition(R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}