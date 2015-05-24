package eus.ehu.dsiweb.ekain.eltenedor;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eus.ehu.dsiweb.ekain.eltenedor.entity.User;

public class Utils {

    public static void changeLocale(Fragment fragment) {
        changeLocale(fragment.getActivity());
    }

    public static void changeLocale(Activity activity) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        String language_code = pref.getString("option_language", "");
        changeLocale(activity, language_code);
    }

    public static void changeLocale(Fragment fragment, String language) {
        changeLocale(fragment.getActivity(), language);
    }

    public static void changeLocale(Activity activity, String language) {
        final Resources res = activity.getResources();
        final DisplayMetrics dm = res.getDisplayMetrics();
        final Configuration conf = res.getConfiguration();
        if (language == null || language.length() == 0) {
            conf.locale = Locale.getDefault();
        } else {
            final int idx = language.indexOf('-');
            if (idx != -1) {
                final String[] split = language.split("-");
                conf.locale = new Locale(split[0], split[1].substring(1));
            } else {
                conf.locale = new Locale(language);
            }
        }
        res.updateConfiguration(conf, dm);
    }


    private static Pattern pattern;
    private static Matcher matcher;
    //Email Pattern
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Validate Email with regular expression
     *
     * @param email
     * @return true for Valid Email and false for Invalid Email
     */
    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }
    /**
     * Checks for Null String object
     *
     * @param txt
     * @return true for not null and false for null String object
     */
    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }


    public static User obtainUser(JSONObject obj){
        User user = new User();
        try {
            user.setId(obj.getInt("id"));
            user.setName(obj.getString("name"));
            user.setLogin(obj.getString("login"));
            user.setEmail(obj.getString("email"));
        } catch (JSONException e) {

        }
        return user;
    }
}
