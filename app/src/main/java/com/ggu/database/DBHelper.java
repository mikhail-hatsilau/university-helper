package com.ggu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Михаил on 24.08.2014.
 */
public class DBHelper extends SQLiteOpenHelper {


    private final static int DATA_BASE_VERSION = 1;
    private final static String DATA_BASE_NAME = "UHDataBase";

    private final static String CREATE_TABLE = "CREATE TABLE ";
    private final static String COMMA_SEP = ",";
    private final static String TEXT_TYPE = " TEXT";
    private final static String INTEGER_TYPE = " INTEGER";

    private final static String SQL_CREATE_TABLE_SUBJECTS = CREATE_TABLE + DBContract.FeedsSubjects.TABLE_NAME_SUBJECTS +
            " (" + DBContract.FeedsSubjects._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
            DBContract.FeedsSubjects.SUBJECT_NAME + TEXT_TYPE + " );";

    private final static String SQL_CREATE_TABLE_SCHEDULE = CREATE_TABLE + DBContract.FeedsSchedule.TABLE_NAME_SCHEDULE +
            " (" + DBContract.FeedsSchedule.SUBJECT_ID + INTEGER_TYPE + COMMA_SEP + DBContract.FeedsSchedule.LOCATION +
            TEXT_TYPE + COMMA_SEP + DBContract.FeedsSchedule.TIME + TEXT_TYPE + COMMA_SEP + DBContract.FeedsSchedule.TYPE + INTEGER_TYPE +
            COMMA_SEP + DBContract.FeedsSchedule.DAY + INTEGER_TYPE + COMMA_SEP + " FOREIGN KEY" + "(" +
            DBContract.FeedsSchedule.SUBJECT_ID + ")" + " REFERENCES " + DBContract.FeedsSubjects.TABLE_NAME_SUBJECTS + "(" +
            DBContract.FeedsSubjects._ID + ")" + " );";

    private final static String SQL_CREATE_TABLE_STATISTICS = CREATE_TABLE + DBContract.FeedsStatistics.TABLE_NAME_STATISTICS + " (" +
            DBContract.FeedsStatistics._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP + DBContract.FeedsStatistics.MONTH +
            INTEGER_TYPE + COMMA_SEP + DBContract.FeedsStatistics.YEAR + INTEGER_TYPE + COMMA_SEP + DBContract.FeedsStatistics.SEEK +
            INTEGER_TYPE + COMMA_SEP + DBContract.FeedsStatistics.LOOSE + INTEGER_TYPE + ");";

    private final static String SQL_CREATE_TABLE_GROUP = CREATE_TABLE + DBContract.FeedsGroup.TABLE_NAME_GROUP + " (" +
            DBContract.FeedsGroup._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + DBContract.FeedsGroup.PERSON_NAME + TEXT_TYPE + ");";

    private final static String SQL_CREATE_TABLE_UNIVERSITIES = CREATE_TABLE + DBContract.FeedsUniversity.TABLE_NAME + " (" +
            DBContract.FeedsUniversity._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + DBContract.FeedsUniversity.UNIVERSITY_NAME +
            " TEXT" + ");";

    public DBHelper(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_SUBJECTS);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_SCHEDULE);

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_STATISTICS);

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_GROUP);

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_UNIVERSITIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
