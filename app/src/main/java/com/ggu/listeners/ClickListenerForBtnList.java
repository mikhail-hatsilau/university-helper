package com.ggu.listeners;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.ggu.constants.Constants;
import com.ggu.parsedclasses.Day;
import com.ggu.activities.EditScheduleActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Михаил on 26.09.2014.
 */
public class ClickListenerForBtnList implements View.OnClickListener {

    private Context context;
    private ArrayList<Day> day;
    private String nameDay;
    private int dayNumber;

    public ClickListenerForBtnList(Context context) {
        this.context = context;
    }

    public void setSchedule(ArrayList<Day> day) {
        this.day = day;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public void setNameDay(String nameDay) {
        this.nameDay = nameDay;
    }


    @Override
    public void onClick(View view) {

        Log.d(Constants.LOG, day.toString());

        Intent intent = new Intent(context, EditScheduleActivity.class);
        intent.putExtra(Constants.DAY_OF_WEEK, getHashMap());
        intent.putExtra(Constants.NAME_DAY, nameDay);
        intent.putExtra(Constants.DAY_NUMBER, dayNumber);
        context.startActivity(intent);

    }

    private HashMap<Integer, Day> getHashMap() {
        HashMap<Integer, Day> hashMap = new HashMap<Integer, Day>();
        for (int i = 0; i < day.size(); i++) {
            hashMap.put(i, day.get(i));
        }
        return hashMap;
    }
}
