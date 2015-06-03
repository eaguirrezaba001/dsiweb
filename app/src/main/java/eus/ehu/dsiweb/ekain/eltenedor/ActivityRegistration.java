package eus.ehu.dsiweb.ekain.eltenedor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import eus.ehu.dsiweb.entity.IEntityConstants;


public class ActivityRegistration extends ActionBarActivity {

    ProgressDialog prgDialog;

    EditText TxtRegisterName;
    EditText TxtRegisterDocument;
    EditText TxtRegisterPhone;
    EditText TxtRegisterEmail;
    EditText TxtRegisterLogin;
    EditText TxtRegisterPassword;
    EditText TxtRegisterPasswordRepeat;

    TextView TxtRegisterError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TxtRegisterError = (TextView)findViewById(R.id.TxtRegisterError);
        TxtRegisterName = (EditText)findViewById(R.id.TxtRegisterName);
        TxtRegisterDocument = (EditText)findViewById(R.id.TxtRegisterDocument);
        TxtRegisterPhone = (EditText)findViewById(R.id.TxtRegisterPhone);
        TxtRegisterEmail = (EditText)findViewById(R.id.TxtRegisterEmail);
        TxtRegisterLogin = (EditText)findViewById(R.id.TxtRegisterLogin);
        TxtRegisterPassword = (EditText)findViewById(R.id.TxtRegisterPassword);
        TxtRegisterPasswordRepeat = (EditText)findViewById(R.id.TxtRegisterPasswordRepeat);

        TxtRegisterEmail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                TxtRegisterLogin.setText(TxtRegisterEmail.getText());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getString(R.string.loading));
        prgDialog.setCancelable(false);
    }

    public void registerUser(View view){
        String name =  TxtRegisterName.getText().toString();
        String document =  TxtRegisterDocument.getText().toString();
        String phone =  TxtRegisterPhone.getText().toString();
        String email = TxtRegisterEmail.getText().toString();
        String login = TxtRegisterLogin.getText().toString();
        String password = TxtRegisterPassword.getText().toString();
        String passwordRepeat = TxtRegisterPasswordRepeat.getText().toString();

        if(Utils.isNotNull(login) && Utils.isNotNull(email) && Utils.isNotNull(password)){
            if(Utils.validEmail(email)){
                if(Utils.validPassword(password, passwordRepeat)){
                    RequestParams params = new RequestParams();
                    params.put("name",name);
                    params.put("document",document);
                    params.put("login",login);
                    params.put("password",password);
                    params.put("email",email);
                    params.put("phone",phone);
                    invokeResgistrationWS(params);
                } else{
                    Toast.makeText(getApplicationContext(), R.string.error_registration_password_incorrect, Toast.LENGTH_LONG).show();
                    TxtRegisterError.setText(getString(R.string.error_registration_password_incorrect));
                }
            } else{
                Toast.makeText(getApplicationContext(), R.string.error_registration_enter_valid_email, Toast.LENGTH_LONG).show();
                TxtRegisterError.setText(getString(R.string.error_registration_enter_valid_email));
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.error_registration_complete_all_field, Toast.LENGTH_LONG).show();
            TxtRegisterError.setText(getString(R.string.error_registration_complete_all_field));
        }

    }

    public void invokeResgistrationWS(RequestParams params){
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.SERVER_URL + "/user/doregister", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                prgDialog.hide();
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean(IEntityConstants.STATUS)){
                        setDefaultValues();
                        Toast.makeText(getApplicationContext(), R.string.user_successful_registration, Toast.LENGTH_LONG).show();
                        navigatetoLoginActivity(null);
                    } else {
                        TxtRegisterError.setText(obj.getString(IEntityConstants.ERROR_MSG));
                        Toast.makeText(getApplicationContext(), obj.getString(IEntityConstants.ERROR_MSG), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), R.string.error_response_invalid, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                prgDialog.hide();
                if(statusCode == 404) {
                    Toast.makeText(getApplicationContext(), R.string.error_request_not_found, Toast.LENGTH_LONG).show();
                    TxtRegisterError.setText(getString(R.string.error_request_not_found));
                } else if(statusCode == 500) {
                    Toast.makeText(getApplicationContext(), R.string.error_server_end, Toast.LENGTH_LONG).show();
                    TxtRegisterError.setText(getString(R.string.error_server_end));
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_server_down, Toast.LENGTH_LONG).show();
                    TxtRegisterError.setText(getString(R.string.error_server_down));
                }
            }
        });
    }

    public void navigatetoLoginActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),MainActivity.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    public void setDefaultValues(){
        TxtRegisterName.setText("");
        TxtRegisterEmail.setText("");
        TxtRegisterDocument.setText("");
        TxtRegisterPhone.setText("");
        TxtRegisterLogin.setText("");
        TxtRegisterPassword.setText("");
        TxtRegisterPasswordRepeat.setText("");
        TxtRegisterError.setText("");
    }


}
