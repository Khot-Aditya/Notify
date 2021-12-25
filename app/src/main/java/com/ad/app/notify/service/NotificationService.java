package com.ad.app.notify.service;

import static com.ad.app.notify.utils.Constants.CHANNEL_ID;
import static com.ad.app.notify.utils.Constants.CHANNEL_NAME;
import static com.ad.app.notify.utils.Constants.NOTIFICATION_MODEL;
import static com.ad.app.notify.utils.Constants.TAG_WATCH_LATER;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
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
import com.ad.app.notify.activities.EditorActivity;
import com.ad.app.notify.activities.MainActivity;
import com.ad.app.notify.database.NotificationDatabaseHandler;
import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.receiver.NotificationActionReceiver;
import com.ad.app.notify.utils.Constants;
import com.ad.app.notify.utils.TextProcessor;
import com.ad.app.notify.utils.Utils;

public class NotificationService extends Service {
    private static final String TAG = "NotificationService";


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

//    if (new NotificationDatabaseHandler(this).addNewNotification(notificationModel))
//            Toast.makeText(this, "Added to Notify", Toast.LENGTH_SHORT).show();
//                    else
//                            Toast.makeText(this, "NotificationService: Exception found", Toast.LENGTH_SHORT).show();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationModel notificationModel = (NotificationModel) intent.getSerializableExtra(NOTIFICATION_MODEL);

        Toast.makeText(this,
                intent.hasExtra(Constants.ACTION) ?
                        createNotification(notificationModel) ?
                                intent.getStringExtra(Constants.ACTION).equals(Constants.ACTION_ADD) ?
                                        new NotificationDatabaseHandler(this)
                                                .addNewNotification(notificationModel) ?
                                                "Added to notify" : "addNewNotification() : Exception Found" :
                                        new NotificationDatabaseHandler(this)
                                                .updateExistingNotification(notificationModel) ?
                                                "Updated" : "updateExistingNotification() : Exception Found" :
                                "createNotification() : Exception Found" :
                        "hasExtra() : Exception Found", Toast.LENGTH_SHORT).show();

        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean createNotification(NotificationModel model) {

        SharedPreferences sharedConfig = PreferenceManager.getDefaultSharedPreferences(this);

        //gets milli seconds value saved in array
        String temporaryNotes = sharedConfig.getString(getString(R.string.temporary_notes_title), "never");


        //Notification Builder ---------------------------------------------------------------------
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


        //Constant values --------------------------------------------------------------------------
        mBuilder.setSmallIcon(R.drawable.ic_notification_icon);
        mBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
        mBuilder.setSilent(true);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.build().flags = NotificationCompat.FLAG_NO_CLEAR;
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_SECRET);
        mBuilder.setDefaults(0);
        mBuilder.setColor(Color.parseColor("#EEEEEE"));
        mBuilder.setColorized(true);
        mBuilder.setSubText(model.getNotificationTags());


        //User can change values -------------------------------------------------------------------
        if (!temporaryNotes.equals("never"))
            mBuilder.setTimeoutAfter(Integer.parseInt(temporaryNotes));

        //if is pinned then se auto cancel false
        mBuilder.setAutoCancel(!model.isNotificationPinned());
        mBuilder.setOngoing(model.isNotificationPinned());


        //TODO - ADD OTHER REMOTE VIEWS
        if (model.getNotificationCategory().equals(TAG_WATCH_LATER)) {
            mBuilder.setCustomContentView(getWatchLaterRemoteView(model));
        } else {
            mBuilder.setCustomBigContentView(getFullRemoteViews(model));
        }

        try {
            notificationManager.notify(model.getNotificationId(), mBuilder.build());
            return true;
        } catch (Exception e) {
            //TODO - LOG ERROR
            return false;
        }
    }

    private RemoteViews getWatchLaterRemoteView(NotificationModel model) {

        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.layout_notification_watch_later);

        remoteViews.setTextViewText(R.id.txt_SubText_WatchLater, model.getNotificationSubText());
        remoteViews.setTextViewText(R.id.txt_Time_WatchLater,
                model.getNotificationTime() + " • " + model.getNotificationDate());
        remoteViews.setImageViewResource(R.id.ic_Pin_Watchlater, model.isNotificationPinned() ?
                R.drawable.ic_pinned :
                R.drawable.ic_unpinned);

        remoteViews.setOnClickPendingIntent(R.id.ic_Pin_Watchlater, getPinNotificationIntent(model));
        remoteViews.setOnClickPendingIntent(R.id.txt_SubText_WatchLater,
                getSearchUrlIntent(model.getNotificationSubText()));
        return remoteViews;
    }

    private RemoteViews getFullRemoteViews(NotificationModel model) {

        String receivedText = model.getNotificationSubText();

        //TODO - MENTION IN UI, MESSAGE IN NOTIFICATION IS LIMITED
        final String trimmedText = receivedText.length() > 300 ?
                receivedText.substring(0, 300) + "..." :
                receivedText;

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification_full);
        remoteViews.setTextViewText(R.id.txt_Body, trimmedText);
        remoteViews.setImageViewResource(R.id.img_ActionPinNotification,
                model.isNotificationPinned() ?
                        R.drawable.ic_pinned :
                        R.drawable.ic_unpinned);

        remoteViews.setOnClickPendingIntent(R.id.img_ActionCopy, getCopyToClipboardIntent(model));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionSearch, getSearchUrlIntent(receivedText));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionShare, getShareIntent(receivedText));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionMessage, getMessageIntent());
        remoteViews.setOnClickPendingIntent(R.id.img_ActionDialer, getDialerIntent(receivedText));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionEdit, getEditIntent(model));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionPinNotification, getPinNotificationIntent(model));

        return remoteViews;
    }

