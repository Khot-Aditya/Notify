package com.ad.app.notify.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.utils.Constants;

import java.util.ArrayList;
import java.util.List;


public class NotificationDatabaseHandler extends SQLiteOpenHelper {

    public static final String TAG = "NotificationDatabaseHandler";

    public NotificationDatabaseHandler(@Nullable Context context) {
        super(context, "com.ad.app.notify.NotificationDatabaseHandler", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + Constants.TABLE_NOTIFICATION + " ( " +
                Constants.COL_ID + " INTEGER" + " , " +
                Constants.COL_DATE + " TEXT" + " , " +
                Constants.COL_TIME + " TEXT" + " , " +
                Constants.COL_SUB_TEXT + " TEXT" + " , " +
                Constants.COL_CATEGORY + " TEXT" + " , " +
                Constants.COL_TAGS + " TEXT" + " , " +
                Constants.COL_IS_PINNED + " BOOL" + " , " +
                Constants.COL_BG_COLOR + " INTEGER" + " )";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean updateExistingNotification(NotificationModel notificationModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int notificationId = notificationModel.getNotificationId();
        contentValues.put(Constants.COL_ID, notificationId);
        contentValues.put(Constants.COL_DATE, notificationModel.getNotificationDate());
        contentValues.put(Constants.COL_TIME, notificationModel.getNotificationTime());
        contentValues.put(Constants.COL_SUB_TEXT, notificationModel.getNotificationSubText());
        contentValues.put(Constants.COL_CATEGORY, notificationModel.getNotificationCategory());
        contentValues.put(Constants.COL_TAGS, notificationModel.getNotificationTags());
        contentValues.put(Constants.COL_IS_PINNED, notificationModel.isNotificationPinned());
        contentValues.put(Constants.COL_BG_COLOR, notificationModel.getNotificationBgColor());

        long insert = database.update(Constants.TABLE_NOTIFICATION, contentValues, Constants.COL_ID + " = " + notificationId, null);

        database.close();
        return insert != -1;
    }


    public boolean deleteNotification(int notificationId) {
        SQLiteDatabase database = this.getWritableDatabase();

        int delete = database.delete(Constants.TABLE_NOTIFICATION, Constants.COL_ID + " = " + notificationId, null);

        database.close();
        return delete != -1;
    }

    public boolean deleteAllNotifications() {
        SQLiteDatabase database = this.getWritableDatabase();

        int delete = database.delete(Constants.TABLE_NOTIFICATION, null, null);

        database.close();
        return delete != -1;
    }

    public boolean addNewNotification(NotificationModel notificationModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.COL_ID, notificationModel.getNotificationId());
        contentValues.put(Constants.COL_DATE, notificationModel.getNotificationDate());
        contentValues.put(Constants.COL_TIME, notificationModel.getNotificationTime());
        contentValues.put(Constants.COL_SUB_TEXT, notificationModel.getNotificationSubText());
        contentValues.put(Constants.COL_CATEGORY, notificationModel.getNotificationCategory());
        contentValues.put(Constants.COL_TAGS, notificationModel.getNotificationTags());
        contentValues.put(Constants.COL_IS_PINNED, notificationModel.isNotificationPinned());
        contentValues.put(Constants.COL_BG_COLOR, notificationModel.getNotificationBgColor());

        long insert = database.insert(Constants.TABLE_NOTIFICATION, null, contentValues);

        database.close();
        return insert != -1;

    }


    public List<NotificationModel> getActiveNotificationList(boolean recentlyAdded) {
        return getDataFromDatabase(recentlyAdded, "SELECT * FROM " + Constants.TABLE_NOTIFICATION);
    }


    private List<NotificationModel> getDataFromDatabase(boolean recentlyAdded, String queryString) {

        SQLiteDatabase database = this.getReadableDatabase();

        List<NotificationModel> itemList = new ArrayList<>();

        Cursor cursor = database.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {

            do {
                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                String time = cursor.getString(2);
                String subText = cursor.getString(3);
                String category = cursor.getString(4);
                String tags = cursor.getString(5);
                int isPinned = cursor.getInt(6);
                int backgroundColor = cursor.getInt(7);

                NotificationModel notificationModel = new NotificationModel();

                try {
                    notificationModel.setNotificationId(id);
                    notificationModel.setNotificationDate(date);
                    notificationModel.setNotificationTime(time);
                    notificationModel.setNotificationSubText(subText);
                    notificationModel.setNotificationCategory(category);
                    notificationModel.setNotificationTags(tags);
                    notificationModel.setNotificationPinned(isPinned == 1);
                    notificationModel.setNotificationBgColor(backgroundColor);

                } catch (Exception e) {
                    Log.d(TAG, "getList: " + e.getLocalizedMessage());
                }

                if (recentlyAdded) {
                    itemList.add(0, notificationModel);
                } else {
                    itemList.add(notificationModel);
                }


            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return itemList;
    }

    public int getItemCount() {
        String countQuery = "SELECT  * FROM " + Constants.TABLE_NOTIFICATION;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        database.close();
        return count;
    }
}
