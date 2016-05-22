package com.ggu.parsedclasses;

import java.util.ArrayList;

/**
 * Created by Михаил on 29.08.2014.
 */
public class Statistics {

    private ArrayList <StatMonth> stat = new ArrayList<StatMonth>();

    public ArrayList<StatMonth> getStat() {
        return stat;
    }

    public void setStat(ArrayList<StatMonth> stat) {
        this.stat = stat;
    }
}
