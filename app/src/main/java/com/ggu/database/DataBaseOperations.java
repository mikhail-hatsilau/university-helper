package com.ggu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ggu.constants.Constants;
import com.ggu.constants.SQLConstants;
import com.ggu.parsedclasses.Day;
import com.ggu.parsedclasses.StatMonth;

import java.util.ArrayList;
import java.util.List;


public class DataBaseOperations {

    public static void writeScheduleToDataBase(List<Day> schedule, int numberOfDay, Context context) {


        DBHelper dbHelper = new DBHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(SQLConstants.GET_SCHEDULE, new String[]{String.valueOf(numberOfDay)});

        String time = null;
        String name = null;
        String location = null;
        int id = 0, type = 0;
        boolean hasRows = false;

        if (cursor.moveToFirst()) {
            hasRows = true;
            if (schedule.size() == 0) {
                db.delete(DBContract.FeedsSchedule.TABLE_NAME_SCHEDULE, null, null);
            } else {

                for (Day day : schedule) {

                    if (!hasRows) {
                        insertToDataBase(day, db, numberOfDay);
                        continue;
                    }

                    id = cursor.getInt(cursor.getColumnIndex(DBContract.FeedsSchedule.SUBJECT_ID));
                    time = cursor.getString(cursor.getColumnIndex(DBContract.FeedsSchedule.TIME));
                    name = cursor.getString(cursor.getColumnIndex(DBContract.FeedsSubjects.SUBJECT_NAME));
                    location = cursor.getString(cursor.getColumnIndex(DBContract.FeedsSchedule.LOCATION));
                    type = cursor.getInt(cursor.getColumnIndex(DBContract.FeedsSchedule.TYPE));

                    if (!(day.getTime().equals(time) && day.getName().equals(name) && day.getLocation().equals(location) && (day.getType() == type))) {
                        updateDataBase(day, db, id);
                    }

                    if (!cursor.moveToNext()) {
                        hasRows = false;
                    }

                }

                if (hasRows) {
                    deleteRows(db, cursor);
                }
            }

        } else {

            for (Day day : schedule) {
                insertToDataBase(day, db, numberOfDay);
            }
        }


        dbHelper.close();
    }

