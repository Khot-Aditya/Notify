package com.ad.app.notify.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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
    }


    public void process(String string) {


        //create unique notification id

        List<NotificationModel> activeNotificationList =
                new NotificationDatabaseHandler(context).getActiveNotificationList();

        //set id to a random number
        int notificationId = new Utils().getNotificationId();
        if (activeNotificationList.size() != 0) {
            //if there are active notifications
            for (NotificationModel list : activeNotificationList) {
                //increment by 1 of last notification id
                notificationId = list.getNotificationId() + 1;
            }
        }

        StringBuilder stringBuilder = new StringBuilder("");

        //TODO - SET TAGS


        NotificationModel newNotificationModel = new NotificationModel();

        newNotificationModel.setNotificationId(notificationId);
        newNotificationModel.setNotificationDate(new Utils().getCurrentDate());
        newNotificationModel.setNotificationTime(new Utils().getCurrentTime());
        newNotificationModel.setNotificationSubText(string + "[" + notificationId + "]");
        newNotificationModel.setNotificationCategory(getCategory(string));
        newNotificationModel.setNotificationTags("sticky note - phone - email");  //TODO - SET TAGS AFTER TEXT PROCESSING
        newNotificationModel.setNotificationPinned(true);


        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra("notificationModel", newNotificationModel);
        context.startService(intent);
    }

    private String getCategory(String string) {

        String category;

        //check if received string contains any url
        String urlType = getUrlType(string);
        String emailType = getEmailType(string);
        String phoneNumberType = getPhoneNumberType(string);

        PreferenceManager.setDefaultValues(context, R.xml.root_preferences, false);

        SharedPreferences sharedConfig = PreferenceManager.getDefaultSharedPreferences(context);
        boolean attachPinByDefault = sharedConfig.getBoolean(context.getString(R.string.attach_pin_title), true);

        category = attachPinByDefault ?
                Constants.STICKY_NOTE :
                Constants.TEMPORARY_NOTE;

        if (!urlType.equals("0")) {
            //contains url

            switch (urlType){

                case Constants.YOUTUBE_URL:
                    return "Youtube";

                case Constants.NOTE_CONTAINING_YOUTUBE_URL:
                    //TODO
                    break;

                case Constants.WEB_URL:
                    //TODO
                    break;

                case Constants.NOTE_CONTAINING_WEB_URL:
                    //TODO
                    break;

                default:
                    //TODO
                    break;
            }
        }else{
            //TODO
        }

        if (!emailType.equals("0")) {
            //contains email address

            if(emailType.equals(Constants.EMAIL_ADDRESS)){

            }else if(emailType.equals(Constants.NOTE_CONTAINING_EMAIL_ADDRESS)){

            }else{

            }
        }else{

        }

        if (!phoneNumberType.equals("0")) {
            //phone number found

            if(phoneNumberType.equals(Constants.PHONE_NUMBER)){

            }

            if(phoneNumberType.equals(Constants.NOTE_CONTAINING_PHONE_NUMBER)){

            }else{

            }
        }else{

        }



        return category;
    }

    private String getPhoneNumberType(String string) {

        List<String> phoneNumberList = getPhoneNumbersFromString(string);
        if (phoneNumberList != null) {
            for (String s : phoneNumberList) {
                if (phoneNumberList.size() == 1) {
                    //single phone number found

                    if (s.length() == string.length()) {
                        //received string contains only phone number no other character
                        return Constants.PHONE_NUMBER;
                    } else {
                        //received string contains other characters with single phone number
                        return Constants.NOTE_CONTAINING_PHONE_NUMBER;
                    }
                } else {
                    //multiple phone numbers found
                    return Constants.NOTE_CONTAINING_PHONE_NUMBER;
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
                            Constants.EMAIL_ADDRESS :
                            Constants.NOTE_CONTAINING_EMAIL_ADDRESS;
                } else {
                    //received string contains multiple email addresses
                    return Constants.NOTE_CONTAINING_EMAIL_ADDRESS;
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
                                Constants.YOUTUBE_URL :
                                Constants.NOTE_CONTAINING_YOUTUBE_URL;
                    } else {
                        //when received url is not of youtube

                        //if only url is received not any other characters
                        return s.length() == string.length() ?
                                Constants.WEB_URL :
                                Constants.NOTE_CONTAINING_WEB_URL;
                    }
                } else {
                    //if multiple urls are found

                    //check if url is of youtube or other websites
                    return isYoutubeUrl(s) ?
                            Constants.NOTE_CONTAINING_YOUTUBE_URL :
                            Constants.NOTE_CONTAINING_WEB_URL;
                }
            }
        } else {
            //return 0 when no url found
            return "0";
        }

        return "0";
    }

    public static boolean isYoutubeUrl(String youTubeURl) {

        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";

        return !youTubeURl.isEmpty() && youTubeURl.matches(pattern);
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

        String s;

//        if (isYoutubeUrl(url)) {
//            Matcher matcher = REGEX_YOUTUBE_URL.matcher(url);
//            if (matcher.find()) {
//                s = "https://www.youtube.com/watch?v=" + matcher.group(1);
//            }
//        } else {
//            s = url;
//        }


        return null;

    }


}
