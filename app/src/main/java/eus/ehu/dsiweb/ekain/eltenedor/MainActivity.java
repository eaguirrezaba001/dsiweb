package eus.ehu.dsiweb.ekain.eltenedor;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
//import android.support.v4.app.ActionBarDrawerToggle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private DrawerLayout drawerLayout;
    private LinearLayout drawerList;
    private ActionBarDrawerToggle drawerToggle;
    FragmentLeftPanel leftPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load language from the preferences
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String language_code = pref.getString("option_language", "es");
        Utils.changeLocale(this, language_code);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnectedViaWifi();

        loadLeftDrawerToggle();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        // Load init fragment
        ft.replace(R.id.content_frame, new FragmentRestaurantList());
        // Load left panel fragment
        Fragment leftPanel = new FragmentLeftPanel();
        ft.replace(R.id.left_panel_frame, leftPanel);
        ft.commit();

    }

    private void checkConnectedViaWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(!mWifi.isConnected()){
            new AlertDialog.Builder(this)
                    .setMessage("Esta es una version de prueba, el Wifi debe estar conectado. ¿Activarlo ahora?")
                    .setCancelable(false)
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
    }


    private void loadLeftDrawerToggle(){
        final CharSequence tituloSeccion;
        final CharSequence tituloApp;

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (LinearLayout) findViewById(R.id.left_drawer);

        tituloSeccion = getTitle();
        tituloApp = getTitle();
        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(tituloSeccion);
                ActivityCompat.invalidateOptionsMenu(MainActivity.this);
            }
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(tituloApp);
                ActivityCompat.invalidateOptionsMenu(MainActivity.this);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }

    public void closeDrawerLayout(){
        drawerLayout.closeDrawers();
    }

    public void openDrawerLayout(){
        drawerLayout.openDrawer(0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    public void restoreMainFragment(View view){
        FragmentTransaction ft1 = getFragmentManager().beginTransaction();
        ft1.replace(R.id.content_frame, new FragmentRestaurantList());
        ft1.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.content_frame);
        if(fragment instanceof FragmentRestaurantList){
            new AlertDialog.Builder(this)
                    .setMessage(R.string.exit_yes_no)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        } else {
            restoreMainFragment(null);
        }
    }

}