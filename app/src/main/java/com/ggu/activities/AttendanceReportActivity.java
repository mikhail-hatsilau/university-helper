package com.ggu.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ggu.adapters.AttendanceReportListAdapter;
import com.ggu.callbacks.AttandanceReportCallBack;
import com.ggu.constants.Constants;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.AttendanceItem;
import com.ggu.parsedclasses.AttendanceReport;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Михаил on 15.11.2014.
 */
public class AttendanceReportActivity extends Activity {


    private ListView list;
    private ProgressBar progressBar;
    private TextView emptyList;
    private AttendanceReport attendanceReport;
    private AttendanceReportListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs_report);

        list = (ListView) findViewById(R.id.logs_report_list);
        progressBar = (ProgressBar) findViewById(R.id.logs_report_progress_bar);
        emptyList = (TextView) findViewById(R.id.text_empty_logs_report);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        showReport();
    }

    private void showReport() {
        progressBar.setVisibility(View.VISIBLE);

        SendRequest getInformation = new SendRequest(this, getApiUrl(), Constants.GET, new AttandanceReportCallBack() {
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                progressBar.setVisibility(View.GONE);
                attendanceReport = (AttendanceReport) response;
                showReportList();
            }

            @Override
            public void onError() {
                super.onError();
                progressBar.setVisibility(View.GONE);
            }
        });

        getInformation.execute();
    }

    private void showReportList() {
        if (attendanceReport.getStats().size() == 0) {
            emptyList.setVisibility(View.VISIBLE);
        } else {
            Collections.sort(attendanceReport.getStats(), new Comparator<AttendanceItem>() {
                @Override
                public int compare(AttendanceItem attendanceItem, AttendanceItem attendanceItem2) {
                    return attendanceItem2.getLoose() - attendanceItem.getLoose();
                }
            });
            list.addHeaderView(getHeaderView());
            listAdapter = new AttendanceReportListAdapter(this, attendanceReport.getStats());
            list.setAdapter(listAdapter);
        }
    }

    private View getHeaderView() {
        View view = LayoutInflater.from(this).inflate(R.layout.attendance_list_header, null);
        return view;
    }

    private String getApiUrl() {
        SharedPreferences preferences = getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE);

        String apiUrl = Constants.API_URL + "action=view_report" + "&" + "id=" + preferences.getInt(Constants.USER_ID, -1) +
                "&" + "secret=" + preferences.getString(Constants.SECRET, "") +
                "&" + "univercity=" + preferences.getInt(Constants.UNIVERSITY_ID, -1) + "&" + "type=timings";

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
