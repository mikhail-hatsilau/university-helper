package com.ggu.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ggu.activities.AttendanceReportActivity;
import com.ggu.activities.LogsReportActivity;
import com.ggu.activities.R;

/**
 * Created by Михаил on 25.10.2014.
 */
public class ReportsFragment extends Fragment implements View.OnClickListener {

    private Button logsButton, attendanceButton;
    private Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);


        logsButton = (Button) view.findViewById(R.id.button_logs_report);
        attendanceButton = (Button) view.findViewById(R.id.button_attendance_report);

        logsButton.setOnClickListener(this);
        attendanceButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_logs_report:
                showActivity(LogsReportActivity.class);
                break;
            case R.id.button_attendance_report:
                showActivity(AttendanceReportActivity.class);
                break;
        }
    }

    private void showActivity(Class mClass){
        Intent intent = new Intent(activity, mClass);
        activity.startActivity(intent);
    }
}
