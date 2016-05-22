package com.ggu.callbacks;

import com.ggu.parsedclasses.GroupList;
import com.google.gson.Gson;

/**
 * Created by Михаил on 24.10.2014.
 */
public class GroupListCallBack extends CallBack {

    @Override
    public Object getResponse(String json) {

        Gson gson = new Gson();

        GroupList groups = gson.fromJson(json, GroupList.class);

        return groups;
    }

}
