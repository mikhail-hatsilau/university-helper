package com.ggu.parsedclasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Михаил on 24.10.2014.
 */
public class StudInfo extends UserInfo {

    private List<ArrayList<Day>> shedule = new ArrayList<ArrayList<Day>>();

    public List<ArrayList<Day>> getShedule() {
        return shedule;
    }

    public void setShedule(List<ArrayList<Day>> shedule) {
        this.shedule = shedule;
    }
}
