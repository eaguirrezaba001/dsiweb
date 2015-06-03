package eus.ehu.dsiweb.ekain.eltenedor;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.DisplayMetrics;

import eus.ehu.dsiweb.Utility;
import eus.ehu.dsiweb.entity.DBRestaurant;
import eus.ehu.dsiweb.entity.IEntityConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils extends Utility {

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

    public static Locale getLocale(Context context){
        final Resources res = context.getResources();
        final DisplayMetrics dm = res.getDisplayMetrics();
        final Configuration conf = res.getConfiguration();
        return conf.locale;
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
    public static boolean validEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }
    public static boolean validPassword(String passwd1, String passwd2) {
        return passwd1.equals(passwd2);

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

    public static Bitmap obtainLogoImage(byte[] data) {
        try{
            if(data!=null){
                // Create object of bitmapfactory's option method for further option use
                BitmapFactory.Options options=new BitmapFactory.Options();
                // inPurgeable is used to free up memory while required
                options.inPurgeable = true;
                //Decode image
                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                // convert decoded bitmap into well scalled Bitmap format.
                bm = Bitmap.createScaledBitmap(bm, 100 , 50 , true);
                return bm;
            }
        } catch(Throwable th){
            th.printStackTrace();
        }
        return null;
    }

   public static DBRestaurant obtainRestaurant(JSONObject obj) throws JSONException {
        DBRestaurant restaurant = new DBRestaurant();
        restaurant.setId(obj.getInt(IEntityConstants.ID));
        restaurant.setName(obj.getString(IEntityConstants.NAME));
        restaurant.setDescription(obj.getString(IEntityConstants.DESCRIPTION));
        restaurant.setTableCount(obj.getInt(IEntityConstants.TABLE_COUNT));
        restaurant.setLongitude(obj.getString(IEntityConstants.LONGITUDE));
        restaurant.setLatitude(obj.getString(IEntityConstants.LATITUDE));
        try{
            String logo = obj.getString(IEntityConstants.LOGO_IMAGE);
            restaurant.setLogoImage(logo != null ? Base64.decode(logo, 0) : "".toString().getBytes());
        } catch(Throwable th){
            restaurant.setLogoImage("".toString().getBytes());
        }

        return restaurant;
    }


}
