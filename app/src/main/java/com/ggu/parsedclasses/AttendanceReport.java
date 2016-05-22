package com.ggu.parsedclasses;

import java.util.ArrayList;

/**
 * Created by Михаил on 29.10.2014.
 */
public class AttendanceReport {

    private ArrayList<AttendanceItem> stats = new ArrayList<AttendanceItem>();

    public ArrayList<AttendanceItem> getStats() {
        return stats;
    }

    public void setStats(ArrayList<AttendanceItem> stats) {
        this.stats = stats;
    }
}
