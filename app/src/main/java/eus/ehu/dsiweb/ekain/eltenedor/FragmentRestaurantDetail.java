package eus.ehu.dsiweb.ekain.eltenedor;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.List;

import eus.ehu.dsiweb.entity.DBRestaurant;
import eus.ehu.dsiweb.entity.DBUser;

public class FragmentRestaurantDetail extends Fragment {

    DBRestaurant restaurant;

    public DBRestaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(DBRestaurant restaurant) {
        this.restaurant = restaurant;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);

        loadRestaurantInfoLayout(view);

        Button BtnNewReservation = (Button) view.findViewById(R.id.BtnNewReservation);
        loadNewReservationButton(BtnNewReservation);

        ImageButton BtnRestaurantMap = (ImageButton) view.findViewById(R.id.BtnRestaurantMap);
        loadRestaurantMapButton(BtnRestaurantMap);

        return view;
    }

    private void loadRestaurantInfoLayout(View view) {
        TextView TxtSelectedRestaurantName = (TextView) view.findViewById(R.id.TxtSelectedRestaurantName);
        TxtSelectedRestaurantName.setText(restaurant.getName());

        ImageView ImgSelectedRestaurantLogo = (ImageView) view.findViewById(R.id.ImgSelectedRestaurantLogo);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200, 100);
        ImgSelectedRestaurantLogo.setLayoutParams(layoutParams);
        if(restaurant.getLogoImage()==null || restaurant.getLogoImage().length==0){
            ImgSelectedRestaurantLogo.setImageResource(R.drawable.ic_imag_not_available);
        } else {
            ImgSelectedRestaurantLogo.setImageBitmap(Utils.obtainLogoImage(restaurant.getLogoImage()));
        }

        TextView description = new TextView(this.getActivity());
        description.setText(restaurant.getDescription());

        LinearLayout LyoSelectedRestaurantDetail = (LinearLayout) view.findViewById(R.id.LyoSelectedRestaurantDetail);
        LyoSelectedRestaurantDetail.addView(description);
    }

    private void loadNewReservationButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBUser user = Session.getInstance().getLoggedUser();
                if(user!=null && user.getId()!=null){
                    Intent intent = new Intent(getActivity(), ActivityReservation.class);
                    intent.putExtra("restaurant_id", restaurant.getId().toString());
                    intent.putExtra("restaurant_description", restaurant.getName());
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(getActivity(), ActivityRegistration.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void loadRestaurantMapButton(ImageButton button) {
       button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("longitude", restaurant.getLongitude());
                intent.putExtra("latitude", restaurant.getLatitude());
                startActivity(intent);
            }
        });
    }

}