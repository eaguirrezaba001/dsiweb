package eus.ehu.dsiweb.ekain.eltenedor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;

import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class FragmentConfig extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_config, container, false);
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences arg0, String key) {
        ListPreference lp = (ListPreference) findPreference("option_language");
        lp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // Toast.makeText(PreferencesActivity.this, "second", Toast.LENGTH_LONG).show();
                changeLocale(newValue.toString().toLowerCase());
                return true;
            }
        });
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        //loadLocale();
        super.onConfigurationChanged(newConfig);
    }

    private void changeLocale(String locale){
        Utils.changeLocale(this, locale);
    }
    
}