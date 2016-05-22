package com.ggu.parsedclasses;

import java.util.ArrayList;

/**
 * Created by Михаил on 27.11.2014.
 */
public class Schedule {

    ArrayList<ArrayList<Day>> shedule = new ArrayList<ArrayList<Day>>();

    public ArrayList<ArrayList<Day>> getShedule() {
        return shedule;
    }

    public void setShedule(ArrayList<ArrayList<Day>> shedule) {
        this.shedule = shedule;
    }
}
