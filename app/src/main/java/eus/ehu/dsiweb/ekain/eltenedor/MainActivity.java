package eus.ehu.dsiweb.ekain.eltenedor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
//import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;

import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
//import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {

    private DrawerLayout drawerLayout;
    private LinearLayout drawerList;
    private ActionBarDrawerToggle drawerToggle;


    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object
    EditText emailET;
    // Passwprd Edit View Object
    EditText pwdET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load language from the preferences
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String language_code = pref.getString("option_language", "es");
        Utils.changeLocale(this, language_code);

        super.onCreate(savedInstanceState);

        // Load Facebook login
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Load Twitter login
        //TwitterAuthConfig authConfig = new TwitterAuthConfig("consumerKey", "consumerSecret");
        //Fabric.with(this, new TwitterCore(authConfig));

        setContentView(R.layout.activity_main);

        loadLoginButtons();

        loadMoreActions();

        loadLeftLayoutContent();

        // Load init fragment
        FragmentTransaction ft1 = getFragmentManager().beginTransaction();
        ft1.replace(R.id.content_frame, new FragmentMain());
        ft1.commit();
    }


    private void loadLoginButtons() {

        //if(Session.getInstance().getLoggedUser()==null
        //        || Session.getInstance().getLoggedUser().getId()==null
        //        || Session.getInstance().getLoggedUser().getId()<=0){
            // Find Error Msg Text View control by ID
            errorMsg = (TextView)findViewById(R.id.login_error);
            // Find Email Edit View control by ID
            emailET = (EditText)findViewById(R.id.TxtLoginUserName);
            // Find Password Edit View control by ID
            pwdET = (EditText)findViewById(R.id.TxtLoginUserPassword);
            // Instantiate Progress Dialog object
            prgDialog = new ProgressDialog(this);
            // Set Progress Dialog Text
            prgDialog.setMessage("Please wait...");
            // Set Cancelable as False
            prgDialog.setCancelable(false);

            // other login options
            LoginButton facebookLoginButton;
            CallbackManager callbackManager;
            //TwitterLoginButton twitterLoginButton;

            // FACEBOOK
            facebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
            callbackManager = CallbackManager.Factory.create();
            facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                }
            });
            // TWITTER
           /*
            twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
            twitterLoginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    // Do something with result, which provides a
                    // TwitterSession for making API calls
                }

                @Override
                public void failure(TwitterException exception) {
                    // Do something on failure
                }
            });
            */
/*
        } else {
            emailET.setVisibility(View.GONE);
            pwdET.setVisibility(View.GONE);
            errorMsg.setVisibility(View.GONE);
        }*/
    }

    private void loadMoreActions() {
        Button btnNewUser;
        Button btnSettings;

        btnNewUser = (Button)findViewById(R.id.BtnNewUser);
        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
/*
        if(Session.getInstance().getLoggedUser()!=null
                && Session.getInstance().getLoggedUser().getId()!=null
                || Session.getInstance().getLoggedUser().getId()>0){
            btnNewUser.setVisibility(View.GONE);
        }
*/
        btnSettings = (Button)findViewById(R.id.BtnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadLeftLayoutContent(){
        final CharSequence tituloSeccion;
        final CharSequence tituloApp;

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (LinearLayout) findViewById(R.id.left_drawer);

        tituloSeccion = getTitle();
        tituloApp = getTitle();
        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.drawable.ic_navigation_drawer,
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


    /**
     * Method gets triggered when Login button is clicked
     *
     * @param view
     */
    public void loginUser(View view){
        // Get Email Edit View Value
        String email = emailET.getText().toString();
        // Get Password Edit View Value
        String password = pwdET.getText().toString();
        // Instantiate Http Request Param Object
        RequestParams params = new RequestParams();
        // When Email Edit View and Password Edit View have values other than Null
        if(Utils.isNotNull(email) && Utils.isNotNull(password)){
            // When Email entered is Valid
            if(Utils.validate(email)){
                // Put Http parameter username with value of Email Edit View control
                params.put("username", email);
                // Put Http parameter password with value of Password Edit Value control
                params.put("password", password);
                // Invoke RESTful Web Service with Http parameters
                invokeLoginWS(params);
            }
            // When Email is invalid
            else{
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        }
        // When any of the Edit View control left blank
        else{
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeLoginWS(RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.43.17:9999/useraccount/login/dologin",params ,new AsyncHttpResponseHandler() {
        client.get(Constants.SERVER_URL + "/user/dologin",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if(obj.getInt("id")>0){
                        Session.getInstance().setLoggedUser(Utils.obtainUser(obj));
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        // Navigate to Home screen
                        navigatetoHomeActivity();
                    }
                    // Else display error message
                    else{
                        errorMsg.setText("Incorrect Email or Password");
                        Toast.makeText(getApplicationContext(), "Incorrect Email or Password", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Method which navigates from Login Activity to Home Activity
     */
    public void navigatetoHomeActivity(){
        Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    /**
     * Method gets triggered when Register button is clicked
     *
     * @param view
     */
    public void navigatetoRegisterActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),RegistrationActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }


}