    public static void deleteDayFromScheduleTable(Context context, int day) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBContract.FeedsSchedule.TABLE_NAME_SCHEDULE, DBContract.FeedsSchedule.DAY + "=?", new String[]{String.valueOf(day)});
        dbHelper.close();
    }

    private static void insertToDataBase(Day day, SQLiteDatabase db, int numberOfDay) {
        long subjectId = -1;
        ContentValues cv = new ContentValues();
        Cursor cursor = db.rawQuery(SQLConstants.GET_SUBJECTS, new String[]{day.getName()});
        if (!cursor.moveToFirst()) {
            cv.put(DBContract.FeedsSubjects.SUBJECT_NAME, day.getName());
            subjectId = db.insert(DBContract.FeedsSubjects.TABLE_NAME_SUBJECTS, null, cv);
            cv = new ContentValues();
        } else {
            subjectId = cursor.getInt(cursor.getColumnIndex(DBContract.FeedsSubjects._ID));
        }
        cv.put(DBContract.FeedsSchedule.SUBJECT_ID, subjectId);
        cv.put(DBContract.FeedsSchedule.LOCATION, day.getLocation());
        cv.put(DBContract.FeedsSchedule.TIME, day.getTime());
        cv.put(DBContract.FeedsSchedule.TYPE, day.getType());
        cv.put(DBContract.FeedsSchedule.DAY, numberOfDay);
        db.insert(DBContract.FeedsSchedule.TABLE_NAME_SCHEDULE, null, cv);
    }

    private static void updateDataBase(Day day, SQLiteDatabase db, int id) {
        ContentValues cv = new ContentValues();
        cv.put(DBContract.FeedsSubjects.SUBJECT_NAME, day.getName());
        db.update(DBContract.FeedsSubjects.TABLE_NAME_SUBJECTS, cv, DBContract.FeedsSubjects._ID + "=?", new String[]{String.valueOf(id)});
        cv = new ContentValues();
        cv.put(DBContract.FeedsSchedule.TIME, day.getTime());
        cv.put(DBContract.FeedsSchedule.LOCATION, day.getLocation());
        cv.put(DBContract.FeedsSchedule.TYPE, day.getType());
        db.update(DBContract.FeedsSchedule.TABLE_NAME_SCHEDULE, cv, DBContract.FeedsSchedule.SUBJECT_ID + " = ?", new String[]{String.valueOf(id)});
    }

    private static void deleteRows(SQLiteDatabase db, Cursor cursor) {
        do {
            int id = cursor.getInt(cursor.getColumnIndex(DBContract.FeedsSchedule.SUBJECT_ID));
            db.delete(DBContract.FeedsSchedule.TABLE_NAME_SCHEDULE, DBContract.FeedsSchedule.SUBJECT_ID + " = ?", new String[]{String.valueOf(id)});
        } while (cursor.moveToNext());
    }

    public static List<ArrayList<Day>> getScheduleFromDb(Context context) {

        List<ArrayList<Day>> days = new ArrayList<ArrayList<Day>>();

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        for (int i = 0; i < Constants.NUMBER_OF_DAYS; i++) {
            Cursor cursor = db.rawQuery(SQLConstants.GET_SCHEDULE, new String[]{String.valueOf(i)});
            ArrayList<Day> subjects = new ArrayList<Day>();

            if (cursor.moveToFirst()) {
                do {
                    Day day = new Day();

                    day.setName(cursor.getString(cursor.getColumnIndex(DBContract.FeedsSubjects.SUBJECT_NAME)));
                    day.setLocation(cursor.getString(cursor.getColumnIndex(DBContract.FeedsSchedule.LOCATION)));
                    day.setTime(cursor.getString(cursor.getColumnIndex(DBContract.FeedsSchedule.TIME)));
                    day.setType(cursor.getInt(cursor.getColumnIndex(DBContract.FeedsSchedule.TYPE)));

                    subjects.add(day);

                } while (cursor.moveToNext());

                days.add(subjects);
            }
        }

        dbHelper.close();

        return days;
    }

    public static List<StatMonth> getStatisticsFromDb(Context context) {

        List<StatMonth> statistics = new ArrayList<StatMonth>();
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(SQLConstants.GET_STATISTICS, null);

        if (c.moveToFirst()) {

            statistics = new ArrayList<StatMonth>();

            do {
                StatMonth month = new StatMonth();
                month.setMonth(c.getInt(c.getColumnIndexOrThrow(DBContract.FeedsStatistics.MONTH)));
                month.setYear(c.getInt(c.getColumnIndexOrThrow(DBContract.FeedsStatistics.YEAR)));
                month.setLoose(c.getInt(c.getColumnIndexOrThrow(DBContract.FeedsStatistics.LOOSE)));
                month.setSeek(c.getInt(c.getColumnIndexOrThrow(DBContract.FeedsStatistics.SEEK)));
                statistics.add(month);
            } while (c.moveToNext());
        }
        dbHelper.close();
        return statistics;
    }

    public static void writeStatisticsToDb(List<StatMonth> statistics, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(SQLConstants.GET_STATISTICS, null);

        if (!cursor.moveToFirst()) {
            for (StatMonth statMonth : statistics) {
                insertIntoStatistics(statMonth, db);
            }
        } else {
            boolean exists;
            for (StatMonth statMonth : statistics) {
                exists = false;
                do {
                    int month = cursor.getInt(cursor.getColumnIndex(DBContract.FeedsStatistics.MONTH));
                    //int year = cursor.getInt(cursor.getColumnIndex(DBContract.FeedsStatistics.YEAR));
                    int id = cursor.getInt(cursor.getColumnIndex(DBContract.FeedsStatistics._ID));
                    int loose = cursor.getInt(cursor.getColumnIndex(DBContract.FeedsStatistics.LOOSE));
                    int seek = cursor.getInt(cursor.getColumnIndex(DBContract.FeedsStatistics.SEEK));

                    if (month == statMonth.getMonth()) {
                        exists = true;
                        if ((loose != statMonth.getLoose()) || (seek != statMonth.getSeek())) {
                            updateStatistics(id, statMonth, db);
                        }
                    }
                }
                while (cursor.moveToNext());

                if (!exists) {
                    insertIntoStatistics(statMonth, db);
                }

                cursor.moveToFirst();
            }
        }

        dbHelper.close();
    }

    private static void updateStatistics(int id, StatMonth statMonth, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DBContract.FeedsStatistics.LOOSE, statMonth.getLoose());
        cv.put(DBContract.FeedsStatistics.SEEK, statMonth.getSeek());
        db.update(DBContract.FeedsStatistics.TABLE_NAME_STATISTICS, cv, DBContract.FeedsStatistics._ID + "=?", new String[]{String.valueOf(id)});
    }

    private static void insertIntoStatistics(StatMonth statMonth, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DBContract.FeedsStatistics.MONTH, statMonth.getMonth());
        cv.put(DBContract.FeedsStatistics.YEAR, statMonth.getYear());
        cv.put(DBContract.FeedsStatistics.SEEK, statMonth.getSeek());
        cv.put(DBContract.FeedsStatistics.LOOSE, statMonth.getLoose());
        db.insert(DBContract.FeedsStatistics.TABLE_NAME_STATISTICS, null, cv);
    }
}
