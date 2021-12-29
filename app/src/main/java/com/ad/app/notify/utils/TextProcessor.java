package com.ad.app.notify.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.ad.app.notify.R;
import com.ad.app.notify.database.NotificationDatabaseHandler;
import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.service.NotificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextProcessor extends Constants {

    private Context context;
    private boolean attachPinByDefault;

    //TODO - IMPROVE REGEX
    private static final Pattern REGEX_PHONE_NUMBER_1 =
            Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");

    private static final Pattern REGEX_PHONE_NUMBER_2 =
            Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                    + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                    + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$", Pattern.CASE_INSENSITIVE);


    private static final Pattern REGEX_EMAIL_ADDRESS =
            Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+", Pattern.CASE_INSENSITIVE);


    public TextProcessor(Context context) {
        this.context = context;

        SharedPreferences sharedConfig = PreferenceManager.getDefaultSharedPreferences(context);
        attachPinByDefault = sharedConfig.getBoolean(context.getString(R.string.attach_pin_title), true);
    }


    public void process(String string, String action) {

        List<NotificationModel> activeNotificationList =
                new NotificationDatabaseHandler(context).getActiveNotificationList(false);

        //create unique notification id
        //set id to a random number
        int notificationId = new Utils().getNotificationId();
        if (activeNotificationList.size() != 0) {
            //if there are active notifications
            for (NotificationModel list : activeNotificationList) {
                //increment by 1 of last notification id
                notificationId = list.getNotificationId() + 1;
            }
        }


        String category = getCategory(string);
        String tags = getTags(category);

        NotificationModel newNotificationModel = new NotificationModel();

        newNotificationModel.setNotificationId(notificationId);
        newNotificationModel.setNotificationDate(new Utils().getCurrentDate());
        newNotificationModel.setNotificationTime(new Utils().getCurrentTime());
        newNotificationModel.setNotificationSubText(string);
        newNotificationModel.setNotificationCategory(category);
        newNotificationModel.setNotificationTags(tags);
        newNotificationModel.setNotificationPinned(attachPinByDefault);

        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra(NOTIFICATION_MODEL, newNotificationModel);
        intent.putExtra(Constants.ACTION,action);
        context.startService(intent);
    }

    private String getTags(String string) {

        switch (string) {

            case TAG_URL:
                return "Web Url";

            case TAG_EMAIL:
                return "Email";

            case TAG_PHONE_NUMBER:
                return "Phone Number";

            case TAG_WATCH_LATER:
                return "Youtube Watch Later";

            case TAG_NOTE:
                return attachPinByDefault ? "Sticky Note" : "Temporary Note";
        }
        return null;
    }

    private String getCategory(String string) {

        String urlType = getUrlType(string);
        String emailType = getEmailType(string);
        String phoneNumberType = getPhoneNumberType(string);

        if (!urlType.equals("0")) return urlType;
        if (!emailType.equals("0")) return emailType;
        if (!phoneNumberType.equals("0")) return phoneNumberType;

        return Constants.TAG_NOTE;
    }

    private String getPhoneNumberType(String string) {

        List<String> phoneNumberList = getPhoneNumbersFromString(string);
        if (phoneNumberList != null) {
            for (String s : phoneNumberList) {
                if (phoneNumberList.size() == 1) {
                    //single phone number found

                    if (s.length() == string.length()) {
                        //received string contains only phone number no other character
                        return Constants.TAG_PHONE_NUMBER;
                    } else {
                        //received string contains other characters with single phone number
                        return Constants.TAG_NOTE;
                    }
                } else {
                    //multiple phone numbers found
                    return Constants.TAG_NOTE;
                }
            }
        } else {
            //return 0 when no phone number found in received string
            return "0";
        }

        return "0";
    }

    private String getEmailType(String string) {

        List<String> emailList = getEmailFromString(string);
        if (emailList != null) {

            for (String s : emailList) {
                if (emailList.size() == 1) {
                    //one email address found

                    //check if received text only contains email address and not any other character
                    return s.length() == string.length() ?
                            Constants.TAG_EMAIL :
                            Constants.TAG_NOTE;
                } else {
                    //received string contains multiple email addresses
                    return Constants.TAG_NOTE;
                }
            }
        } else {
            //return 0 when no email found in received string
            return "0";
        }

        return "0";
    }

    private String getUrlType(String string) {
        //TODO - IN DEVELOPMENT
        List<String> list = getUrlFromString(string);
        if (list != null) {
            //continue if at least one url found
            for (String s : list) {
                //loop though all urls
                if (list.size() == 1) {
                    //when only one url if found
                    if (isYoutubeUrl(s)) {
                        //if one url found is of youtube

                        //if only url is received not any other characters
                        return s.length() == string.length() ?
                                Constants.TAG_WATCH_LATER :
                                Constants.TAG_NOTE;
                    } else {
                        //when received url is not of youtube

                        //if only url is received not any other characters
                        return s.length() == string.length() ?
                                Constants.TAG_URL :
                                Constants.TAG_NOTE;
                    }
                } else {
                    //if multiple urls are found

                    //check if url is of youtube or other websites
                    return isYoutubeUrl(s) ?
                            Constants.TAG_NOTE :
                            Constants.TAG_NOTE;
                }
            }
        } else {
            //return 0 when no url found
            return "0";
        }

        return "0";
    }

    public boolean isYoutubeUrl(String url) {

        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";

        return !url.isEmpty() && url.matches(pattern);
    }

    private List<String> getUrlFromString(String string) {
        //TODO - REGEX DOES NOT RECOGNISE TWO URLS IN SINGLE LINE DIVIDED BY SPACE
        //Ex. (https://www.something.com https://www.something.com)


        List<String> urlList = new ArrayList<>();
        String regex
                = "\\b((?:https?|ftp|file):"
                + "//[-a-zA-Z0-9+&@#/%?="
                + "~_|!:, .;]*[-a-zA-Z0-9+"
                + "&@#/%=~_|])";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            urlList.add(matcher.group());
        }
        return urlList.size() == 0 ? null : urlList;
    }

    private List<String> getPhoneNumbersFromString(String string) {

        List<String> phoneNumberList = new ArrayList<>();
//        String regex = "/^(\\+\\d{1,3}[- ]?)?\\d{10}$/";
//
//        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = REGEX_PHONE_NUMBER_2.matcher(string);

        while (matcher.find()) {
            phoneNumberList.add(matcher.group());
        }

        return phoneNumberList.size() == 0 ? null : phoneNumberList;
    }

    private List<String> getEmailFromString(String string) {

        List<String> emailList = new ArrayList<>();
//        String regex = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";
//
//        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = REGEX_EMAIL_ADDRESS.matcher(string);

        while (matcher.find()) {
            emailList.add(matcher.group());
        }

        return emailList.size() == 0 ? null : emailList;
    }


    //TODO - DEVELOP THIS FUNCTION
    public String getSearchableUrl(String url) {


        if (isYoutubeUrl(url)) {

            Pattern pattern = Pattern.compile(
                    "^https?://.*(?:youtu.be/|v/|u/\\\\w/|embed/|watch?v=)([^#&?]*).*$",
                    Pattern.CASE_INSENSITIVE);

            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return "https://www.youtube.com/watch?v=" + matcher.group(1);
            }
        } else {

            List<String> urls = getUrlFromString(url);

            if (urls != null) {
                //TODO SHOW TRANSPARENT ACTIVITY TO CHOOSE OPTIONS
//                Toast.makeText(context, urls.get(0), Toast.LENGTH_SHORT).show();
            }
        }

        return url;
    }


}
