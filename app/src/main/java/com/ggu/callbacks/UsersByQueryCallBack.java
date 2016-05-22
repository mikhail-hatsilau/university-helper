package com.ggu.callbacks;

import com.ggu.parsedclasses.AdminUsers;
import com.google.gson.Gson;

/**
 * Created by Михаил on 28.10.2014.
 */
public class UsersByQueryCallBack extends CallBack {

    @Override
    public Object getResponse(String json) {
        Gson gson = new Gson();

        AdminUsers users = gson.fromJson(json, AdminUsers.class);

        return users;
    }
}
