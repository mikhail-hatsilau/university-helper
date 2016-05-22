package com.ggu.loaders;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ggu.callbacks.CallBack;
import com.ggu.connection.CheckConnection;
import com.ggu.constants.Constants;
import com.ggu.activities.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by Михаил on 09.07.2014.
 */
public class SendRequest extends AsyncTask<Void, Void, String> {


    private HttpURLConnection httpURLConnection;
    private URL url;
    private BufferedReader bufferedReader;
    private String api_url_query;
    private Context context;
    private CallBack callback;
    private String type;

    public SendRequest(Context context, String api_url_query, String type, CallBack callback) {
        this.callback = callback;
        this.api_url_query = api_url_query;
        this.context = context;
        this.type = type;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Void... voids) {

        String line;
        StringBuilder stringBuilder = null;

        if (!CheckConnection.getConnectionStatus(context)) {
            return null;
        }

        Log.d(Constants.LOG, "begin transaction");


        try {
            url = new URL(api_url_query);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(type);
            httpURLConnection.setConnectTimeout(5000);

            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (SocketTimeoutException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            httpURLConnection.disconnect();
        }

        Log.d(Constants.LOG, stringBuilder.toString());

        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);

        if (json == null) {
            Toast.makeText(context, context.getResources().getString(R.string.no_connection), Toast.LENGTH_LONG).show();
            callback.onError();
            return;
        }

        String status = context.getResources().getString(R.string.status);
        String userNotFound = context.getResources().getString(R.string.not_found);

        Log.d(Constants.LOG, json);

        if ((json.contains(userNotFound)) || (json.contains(status) && json.contains("0"))) {
            callback.onError();
        } else {
            callback.onComplete(callback.getResponse(json));
        }


    }


}
