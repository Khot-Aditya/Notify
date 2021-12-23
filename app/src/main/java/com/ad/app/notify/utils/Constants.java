package com.ad.app.notify.utils;

import java.util.regex.Pattern;

public class Constants {

    public static final String CHANNEL_ID = "Notify Service";
    public static final String CHANNEL_NAME = "Notify Service";


    public static final String NOTIFICATION_ID = "com.ad.app.notify.Constants.NOTIFICATION_ID";
    public static final String NOTIFICATION_SUB_TEXT = "com.ad.app.notify.Constants.NOTIFICATION_SUB_TEXT";
    public static final String NOTIFICATION_CATEGORY = "com.ad.app.notify.Constants.NOTIFICATION_CATEGORY";
    public static final String NOTIFICATION_INTENT_ACTION = "com.ad.app.notify.Constants.NOTIFICATION_INTENT_ACTION";

    public static final String ACTION_COPY = "com.ad.app.notify.Constants.ACTION_COPY";
    public static final String ACTION_SEARCH = "com.ad.app.notify.Constants.ACTION_SEARCH";
    public static final String ACTION_SHARE = "com.ad.app.notify.Constants.ACTION_SHARE";
    public static final String ACTION_MESSAGE = "com.ad.app.notify.Constants.ACTION_MESSAGE";
    public static final String ACTION_DIALER = "com.ad.app.notify.Constants.ACTION_DIALER";
    public static final String ACTION_OPEN_EDITOR = "com.ad.app.notify.Constants.ACTION_OPEN_EDITOR";
    public static final String ACTION_ATTACH_PIN = "com.ad.app.notify.Constants.ACTION_ATTACH_PIN";
    public static final String ACTION_REMOVE_PIN = "com.ad.app.notify.Constants.ACTION_REMOVE_PIN";


    //tags
    //TODO - EXTRACT STRING FROM RESOURCE
    public static final String TAG_WATCH_LATER = "Watch Later"; //WATCH LATER TAG IF ONLY YOUTUBE LINK IS RECEIVED
    public static final String URL = "Url"; //if any contains any url including youtube
    public static final String PHONE = "Mobile Number";
    public static final String EMAIL = "Email";
    public static final String All = "All";
    public static final String PLAIN_TEXT = "Plain Text";


    /*
    * TODO -
    *   LAYOUTS
    *   1. YOUTUBE WATCH LATER LAYOUT
    *   2. WEB LINK
    *   3. PHONE NUMBER
    *   4. EMAIL ADDRESS
    *   5. PLAIN TEXT
    *   6. CONTAIN DIFFERENT LINKS
    *   7. CONTAIN DIFFERENT CONTACTS
    *   8. CONTAIN ALL TAGS
    */


    public static final String WEB_URL = "com.ad.app.notify.Constants.WEB_URL";
    public static final String YOUTUBE_URL = "com.ad.app.notify.Constants.YOUTUBE_URL";
    public static final String PHONE_NUMBER = "com.ad.app.notify.Constants.PHONE_NUMBER";
    public static final String EMAIL_ADDRESS = "com.ad.app.notify.Constants.EMAIL_ADDRESS";
    public static final String STICKY_NOTE = "com.ad.app.notify.Constants.STICKY_NOTE";
    public static final String TEMPORARY_NOTE = "com.ad.app.notify.Constants.TEMPORARY_NOTE";

    public static final String NOTE_CONTAINING_WEB_URL = "com.ad.app.notify.Constants.NOTE_CONTAINING_WEB_URL";
    public static final String NOTE_CONTAINING_YOUTUBE_URL = "com.ad.app.notify.Constants.NOTE_CONTAINING_YOUTUBE_URL";
    public static final String NOTE_CONTAINING_PHONE_NUMBER = "com.ad.app.notify.Constants.NOTE_CONTAINING_PHONE_NUMBER";
    public static final String NOTE_CONTAINING_EMAIL_ADDRESS = "com.ad.app.notify.Constants.NOTE_CONTAINING_EMAIL_ADDRESS";




    //TODO - USE THIS REGEX IF OTHERS FAIL
//    public static final Pattern REGEX_PHONE_NUMBER_1 =
//            Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
//
//    public static final Pattern REGEX_PHONE_NUMBER_2 =
//            Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
//                    + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
//                    + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$");
//
//    public static final Pattern REGEX_WEB_URL =
//            Pattern.compile("(?i)^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\." +
//                    "\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2" +
//                    "\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1" +
//                    "?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))" +
//                    "|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a" +
//                    "1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))" +
//                    "\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?$");
//
//    public static final Pattern REGEX_EMAIL_ADDRESS =
//            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
//
//    public static final Pattern REGEX_YOUTUBE_URL = Pattern.compile("http(?:s)?:\\/\\/(?:m.)" +
//            "?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|" +
//            "embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)", Pattern.CASE_INSENSITIVE);


    //DATABASE TABLE CONSTANTS
    public static final String TABLE_NOTIFICATION = "table_notification";
    public static final String COL_ID = "col_id";
    public static final String COL_DATE = "col_date";
    public static final String COL_TIME = "col_time";
    public static final String COL_SUB_TEXT = "col_sub_text";
    public static final String COL_CATEGORY = "col_category";
    public static final String COL_TAGS = "col_tags";
    public static final String COL_IS_PINNED = "col_is_pinned";
}
