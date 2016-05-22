package com.ggu.database;

import android.provider.BaseColumns;

/**
 * Created by Михаил on 24.08.2014.
 */
public class DBContract {

    public abstract static class FeedsSubjects implements BaseColumns {
        public static final String TABLE_NAME_SUBJECTS = "subjects";
        public static final String SUBJECT_NAME = "name";
    }

    public abstract static class FeedsSchedule implements BaseColumns {
        public static final String TABLE_NAME_SCHEDULE = "schedule";
        public static final String SUBJECT_ID = "subjectId";
        public static final String LOCATION = "location";
        public static final String TIME = "time";
        public static final String TYPE = "type";
        public final static String DAY = "day";
    }

    public abstract static class FeedsStatistics implements BaseColumns {

        public static final String TABLE_NAME_STATISTICS = "statistics";
        public static final String YEAR = "year";
        public static final String MONTH = "month";
        public static final String SEEK = "seek";
        public static final String LOOSE = "loose";
    }

    public abstract static class FeedsGroup implements BaseColumns {

        public final static String TABLE_NAME_GROUP = "students_group";
        public final static String PERSON_NAME = "name";
    }

    public abstract static class FeedsUniversity implements BaseColumns{

        public final static String TABLE_NAME = "university";
        public final static String UNIVERSITY_NAME = "name";
    }
}
