package com.ad.app.notify.utils;

import android.util.Patterns;

import java.util.regex.Matcher;

public class TextProcessor extends Constants {


    public boolean isPhoneNumber(String phoneNumber) {

        /*
            Valid phone numbers -

            "1234567890","123 456 7890", "(123) 456-7890", "+123 (456) 789-0123",
            "636 856 789", "+111 636 856 789", "636 85 67 89", "+111 636 85 67 89",
            "+1 1234567890123", "+12 123456789", "+123 123456"
         */

        return REGEX_PHONE_NUMBER_2.matcher(phoneNumber).matches() ||
                REGEX_PHONE_NUMBER_1.matcher(phoneNumber).matches() ||
                Patterns.PHONE.matcher(phoneNumber).matches();
    }

    public boolean isWebUrl(String webUrl) {
        return REGEX_WEB_URL.matcher(webUrl).matches() || Patterns.WEB_URL.matcher(webUrl).matches();
    }

    public boolean isEmailAddress(String emailAddress) {
        return Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches() || REGEX_EMAIL_ADDRESS.matcher(emailAddress).matches();

    }

    public boolean isYoutubeUrl(String url) {
        return url.matches("^(https?\\\\:\\\\/\\\\/)?((www\\\\.)?youtube\\\\.com|youtu\\\\.be)\\\\/.+$");
    }

    public String getIdFromUrl(String url) {

        Matcher matcher = REGEX_YOUTUBE_URL.matcher(url);
        if (matcher.find()) {
            return "https://www.youtube.com/watch?v=" + matcher.group(1);
        }

        return null;
    }

    public String getResult(String receivedText) {

        if (new TextProcessor().isWebUrl(receivedText)) {
            return WEB_URL;
        } else if (new TextProcessor().isPhoneNumber(receivedText)) {
            return PHONE_NUMBER;
        } else if (new TextProcessor().isEmailAddress(receivedText)) {
            return EMAIL_ADDRESS;
        } else {
            return STICKY_NOTE;
        }
    }
}