//    private RemoteViews getRemoteView(NotificationModel model) {
//
//        int notificationId = model.getNotificationId();
//        String receivedText = model.getNotificationSubText();
//
//        //TODO - MENTION IN UI MESSAGE IN NOTIFICATION IS LIMITED
//        final String trimmedText = receivedText.length() > 300 ?
//                receivedText.substring(0, 300) + "..." :
//                receivedText;
//
//        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification_full);
////        remoteViews.setInt(R.id.linear_Container, "setBackgroundResource", R.color.color_StickyNote6); //notification color
//        remoteViews.setTextViewText(R.id.txt_Body, trimmedText);
////        remoteViews.setTextViewTextSize(R.id.txt_Body, 1, 24); //change size if phone number
////        remoteViews.setTextColor(R.id.txt_Body, Color.parseColor("#0645AD")); //text color
//        remoteViews.setOnClickPendingIntent(R.id.img_ActionCopy, getCopyToClipboardIntent(notificationId, receivedText));
//        remoteViews.setOnClickPendingIntent(R.id.img_ActionSearch, getSearchUrlIntent(receivedText));
//        remoteViews.setOnClickPendingIntent(R.id.img_ActionShare, getShareIntent(receivedText));
//        remoteViews.setOnClickPendingIntent(R.id.img_ActionMessage, getMessageIntent());
//        remoteViews.setOnClickPendingIntent(R.id.img_ActionDialer, getDialerIntent(receivedText));
//        remoteViews.setOnClickPendingIntent(R.id.img_ActionMoreSettings, getOpenNotifyIntent(notificationId, receivedText));
//        remoteViews.setOnClickPendingIntent(R.id.img_ActionPinNotification, getPinNotificationIntent(model));
//
//        return remoteViews;
//    }

    private PendingIntent getPinNotificationIntent(NotificationModel model) {

        return PendingIntent.getBroadcast(this,
                new Utils().getNotificationId(),
                new Intent(this, NotificationActionReceiver.class)
                        .putExtra(NOTIFICATION_MODEL, model)
                        .putExtra(Constants.NOTIFICATION_INTENT_ACTION,
                                model.isNotificationPinned() ?
                                        Constants.ACTION_REMOVE_PIN :
                                        Constants.ACTION_ATTACH_PIN),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getCopyToClipboardIntent(NotificationModel model) {

        return PendingIntent.getBroadcast(this, new Utils().getNotificationId(),
                new Intent(this, NotificationActionReceiver.class)
                        .putExtra(NOTIFICATION_MODEL, model)
                        .putExtra(Constants.NOTIFICATION_INTENT_ACTION,
                                Constants.ACTION_COPY),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getSearchUrlIntent(String url) {

        Intent intent;

        String extractedUrl = new TextProcessor(this).getSearchableUrl(url);

        if (new TextProcessor(this).isYoutubeUrl(extractedUrl)) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        } else {
            intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, extractedUrl); // query contains search string
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return TaskStackBuilder.create(this)
                .addParentStack(MainActivity.class)
                .addNextIntent(intent)
                .getPendingIntent(new Utils().getNotificationId(),
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getShareIntent(String textToShare) {

        Intent intent = new Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, textToShare);

        //TODO - EXCLUDE THIS APP FROM SHARE SHEET
        //TODO - THIS FUNCTION DOESN'T WORK
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.putExtra(Intent.EXTRA_EXCLUDE_COMPONENTS,
//                    new ComponentName("com.ad.app.notify", "com.ad.app.notify.activity.TextReceiverActivity"));
//        }

        return TaskStackBuilder.create(this)
                .addParentStack(MainActivity.class)
                .addNextIntent(Intent.createChooser(intent, null))
                .getPendingIntent(new Utils().getNotificationId(),
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getMessageIntent() {
        //TODO - OPEN GMAIL OR SMS MESSENGER
        return null;
    }

    private PendingIntent getDialerIntent(String phoneNumber) {

        return PendingIntent.getActivity(this, new Utils().getNotificationId(),
                new Intent(Intent.ACTION_DIAL)
                        .setData(Uri.parse("tel:" + phoneNumber))
                        .setAction(String.valueOf(new Utils().getNotificationId())),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getEditIntent(NotificationModel model) {

        return PendingIntent.getActivity(this, new Utils().getNotificationId(),
                new Intent(this, EditorActivity.class)
                        .putExtra(NOTIFICATION_MODEL, model)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

    }


}
