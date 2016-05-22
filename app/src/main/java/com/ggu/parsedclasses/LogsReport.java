package com.ggu.parsedclasses;

import java.util.ArrayList;

/**
 * Created by Михаил on 28.10.2014.
 */
public class LogsReport {

    private ArrayList<LogsItem> stats = new ArrayList<LogsItem>();

    public ArrayList<LogsItem> getStats() {
        return stats;
    }

    public void setStats(ArrayList<LogsItem> stats) {
        this.stats = stats;
    }
}
