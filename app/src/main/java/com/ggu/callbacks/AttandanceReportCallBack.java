package com.ggu.callbacks;

import com.ggu.parsedclasses.AttendanceReport;
import com.google.gson.Gson;

/**
 * Created by Михаил on 29.10.2014.
 */
public class AttandanceReportCallBack extends CallBack {
    @Override
    public Object getResponse(String json) {

        Gson gson = new Gson();

        AttendanceReport report = gson.fromJson(json, AttendanceReport.class);


        return report;
    }
}
