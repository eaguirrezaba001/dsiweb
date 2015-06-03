package eus.ehu.dsiweb.ekain.eltenedor;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eus.ehu.dsiweb.entity.DBReservation;
import eus.ehu.dsiweb.entity.DBRestaurant;
import eus.ehu.dsiweb.entity.IEntityConstants;


public class FragmentReservationList extends ListFragment {

    ProgressDialog prgDialog;

    TextView TxtReservationListEmpty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prgDialog = new ProgressDialog(this.getActivity());
        prgDialog.setMessage(getResources().getString(R.string.loading));
        prgDialog.setCancelable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation_list, container, false);
        TxtReservationListEmpty = (TextView)view.findViewById(R.id.TxtReservationListEmpty);
        TxtReservationListEmpty.setVisibility(View.GONE);
        invokeAllReservationWS();
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final int pos = position;
        new AlertDialog.Builder(this.getActivity())
                .setMessage(R.string.reservation_cancel_question)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        invokeCancelReservationWS(pos);
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void invokeAllReservationWS(){
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put(IEntityConstants.USER, Session.getInstance().getLoggedUser().getId().toString());

        client.get(Constants.SERVER_URL + "/reservation/allreservation", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                prgDialog.hide();
                try {
                    JSONArray list = obj.getJSONArray(IEntityConstants.RESERVATION_LIST);
                    List reservationList = new LinkedList<DBReservation>();
                    for(int i=0; i < list.length(); i++) {
                        reservationList.add(Utils.obtainReservation(list.getJSONObject(i)));
                    }

                    ReservationListAdapter adapter = new ReservationListAdapter(getActivity(), reservationList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    setListAdapter(adapter);

                    if(reservationList!=null && reservationList.size()>0){
                        TxtReservationListEmpty.setVisibility(View.GONE);
                    } else {
                        TxtReservationListEmpty.setVisibility(View.VISIBLE);
                    }

                    invokeAllRestaurantWS();
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), R.string.error_response_invalid, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error){
                prgDialog.hide();
                if(statusCode == 404) {
                    Toast.makeText(getActivity(), R.string.error_request_not_found, Toast.LENGTH_LONG).show();
                } else if(statusCode == 500) {
                    Toast.makeText(getActivity(), R.string.error_server_end, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), R.string.error_server_down, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void invokeAllRestaurantWS(){
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(Constants.SERVER_URL + "/reservation/allrestaurant", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                try {
                    JSONArray list = obj.getJSONArray(IEntityConstants.RESTAURANT_LIST);
                    Map restaurantMap = new HashMap<Integer, DBRestaurant>();
                    for(int i=0; i < list.length(); i++) {
                        DBRestaurant r = Utils.obtainRestaurant(list.getJSONObject(i));
                        restaurantMap.put(r.getId(), r);
                    }
                    ReservationListAdapter adapter = ((ReservationListAdapter) getListAdapter());
                    for(DBReservation reservation: adapter.getList()){
                        reservation.setDBRestaurant((DBRestaurant) restaurantMap.get(reservation.getRestaurant()));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), R.string.error_response_invalid, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error){
                if(statusCode == 404) {
                    Toast.makeText(getActivity(), R.string.error_request_not_found, Toast.LENGTH_LONG).show();
                } else if(statusCode == 500) {
                    Toast.makeText(getActivity(), R.string.error_server_end, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), R.string.error_server_down, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void invokeCancelReservationWS(int position){
        AsyncHttpClient client = new AsyncHttpClient();

        final int pos = position;
        DBReservation reservation = (DBReservation) getListAdapter().getItem(position);

        StringEntity entity = null;
        try {
            entity = new StringEntity(Utils.obtainJSON(reservation).toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.post(getActivity(), Constants.SERVER_URL + "/reservation/cancelreservation", entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                try {
                    if (obj.getBoolean("status")) {
                        Toast.makeText(getActivity(), R.string.reservation_canceled, Toast.LENGTH_LONG).show();

                        ((DBReservation) getListAdapter().getItem(pos)).setStatus(1);
                        ((ReservationListAdapter) getListAdapter()).notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), R.string.error_reservation_when_cancel, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), R.string.error_response_invalid, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(statusCode == 404) {
                    Toast.makeText(getActivity(), R.string.error_request_not_found, Toast.LENGTH_LONG).show();
                } else if(statusCode == 500) {
                    Toast.makeText(getActivity(), R.string.error_server_end, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), R.string.error_server_down, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
