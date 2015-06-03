package eus.ehu.dsiweb.ekain.eltenedor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Vibrator;
import android.widget.Toast;

public class WifiBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi != null) {
            if (!wifi.isConnected()) {
                Toast.makeText(context, "Esta es una version de prueba, el Wifi debe estar conectado.",Toast.LENGTH_LONG).show();
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
            }
        }
    }

}
