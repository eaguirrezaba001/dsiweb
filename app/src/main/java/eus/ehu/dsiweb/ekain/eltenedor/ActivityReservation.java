package eus.ehu.dsiweb.ekain.eltenedor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import eus.ehu.dsiweb.entity.DBReservation;
import eus.ehu.dsiweb.entity.DBRestaurant;

public class ActivityReservation extends Activity {

    private Button btnReservationDate;
    DatePickerDialog dateDialog;
    TextView TxtReservationError;
    TextView TxtReservationPrice;

    Spinner SpnRestaurants;
    Spinner spnReservationTime;
    Spinner spnPersonCount;

    ProgressDialog prgDialog;

    String selectedRestaurantId;
    String selectedRestaurantName;

    private String[] availableTimes = {"21:00", "21:15", "21:30", "21:45", "22:00"};
    private Integer[] personCountOptions = {1,2,3,4,5,6,7,8,9};

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate(arg1, arg2+1, arg3);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getResources().getString(R.string.loading));
        prgDialog.setCancelable(false);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int  month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        dateDialog = new DatePickerDialog(this, myDateListener, year, month, day);

        if(getIntent().getExtras()!=null){
            if(getIntent().getExtras().getString("restaurant_id")!=null){
                selectedRestaurantId = getIntent().getExtras().getString("restaurant_id");
            }
            if(getIntent().getExtras().getString("restaurant_description")!=null){
                selectedRestaurantName = getIntent().getExtras().getString("restaurant_description");
            }
        }

        TextView txtSelectedRestaurant = (TextView) findViewById(R.id.TxtSelectedRestaurant);
        SpnRestaurants = (Spinner) findViewById(R.id.SpnRestaurants);
        if(selectedRestaurantId!=null){
            txtSelectedRestaurant.setText(selectedRestaurantName);
            SpnRestaurants.setVisibility(View.GONE);
        } else {
            ServerConnectionProvider.invokeAllRestaurantWS(this, SpnRestaurants, android.R.layout.simple_spinner_item);

            txtSelectedRestaurant.setVisibility(View.GONE);
        }

        btnReservationDate = (Button) findViewById(R.id.BtnReservationDate);
        showDate(year, month+1, day);

        spnReservationTime = (Spinner) findViewById(R.id.SpnReservationTime);
        ArrayAdapter<CharSequence> hoursAdapter = new ArrayAdapter<CharSequence>(
                this, android.R.layout.simple_spinner_item,
                android.R.id.text1, availableTimes);
        hoursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnReservationTime.setAdapter(hoursAdapter);

        spnPersonCount = (Spinner) findViewById(R.id.SpnPersonCount);
        ArrayAdapter<Integer> personCountAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, personCountOptions);
        personCountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPersonCount.setAdapter(personCountAdapter);

        loadReservationDateButton((Button) findViewById(R.id.BtnReservationDate));

        TxtReservationPrice = (TextView) findViewById(R.id.TxtReservationPrice);
        ServerConnectionProvider.invokeUserCreditWS(this, Session.getInstance().getLoggedUser().getId());

        TxtReservationError = (TextView) findViewById(R.id.TxtReservationError);
    }

    private void showDate(int year, int month, int day) {
        btnReservationDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    private void loadReservationDateButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dateDialog.show();
            }
        });
    }

    public void saveReservation(View view){
        DBReservation reservation = new DBReservation();
        reservation.setUser(Session.getInstance().getLoggedUser().getId());
        if(selectedRestaurantId!=null){
            reservation.setRestaurant(Integer.parseInt(selectedRestaurantId));
        } else {
            reservation.setRestaurant(((DBRestaurant)SpnRestaurants.getSelectedItem()).getId());
        }
        Date date = new Date(dateDialog.getDatePicker().getCalendarView().getDate());
        String[] time = ((String) spnReservationTime.getSelectedItem()).split(":");
        Integer hour = Integer.parseInt(time[0]);
        Integer minute = Integer.parseInt(time[1]);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        reservation.setDate(cal.getTime());
        Integer personCount = Integer.parseInt(spnPersonCount.getSelectedItem().toString());
        reservation.setPersonCount(personCount);

        ServerConnectionProvider.invokeDoReservationWS(this, reservation);

        //createNotification();

        /*
        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        */
    }

    public void setTextError(String error){
        TxtReservationError.setText(error);
    }

    public void createNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("ElTenedor")
                        .setContentText("La reserva se ha creado");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }


    public void restoreMainFragment(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}