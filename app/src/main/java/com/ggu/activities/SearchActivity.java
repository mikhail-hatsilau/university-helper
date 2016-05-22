package com.ggu.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ggu.constants.Constants;

/**
 * Created by Михаил on 27.10.2014.
 */
public class SearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG, "Search activity onCreate()");
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(Constants.LOG, "Search activity onNewIntent()");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        String query = null;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            query = intent.getStringExtra(SearchManager.QUERY);
        }
        Log.d(Constants.LOG, query);

    }
}
