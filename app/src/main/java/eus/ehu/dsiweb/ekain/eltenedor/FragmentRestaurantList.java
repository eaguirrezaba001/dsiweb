package eus.ehu.dsiweb.ekain.eltenedor;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import eus.ehu.dsiweb.entity.DBRestaurant;
import eus.ehu.dsiweb.entity.IEntityConstants;

public class FragmentRestaurantList extends ListFragment {

    ProgressDialog prgDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prgDialog = new ProgressDialog(this.getActivity());
        prgDialog.setMessage(getResources().getString(R.string.loading));
        prgDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        AddFloatingActionButton semi_transparent = (AddFloatingActionButton) view.findViewById(R.id.FabRestaurantList);
        semi_transparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Session.getInstance().getLoggedUser()==null){
                    Intent intent = new Intent(getActivity(),ActivityRegistration.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), ActivityReservation.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ServerConnectionProvider.invokeAllRestaurantWS(this.getActivity(), this.getListView(), android.R.layout.simple_list_item_1);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        FragmentTransaction ft1 = getFragmentManager().beginTransaction();
        FragmentRestaurantDetail restaurantDetail = new FragmentRestaurantDetail();
        restaurantDetail.setRestaurant((DBRestaurant) this.getListView().getItemAtPosition(position));
        ft1.replace(R.id.content_frame, restaurantDetail);
        ft1.commit();
    }


}