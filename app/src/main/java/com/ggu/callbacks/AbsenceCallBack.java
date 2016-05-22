package com.ggu.callbacks;

import com.ggu.parsedclasses.AbsenceStat;
import com.google.gson.Gson;

/**
 * Created by Михаил on 20.10.2014.
 */
public class AbsenceCallBack extends CallBack {

    @Override
    public Object getResponse(String json) {

        Gson gson = new Gson();

        AbsenceStat absenceStat = gson.fromJson(json, AbsenceStat.class);

        return absenceStat;
    }
}
