package com.ad.app.notify.service;

import static com.ad.app.notify.utils.Constants.CHANNEL_ID;
import static com.ad.app.notify.utils.Constants.CHANNEL_NAME;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.ad.app.notify.R;
import com.ad.app.notify.activities.MainActivity;
import com.ad.app.notify.database.NotificationDatabaseHandler;
import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.receiver.NotificationActionReceiver;
import com.ad.app.notify.utils.Constants;
import com.ad.app.notify.utils.Utils;

public class NotificationService extends Service {


    //TODO - SETTINGS - DISMISS NOTIFICATIONS AFTER SPECIFIC TIME (DEFAULT 24 HRS)
    //TODO - SETTINGS FOR PHONE NUMBER [OPTIONS - TAP TO COPY/ TAP TO OPEN KEYPAD]
    //TODO - OPEN EMAIL ADDRESS IN EMAIL APP

    //TODO - SWITCH FOR GROUP CATEGORY

    //TODO - REGEX TO ACCEPT ONLY ALPHABETS AND NUMBERS AND SOME SYMBOLS
    //TODO - IF WORDS LESS THEN 200 CHANGE REMOTE VIEW TO NON EXPANDABLE


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationModel notificationModel = (NotificationModel) intent.getSerializableExtra("notificationModel");

        if (createNotification(notificationModel)) {

            Toast.makeText(this, "Added to Notify", Toast.LENGTH_SHORT).show();

            new NotificationDatabaseHandler(this).addNewNotification(notificationModel);
//
//                    RecyclerView recyclerView1 = findViewById(R.id.recyclerview_Home);
//                    recyclerView1.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//                    recyclerView1.setAdapter(new NotificationRecyclerAdapter(
//                            new NotificationDatabaseHandler(MainActivity.this)
//                                    .getActiveNotificationList(), MainActivity.this));

            stopSelf();
        } else {
            Toast.makeText(this, "NotificationService: Exception found", Toast.LENGTH_SHORT).show();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private boolean createNotification(NotificationModel model) {

        int notificationId = model.getNotificationId();
        String notificationSubText = model.getNotificationSubText();
        String notificationDate = model.getNotificationDate();
        String notificationTime = model.getNotificationTime();
        String notificationCategory = model.getNotificationCategory();
        String notificationTags = model.getNotificationTags();
        boolean isNotificationPinned = model.isNotificationPinned();


        //------------------------------------------------------------------------------------------
        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(false);
            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(CHANNEL_ID);
        }

        SharedPreferences sharedConfig = PreferenceManager.getDefaultSharedPreferences(this);
        String temporaryNotes = sharedConfig.getString(getString(R.string.temporary_notes_title), "never");

        if (!temporaryNotes.equals("never")) {
            mBuilder.setTimeoutAfter(Integer.parseInt(temporaryNotes));
        }

        //Constant values
        mBuilder.setSmallIcon(R.drawable.ic_notification_icon);
        mBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
        mBuilder.setSilent(true);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.setGroupSummary(false);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.build().flags = NotificationCompat.FLAG_NO_CLEAR;
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_SECRET);
        mBuilder.setDefaults(0);
        mBuilder.setColor(Color.parseColor("#EEEEEE"));
        mBuilder.setColorized(true);

        //User can change values
        mBuilder.setSubText("Change This Text");
        mBuilder.setAutoCancel(false);
        mBuilder.setOngoing(true);
        mBuilder.setCustomBigContentView(getRemoteView(notificationId, notificationSubText));

        try {
            notificationManager.notify(notificationId, mBuilder.build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private RemoteViews getRemoteView(int notificationId, String receivedText) {

        //TODO - MENTION IN UI MESSAGE IN NOTIFICATION IS LIMITED
        final String trimmedText = receivedText.length() > 300 ?
                receivedText.substring(0, 300) + "..." :
                receivedText;

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
//        remoteViews.setInt(R.id.linear_Container, "setBackgroundResource", R.color.color_StickyNote6); //notification color
        remoteViews.setTextViewText(R.id.txt_Body, trimmedText);
//        remoteViews.setTextViewTextSize(R.id.txt_Body, 1, 24); //change size if phone number
//        remoteViews.setTextColor(R.id.txt_Body, Color.parseColor("#0645AD")); //text color
        remoteViews.setOnClickPendingIntent(R.id.img_ActionCopy, getCopyToClipboardIntent(notificationId, receivedText));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionSearch, getSearchUrlIntent(receivedText));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionShare, getShareIntent(receivedText));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionMessage, getMessageIntent());
        remoteViews.setOnClickPendingIntent(R.id.img_ActionDialer, getDialerIntent(receivedText));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionMoreSettings, getOpenNotifyIntent(notificationId, receivedText));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionPinNotification, getPinNotificationIntent(notificationId, receivedText));

        return remoteViews;
    }

    private PendingIntent getPinNotificationIntent(int notificationId, String subText) {

        Intent intent = new Intent(this, NotificationActionReceiver.class);
        intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
        intent.putExtra(Constants.NOTIFICATION_SUB_TEXT, subText);
        intent.putExtra(Constants.NOTIFICATION_INTENT_ACTION, "pin");

        return PendingIntent.getBroadcast(this, new Utils().getNotificationId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getCopyToClipboardIntent(int notificationId, String subText) {

        Intent intent = new Intent(this, NotificationActionReceiver.class);
        intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
        intent.putExtra(Constants.NOTIFICATION_SUB_TEXT, subText);
        intent.putExtra(Constants.NOTIFICATION_INTENT_ACTION, Constants.ACTION_COPY);

        return PendingIntent.getBroadcast(this, new Utils().getNotificationId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getSearchUrlIntent(String url) {

//        Intent intent = new TextProcessor(this).isYoutubeUrl(url) ?
//                new Intent(Intent.ACTION_VIEW, Uri.parse(new TextProcessor(this).)) :
//                new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(intent);
//
//        return stackBuilder.getPendingIntent(new Utils().getNotificationId(), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        return null;
    }

    private PendingIntent getShareIntent(String textToShare) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, textToShare);

        //TODO - EXCLUDE THIS APP FROM SHARE SHEET
        //TODO - THIS FUNCTION DOESN'T WORK
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.putExtra(Intent.EXTRA_EXCLUDE_COMPONENTS,
//                    new ComponentName("com.ad.app.notify", "com.ad.app.notify.activity.TextReceiverActivity"));
//        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(Intent.createChooser(intent, null));

        return stackBuilder.getPendingIntent(new Utils().getNotificationId(), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getMessageIntent() {
        //TODO - OPEN GMAIL OR SMS MESSENGER
        return null;
    }

    private PendingIntent getDialerIntent(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        intent.setAction(String.valueOf(new Utils().getNotificationId()));
        return PendingIntent.getActivity(this, new Utils().getNotificationId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getOpenNotifyIntent(int notificationId, String subText) {

        Intent intent = new Intent(this, NotificationActionReceiver.class);
        intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
        intent.putExtra(Constants.NOTIFICATION_SUB_TEXT, subText);
        intent.putExtra(Constants.NOTIFICATION_INTENT_ACTION, Constants.ACTION_COPY);

        return PendingIntent.getBroadcast(this, new Utils().getNotificationId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

    }


}
