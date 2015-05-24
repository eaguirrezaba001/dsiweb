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

public class Session {

    private static Session session;
    private User loggedUser;

    private Session(){
    }

    public static Session getInstance(){
        if(session==null){
            session = new Session();
        }
        return session;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
}
