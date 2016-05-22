package com.ggu.callbacks;

import com.ggu.parsedclasses.Schedule;
import com.google.gson.Gson;

/**
 * Created by Михаил on 12.11.2014.
 */
public class GetScheduleCallBack extends CallBack {

    @Override
    public Object getResponse(String json) {

        Gson gson = new Gson();

        Schedule shedule = gson.fromJson(json, Schedule.class);

        return shedule;
    }

}
