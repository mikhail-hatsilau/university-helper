package com.ggu.callbacks;

import com.ggu.parsedclasses.NewUser;
import com.google.gson.Gson;

/**
 * Created by Михаил on 19.10.2014.
 */
public class SavePersonCallBack extends CallBack {
    @Override
    public Object getResponse(String json) {

        Gson gson = new Gson();

        NewUser newUser = gson.fromJson(json, NewUser.class);

        return newUser;
    }
}
