package eus.ehu.dsiweb.ekain.eltenedor;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import eus.ehu.dsiweb.entity.DBUser;

public class FragmentLeftPanel extends Fragment {

    ProgressDialog prgDialog;

    EditText emailET;
    EditText pwdET;
    TextView errorMsg;

    TextView TxtLoggedUserName;
    TextView TxtLoggedUserEmail;

    Button BtnUserLogin;
    Button BtnUserLogout;
    LoginButton facebookLoginButton;

    Button BtnUserReservation;
    Button BtnNewUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this.getActivity());
        prgDialog.setMessage(getResources().getString(R.string.loading));
        prgDialog.setCancelable(false);

        // Load Facebook login
        //FacebookSdk.sdkInitialize(getActivity());

        // Load Twitter login
        //TwitterAuthConfig authConfig = new TwitterAuthConfig("consumerKey", "consumerSecret");
        //Fabric.with(this, new TwitterCore(authConfig));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left_panel, container, false);

        //facebookLoginButton = (LoginButton) view.findViewById(R.id.facebook_login_button);
        BtnUserLogin = (Button)view.findViewById(R.id.BtnUserLogin);
        BtnUserLogout = (Button)view.findViewById(R.id.BtnUserLogout);
        BtnNewUser = (Button)view.findViewById(R.id.BtnNewUser);
        BtnUserReservation = (Button)view.findViewById(R.id.BtnUserReservation);

        DBUser user = Session.getInstance().getLoggedUser();
        if(user==null || user.getId()==null || user.getId()<=0){
            emailET = (EditText)view.findViewById(R.id.TxtLoginUserName);
            pwdET = (EditText)view.findViewById(R.id.TxtLoginUserPassword);
            errorMsg = (TextView)view.findViewById(R.id.login_error);
            LinearLayout LytLogged = (LinearLayout)view.findViewById(R.id.LyoLogged);
            LytLogged.setVisibility(View.GONE);
            BtnUserLogout.setVisibility(View.GONE);
            BtnUserReservation.setVisibility(View.GONE);
        } else {
            ImageView IvUser = (ImageView)view.findViewById(R.id.IvUser);
            TxtLoggedUserEmail = (TextView)view.findViewById(R.id.TxtLoggedUserEmail);
            TxtLoggedUserName = (TextView)view.findViewById(R.id.TxtLoggedUserName);
            IvUser.setImageResource(R.drawable.ic_action_emo_basic);
            TxtLoggedUserEmail.setText(Session.getInstance().getLoggedUser().getEmail());
            TxtLoggedUserName.setText(Session.getInstance().getLoggedUser().getName());
            LinearLayout LytLogin = (LinearLayout)view.findViewById(R.id.LytLogin);
            LytLogin.setVisibility(View.GONE);
            //facebookLoginButton.setVisibility(View.GONE);
            BtnUserLogin.setVisibility(View.GONE);
            BtnNewUser.setVisibility(View.GONE);
        }

        loadLoginButtons(view);

        loadMoreActions(view);

        return view;
    }

    private void loadLoginButtons(View view) {

        BtnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = pwdET.getText().toString();
                if(Utils.isNotNull(email) && Utils.isNotNull(password)){
                    doLogin(email, password);
                } else{
                    Toast.makeText(getActivity(), R.string.error_login_field_blank, Toast.LENGTH_LONG).show();
                }
            }
        });

        BtnUserLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.getInstance().setLoggedUser(null);
                navigateToHomeActivity();
                Toast.makeText(getActivity(), R.string.user_logout_done, Toast.LENGTH_LONG).show();
            }
        });

        // ************************
        // other login options
        // ************************

        // FACEBOOK
        /*
        CallbackManager callbackManager;
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
        */
        // TWITTER
           /*
            //TwitterLoginButton twitterLoginButton;
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

    }

    private void loadMoreActions(View view) {
        Button btnNewUser;
        Button btnSettings;

        btnNewUser = (Button)view.findViewById(R.id.BtnNewUser);
        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityRegistration.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnSettings = (Button)view.findViewById(R.id.BtnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityPreferences.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        BtnUserReservation = (Button)view.findViewById(R.id.BtnUserReservation);
        BtnUserReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                ft1.replace(R.id.content_frame, new FragmentReservationList());
                ft1.commit();

                ((MainActivity)getActivity()).closeDrawerLayout();
            }
        });
    }

    private void doLogin(String email, String password){
        RequestParams params = new RequestParams();
        params.put("username", email);
        params.put("password", password);
        ServerConnectionProvider.invokeLoginWS(this, params);
    }

    public void navigateToHomeActivity(){
        Intent homeIntent = new Intent(getActivity(),MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

}