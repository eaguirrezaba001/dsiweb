package eus.ehu.dsiweb.ekain.eltenedor;


import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import eus.ehu.dsiweb.entity.DBReservation;
import eus.ehu.dsiweb.entity.DBRestaurant;
import eus.ehu.dsiweb.entity.IEntityConstants;

public class ServerConnectionProvider {

    public static void invokeLoginWS(final Fragment fragment, RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.SERVER_URL + "/user/dologin",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getInt(IEntityConstants.ID)>0){
                        Session.getInstance().setLoggedUser(Utils.obtainUser(obj));
                        Toast.makeText(fragment.getActivity(), R.string.user_successful_login, Toast.LENGTH_LONG).show();
                        ((FragmentLeftPanel)fragment).navigateToHomeActivity();
                    } else {
                        ((FragmentLeftPanel)fragment).errorMsg.setText(fragment.getActivity().getString(R.string.error_login_incorrect));
                        Toast.makeText(fragment.getActivity(), R.string.error_login_incorrect, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(fragment.getActivity(), R.string.error_response_invalid, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                failure(fragment.getActivity(), statusCode);
            }
        });
    }

    public static void invokeAllRestaurantWS(final Activity activity, final AdapterView adapterView, final int textViewResourceId){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.SERVER_URL + "/reservation/allrestaurant", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                try {
                    JSONArray array = obj.getJSONArray(IEntityConstants.RESTAURANT_LIST);
                    List<DBRestaurant> list = new LinkedList<DBRestaurant>();
                    for(int i=0; i < array.length(); i++) {
                        list.add(Utils.obtainRestaurant(array.getJSONObject(i)));
                    }
                    RestaurantListAdapter restaurantAdapter = new RestaurantListAdapter(
                            activity, textViewResourceId, list);
                    restaurantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    adapterView.setAdapter(restaurantAdapter);
                } catch (JSONException e) {
                    Toast.makeText(activity, R.string.error_response_invalid, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error){
                failure(activity, statusCode);
            }
        });
    }

    public static void invokeDoReservationWS(final Activity activity, DBReservation reservation){
        StringEntity entity = null;
        try {
            entity = new StringEntity(Utils.obtainJSON(reservation).toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.put(activity, Constants.SERVER_URL + "/reservation/doreservation", entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                try {
                    if (Utils.obtainStatus(obj)) {
                        Toast.makeText(activity, R.string.reservation_created_successful, Toast.LENGTH_LONG).show();
                        ((ActivityReservation)activity).createNotification();
                        ((ActivityReservation)activity).restoreMainFragment(null);
                        Session.getInstance().getLoggedUser().setCredit(Session.getInstance().getLoggedUser().getCredit()-10);
                    } else {
                        ((ActivityReservation)activity).setTextError(Utils.obtainError(obj));
                    }
                } catch (JSONException e) {
                    Toast.makeText(activity, R.string.error_response_invalid, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                failure(activity, statusCode);
            }
        });
    }

    public static void invokeUserCreditWS(final Activity activity, Integer user){
        RequestParams params = new RequestParams();
        params.put(IEntityConstants.USER, user.toString());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.SERVER_URL + "/user/getcredit",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getInt(IEntityConstants.AMOUNT)>=0){
                        String msg = activity.getString(R.string.reservation_price);
                        msg += " " + obj.getInt(IEntityConstants.AMOUNT);
                        ((ActivityReservation)activity).TxtReservationPrice.setText(msg);
                    } else {
                        String msg = activity.getString(R.string.error_reservation_user_credit);
                        ((ActivityReservation)activity).TxtReservationPrice.setText(msg);
                    }
                } catch (JSONException e) {
                    Toast.makeText(activity, R.string.error_response_invalid, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                failure(activity, statusCode);
            }
        });
    }


    private static void failure(Context context, int statusCode){
        if(statusCode == 404) {
            Toast.makeText(context, R.string.error_request_not_found, Toast.LENGTH_LONG).show();
        } else if(statusCode == 500) {
            Toast.makeText(context, R.string.error_server_end, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.error_server_down, Toast.LENGTH_LONG).show();
        }
    }


}
