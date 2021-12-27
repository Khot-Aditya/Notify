package com.ad.app.notify.activities;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.service.notification.StatusBarNotification;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ad.app.notify.R;
import com.ad.app.notify.adapter.NotificationRecyclerAdapter;
import com.ad.app.notify.database.NotificationDatabaseHandler;
import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.utils.Constants;
import com.ad.app.notify.utils.TextProcessor;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private LinearLayout linearLayout_NoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_Home);
        linearLayout_NoData = (LinearLayout) findViewById(R.id.linearLayout_NoData);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.ic_toolbar_app_icon);
        }

        refreshRecyclerview(true);

        fab.setOnClickListener(view -> {

            final View dialogView = getLayoutInflater()
                    .inflate(R.layout.dialog_add_new_note, null);

            AlertDialog dialog = new MaterialAlertDialogBuilder(MainActivity.this)
                    .setView(dialogView)
                    .setCancelable(true)
                    .show();

            final TextInputEditText edt_Dialog_Message = dialogView.findViewById(R.id.edt_Dialog_Message);
            final MaterialButton btn_Dialog_Done = dialogView.findViewById(R.id.btn_Dialog_Done);
            final MaterialButton btn_Dialog_Cancel = dialog.findViewById(R.id.btn_Dialog_Cancel);

            edt_Dialog_Message.requestFocus();
            Window window = dialog.getWindow();

//            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | /* does not affect performance */
//                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            if (btn_Dialog_Done != null)
                btn_Dialog_Done.setOnClickListener(v -> {

                    String message = Objects.requireNonNull(edt_Dialog_Message.getText()).toString();

                    if (!message.equals("")) {

                        new TextProcessor(this).process(message, Constants.ACTION_ADD);

                        dialog.dismiss();

                        //need delay to save data in database
                        new Handler(Looper.getMainLooper()).postDelayed(() -> refreshRecyclerview(true), 200);
                    } else {
                        edt_Dialog_Message.setError(getString(R.string.empty_field_error));
                    }
                });

            if (btn_Dialog_Cancel != null)
                btn_Dialog_Cancel.setOnClickListener(v -> dialog.dismiss());

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        clearNotifications();

        refreshRecyclerview(false);

//        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false); /* does not affect performance */

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

    public void refreshRecyclerview(boolean doAnimation) {

        clearNotifications();

        linearLayout_NoData.setVisibility(
                new NotificationDatabaseHandler(this).getItemCount() == 0 ?
                        View.VISIBLE : View.GONE);

        AnimationSet animationSet = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0f, 1f);
//        Animation animation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f);
//        Animation animation = new TranslateAnimation(-1000f,0f,0.0f,1.0f);
//        Animation animation = new RotateAnimation(90f,0f);

        animation.setDuration(500);
        animationSet.addAnimation(animation);
        LayoutAnimationController controller =
                new LayoutAnimationController(animationSet, 0.5f);

        if (doAnimation) recyclerView.setLayoutAnimation(controller);
        recyclerView.setAdapter(
                new NotificationRecyclerAdapter(
                        new NotificationDatabaseHandler(this)
                                .getActiveNotificationList(true), this));
    }

    private void clearNotifications() {

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        StatusBarNotification[] notifications =
                mNotificationManager.getActiveNotifications();

        //if no ongoing notifications found then clear database
        if (notifications.length == 0) {

            new NotificationDatabaseHandler(this).deleteAllNotifications();
        } else {
            //delete notifications which are dismissed

            //get saved notifications in list
            List<NotificationModel> oldModelList = new NotificationDatabaseHandler(this)
                    .getActiveNotificationList(true);

            List<Integer> oldList = new ArrayList<>();
            List<Integer> notificationList = new ArrayList<>();

            for (NotificationModel model : oldModelList)
                oldList.add(model.getNotificationId());

            for (StatusBarNotification notification : notifications)
                notificationList.add(notification.getId());

            List<Integer> newList = new ArrayList<>();
            for (int oldInt : oldList)
                for (int newInt : notificationList)
                    if (oldInt == newInt) newList.add(oldInt);

            // Prepare a union
            List<Integer> union = new ArrayList<>(oldList);
            union.addAll(newList);

            // Prepare an intersection
            List<Integer> intersection = new ArrayList<>(oldList);
            intersection.retainAll(newList);

            // Subtract the intersection from the union
            union.removeAll(intersection);

            // delete notification from union list
            for (int id : union) new NotificationDatabaseHandler(this).deleteNotification(id);
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
        } else if (id == R.id.action_reportBug) {

            startActivity(new Intent(this, FeedbackActivity.class)
                    .putExtra("subject", "Report Bug"));
            overridePendingTransition(R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left);
        }

//        else if (id == R.id.action_sort) {
//
//            new MaterialAlertDialogBuilder(this)
//                    .setView(R.layout.dialog_sort_list)
//                    .show();
//
//        }


        return super.onOptionsItemSelected(item);
    }


}