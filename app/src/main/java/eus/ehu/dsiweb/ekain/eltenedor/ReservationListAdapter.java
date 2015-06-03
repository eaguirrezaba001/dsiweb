package eus.ehu.dsiweb.ekain.eltenedor;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import eus.ehu.dsiweb.entity.DBReservation;

public class ReservationListAdapter extends ArrayAdapter<DBReservation>{

    private final Context context;
    private List<DBReservation> list;

    public ReservationListAdapter(Context context, List<DBReservation> list) {
        super(context, android.R.layout.simple_list_item_1, list);
        this.context = context;
        this.list = list;
    }

    public List<DBReservation> getList(){
        return list;
    }
    public void setList(List<DBReservation> list){
        this.list = list;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v = view;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_reservation, null);
        }

        DBReservation o = list.get(position);

        TextView TxtReservationRestaurantName = (TextView) v.findViewById(R.id.TxtReservationRestaurantName);
        TextView TxtReservationDate = (TextView) v.findViewById(R.id.TxtReservationDate);
        TextView TxtReservationPersonCount = (TextView) v.findViewById(R.id.TxtReservationPersonCount);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd/MMMMM/yyyy, HH:mm", Utils.getLocale(v.getContext()));
        if(o.getDBRestaurant()!=null){
            TxtReservationRestaurantName.setText(o.getDBRestaurant().getName());
        }
        TxtReservationDate.setText(sdf.format(o.getDate()));
        TxtReservationPersonCount.setText(o.getPersonCount()+" persona"+(o.getPersonCount()==1?"":"s"));

        ImageView imageView = (ImageView) v.findViewById(R.id.IvReservationStatus);
        if(o.getStatus()==0){
            if(!o.getDate().before(new Date())){
                imageView.setImageResource(R.drawable.ic_status_active);
                v.setBackgroundColor(Color.parseColor("#E4FFD3"));
            } else {
                imageView.setImageResource(R.drawable.ic_status_old);
                v.setBackgroundColor(Color.parseColor("#E1E0DF"));
            }
        } else if(o.getStatus()==1){
            imageView.setImageResource(R.drawable.ic_status_canceled);
            v.setBackgroundColor(Color.parseColor("#FFD9E0"));
        }

        return v;
    }

}
