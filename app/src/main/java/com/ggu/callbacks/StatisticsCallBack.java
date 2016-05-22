package com.ggu.callbacks;

import com.ggu.parsedclasses.Statistics;
import com.google.gson.Gson;

/**
 * Created by Михаил on 19.10.2014.
 */
public class StatisticsCallBack extends CallBack {
    @Override
    public Object getResponse(String json) {

        Gson gson = new Gson();
        Statistics stat = gson.fromJson(json, Statistics.class);

        return stat;
    }
}
