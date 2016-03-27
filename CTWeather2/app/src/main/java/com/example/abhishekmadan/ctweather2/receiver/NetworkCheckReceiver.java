package com.example.abhishekmadan.ctweather2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Receiver class to notify user if network is not available
 */
public class NetworkCheckReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();

        if (activeNetInfo!=null||!activeNetInfo.isConnected()) {
            Toast.makeText(context,"Problem Accessing Internet",Toast.LENGTH_SHORT).show();
        }
    }

}
