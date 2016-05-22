package com.ggu.callbacks;

import com.ggu.parsedclasses.Univercities;
import com.google.gson.Gson;

/**
 * Created by Михаил on 29.10.2014.
 */
public class UniversityListCallBack extends CallBack {

    @Override
    public Object getResponse(String json) {

        Gson gson = new Gson();

        Univercities univercities = gson.fromJson(json, Univercities.class);

        return univercities;
    }
}
