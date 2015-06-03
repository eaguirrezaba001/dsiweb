package eus.ehu.dsiweb.ekain.eltenedor;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import eus.ehu.dsiweb.entity.DBRestaurant;

public class RestaurantListAdapter extends ArrayAdapter<DBRestaurant>{

    private final Context context;
    private List<DBRestaurant> list;

    public RestaurantListAdapter(Context context, int textViewResourceId, List<DBRestaurant> list) {
        super(context, textViewResourceId, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View view, ViewGroup parent) {
        View v = view;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_restaurant, null);
        }

        v.setBackgroundColor(position%2!=0?Color.parseColor("#E4FFD3"):Color.WHITE);

        DBRestaurant c = list.get(position);
        TextView name = (TextView) v.findViewById(R.id.TxtRestaurantName);
        name.setText(c.getName());
        TextView description = (TextView) v.findViewById(R.id.TxtRestaurantDescription);
        description.setText(c.getDescription());

        ImageView imageView = (ImageView) v.findViewById(R.id.ImgRestaurantLogo);
        if(c.getLogoImage()==null || c.getLogoImage().length==0){
            imageView.setImageResource(R.drawable.ic_imag_not_available);
        } else {
            imageView.setImageBitmap(Utils.obtainLogoImage(c.getLogoImage()));
        }

        return v;
    }

}
