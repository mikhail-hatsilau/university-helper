package com.ggu.callbacks;

import com.ggu.parsedclasses.GroupInfrormation;
import com.google.gson.Gson;

/**
 * Created by Михаил on 27.10.2014.
 */
public class GroupInformationCallBack extends CallBack {

    @Override
    public Object getResponse(String json) {

        Gson gson = new Gson();

        GroupInfrormation groupInfrormation = gson.fromJson(json, GroupInfrormation.class);

        return groupInfrormation;
    }
}
