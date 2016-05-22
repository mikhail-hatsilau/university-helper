package com.ggu.parsedclasses;

import java.util.ArrayList;

/**
 * Created by Михаил on 07.10.2014.
 */
public class AbsenceStat {

    private ArrayList<UserAbsence> stat = new ArrayList<UserAbsence>();
    private int month;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public ArrayList<UserAbsence> getStat() {
        return stat;
    }

    public void setStat(ArrayList<UserAbsence> stat) {
        this.stat = stat;
    }
}
