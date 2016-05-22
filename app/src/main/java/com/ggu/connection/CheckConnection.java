package com.ggu.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Михаил on 03.10.2014.
 */
public class CheckConnection {

    public static boolean getConnectionStatus(Context context) {

        ConnectivityManager manager;


        manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
