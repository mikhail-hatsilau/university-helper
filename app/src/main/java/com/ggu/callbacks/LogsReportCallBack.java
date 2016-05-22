package com.ggu.callbacks;

import com.ggu.parsedclasses.LogsReport;
import com.google.gson.Gson;

/**
 * Created by Михаил on 28.10.2014.
 */
public class LogsReportCallBack extends CallBack {
    @Override
    public Object getResponse(String json) {

        Gson gson = new Gson();

        LogsReport logs = gson.fromJson(json, LogsReport.class);

        return logs;
    }
}
