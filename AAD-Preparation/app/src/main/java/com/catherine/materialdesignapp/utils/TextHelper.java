package com.catherine.materialdesignapp.utils;

import android.util.Log;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHelper {
    public final static String TAG = TextHelper.class.getSimpleName();


    /**
     * @param input  e.g. tylrS wif/tz
     * @param target e.g. Taylor Swift
     * @return true/false
     */
    public static boolean matcher(String input, String target) {
//        return a.toUpperCase().trim().contains(b.toUpperCase().trim());


        StringBuffer sb = new StringBuffer();
        String a = removeAccents(input.replaceAll("\\s", ""));


        // (?i) ignore cases
        // (t)* 0-many t
        // \s* 0-many whitespace
        // E.g. (?i)(t)*\s*(a)*\s*
        sb.append("(?i)");
        for (char c : a.toCharArray()) {
            sb.append("(");
            sb.append(c);
            sb.append(")*\\s*");
        }

        Log.i(TAG, sb.toString());
        Pattern pattern = Pattern.compile(sb.toString());
        Matcher matcher = pattern.matcher(removeAccents(target));
        return matcher.matches();
    }

    /**
     * remove diacritics
     *
     * @param text
     * @return
     */
    public static String removeAccents(String text) {
        return text == null ? null : Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
