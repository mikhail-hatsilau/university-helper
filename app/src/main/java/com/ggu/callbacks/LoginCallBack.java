package com.ggu.callbacks;

import android.util.Log;

import com.ggu.constants.Constants;
import com.ggu.parsedclasses.AdminInfo;
import com.ggu.parsedclasses.StudInfo;
import com.google.gson.Gson;

/**
 * Created by Михаил on 16.10.2014.
 */
public class LoginCallBack extends CallBack {

    private final String SCHEDULE = "shedule";
    private StudInfo studInfo;
    private AdminInfo adminInfo;

    @Override
    public Object getResponse(String json) {

        Gson gson = new Gson();

        if (json.contains(SCHEDULE)) {
//            Log.d(Constants.LOG, "studInfo");
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = new JSONObject(json);
//                jsonObject.getJSONObject(SCHEDULE);
//            } catch (JSONException e) {
//                //e.printStackTrace();
//                Log.d(Constants.LOG, e.toString());
//                jsonObject.remove(SCHEDULE);
//                json = jsonObject.toString();
//            }
            studInfo = gson.fromJson(json, StudInfo.class);
            return studInfo;
        } else {
            Log.d(Constants.LOG, "adminInfo");
            adminInfo = gson.fromJson(json, AdminInfo.class);
            return adminInfo;
        }
    }
}
