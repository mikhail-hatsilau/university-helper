package com.ggu.constants;

import com.ggu.database.DBContract;

/**
 * Created by Михаил on 26.03.2015.
 */
public class SQLConstants {
    public final static String GET_SCHEDULE = "SELECT " + DBContract.FeedsSchedule.SUBJECT_ID + "," +
            DBContract.FeedsSubjects.TABLE_NAME_SUBJECTS + "." +
            DBContract.FeedsSubjects.SUBJECT_NAME + "," + DBContract.FeedsSchedule.LOCATION + "," +
            DBContract.FeedsSchedule.TIME + "," + DBContract.FeedsSchedule.TYPE +
            " FROM " + DBContract.FeedsSchedule.TABLE_NAME_SCHEDULE + " INNER JOIN " + DBContract.FeedsSubjects.TABLE_NAME_SUBJECTS +
            " ON " + DBContract.FeedsSchedule.SUBJECT_ID + "=" + DBContract.FeedsSubjects._ID +
            " WHERE " + DBContract.FeedsSchedule.DAY + "=?" + ";";
    public final static String GET_SUBJECTS = "SELECT * FROM " + DBContract.FeedsSubjects.TABLE_NAME_SUBJECTS +
            " WHERE " + DBContract.FeedsSubjects.SUBJECT_NAME + "=?" + ";";
    public final static String GET_STATISTICS = "SELECT * FROM " + DBContract.FeedsStatistics.TABLE_NAME_STATISTICS + ";";
}
