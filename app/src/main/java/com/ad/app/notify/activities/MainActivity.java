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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.ad.app.notify.utils.TextProcessor;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    LayoutAnimationController controller;
    LinearLayout linearLayout_NoData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_Home);
        linearLayout_NoData = (LinearLayout) findViewById(R.id.linearLayout_NoData);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AnimationSet animationSet = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        animationSet.addAnimation(animation);
        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100);
        animationSet.addAnimation(animation);
        controller = new LayoutAnimationController(animationSet, 0.5f);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setIcon(R.drawable.ic_toolbar_app_icon);

        refreshRecyclerview();

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

                if (!message.equals("")) {

                    new TextProcessor(this).process(message);

                    dialog.dismiss();

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            refreshRecyclerview();
                        }
                    }, 300);
                } else {
                    //TODO - EXTRACT STRING
                    edt_Dialog_Message.setError("Empty Field");
                }


            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        linearLayout_NoData.setVisibility(
                new NotificationDatabaseHandler(this).getItemCount() == 0 ?
                        View.VISIBLE : View.GONE);

        recyclerView.setAdapter(
                new NotificationRecyclerAdapter(
                        new NotificationDatabaseHandler(this)
                                .getActiveNotificationList(true), this));

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

    public void refreshRecyclerview() {


        linearLayout_NoData.setVisibility(
                new NotificationDatabaseHandler(this).getItemCount() == 0 ?
                        View.VISIBLE : View.GONE);

        recyclerView.setAdapter(
                new NotificationRecyclerAdapter(
                        new NotificationDatabaseHandler(this)
                                .getActiveNotificationList(true), this));
        recyclerView.setLayoutAnimation(controller);


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
        } else if (id == R.id.action_reportBug) {

            new MaterialAlertDialogBuilder(this)
                    .setTitle("Title")
                    .setMessage("Message")
                    .setPositiveButton("Ok", (dialog, which) -> {

                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {

                    })
                    .show();
        } else if (id == R.id.action_sort) {
            Toast.makeText(this, "Sort Clicked", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


}