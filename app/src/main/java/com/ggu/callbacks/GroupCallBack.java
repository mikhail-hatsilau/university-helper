package com.ggu.callbacks;

import com.ggu.parsedclasses.Students;
import com.google.gson.Gson;

/**
 * Created by Михаил on 19.10.2014.
 */
public class GroupCallBack extends CallBack {
    @Override
    public Object getResponse(String json) {

        Gson gson = new Gson();

        Students students = gson.fromJson(json, Students.class);

        return students;
    }
}
