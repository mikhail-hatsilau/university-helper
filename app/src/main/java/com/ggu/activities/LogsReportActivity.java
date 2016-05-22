package com.ggu.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ggu.adapters.LogsReportListAdapter;
import com.ggu.callbacks.LogsReportCallBack;
import com.ggu.constants.Constants;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.LogsReport;

/**
 * Created by Михаил on 15.11.2014.
 */
public class LogsReportActivity extends Activity {

    private ProgressBar progressBar;
    private ListView listView;
    private LogsReportListAdapter listAdapter;
    private LogsReport report;
    private TextView emptyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logs_report);

        listView = (ListView) findViewById(R.id.logs_report_list);
        progressBar = (ProgressBar) findViewById(R.id.logs_report_progress_bar);
        emptyList = (TextView) findViewById(R.id.text_empty_logs_report);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        getLogReport();

    }

    private void getLogReport(){
        progressBar.setVisibility(View.VISIBLE);

        SendRequest getInformation = new SendRequest(this, getApiUrl(), Constants.GET, new LogsReportCallBack(){
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                progressBar.setVisibility(View.GONE);
                report = (LogsReport) response;
                showLogsList();
            }

            @Override
            public void onError() {
                super.onError();
                progressBar.setVisibility(View.GONE);
            }
        });
        getInformation.execute();
    }

    private void showLogsList(){
        if (report.getStats().size()==0){
            emptyList.setVisibility(View.VISIBLE);
        } else {
            emptyList.setVisibility(View.GONE);
            listAdapter = new LogsReportListAdapter(this, report.getStats());
            listView.setAdapter(listAdapter);
        }
    }

    private String getApiUrl(){
        SharedPreferences preferences = getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE);

        String apiUrl = Constants.API_URL+"action=view_report"+"&"+"id="+preferences.getInt(Constants.USER_ID,-1)+
                "&"+"secret="+preferences.getString(Constants.SECRET,"")+
                "&"+"univercity="+preferences.getInt(Constants.UNIVERSITY_ID, -1)+"&"+"type=logs";

        return apiUrl;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }
}
