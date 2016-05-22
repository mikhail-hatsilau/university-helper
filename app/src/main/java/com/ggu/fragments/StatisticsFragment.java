package com.ggu.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ggu.activities.R;
import com.ggu.adapters.StatisticsListAdapter;
import com.ggu.callbacks.StatisticsCallBack;
import com.ggu.constants.Constants;
import com.ggu.database.DataBaseOperations;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.StatMonth;
import com.ggu.parsedclasses.Statistics;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Михаил on 25.10.2014.
 */
public class StatisticsFragment extends Fragment {

    private ListView stat_list;
    private ProgressBar progressBar;
    private StatisticsListAdapter adapter;
    private Activity activity;
    private SharedPreferences preferences;
    private List<StatMonth> statisticsList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        stat_list = (ListView) view.findViewById(R.id.statistics_list);
        progressBar = (ProgressBar) view.findViewById(R.id.stat_progress_bar);

        preferences = activity.getSharedPreferences(Constants.USER_DATA_PREF, activity.MODE_PRIVATE);

        statisticsList = DataBaseOperations.getStatisticsFromDb(activity);
        if (statisticsList.size() == 0) {
            progressBar.setVisibility(View.VISIBLE);
            loadStatistics(null);
        } else {
            showStatisticsList();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Constants.LOG, "onResumeStatistics");
    }

    private void loadStatistics(final MenuItem item) {
        SendRequest getInf = new SendRequest(activity, getApiUrl(), Constants.GET, new StatisticsCallBack() {
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                hidePorgress(item);
                Statistics statistics = (Statistics) response;
                statisticsList = statistics.getStat();
                DataBaseOperations.writeStatisticsToDb(statisticsList, activity);
                showStatisticsList();
            }

            @Override
            public void onError() {
                super.onError();
                hidePorgress(item);
                Log.d(Constants.LOG, "stat error");
            }
        });
        getInf.execute();
    }

    private void hidePorgress(MenuItem item){
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
        if (item != null) {
            item.setActionView(null);
        }
    }

    private String getApiUrl() {
        String api = Constants.API_URL + "action=" + Constants.ACTION_STAT + "&" +
                "id=" + preferences.getInt(Constants.USER_ID, -1) + "&" + "secret=" + preferences.getString(Constants.SECRET, "");
        Log.d(Constants.LOG, api);
        return api;
    }

    private void showStatisticsList() {
        Collections.sort(statisticsList, new Comparator<StatMonth>() {
            @Override
            public int compare(StatMonth statMonth, StatMonth statMonth2) {
                return statMonth.getMonth() - statMonth2.getMonth();
            }
        });
        adapter = new StatisticsListAdapter(activity, statisticsList);
        stat_list.setAdapter(adapter);
    }

//    private void updateDataBase() {
//        dbHelper = new DBHelper(activity);
//        db = dbHelper.getWritableDatabase();
//        boolean dbHasRaw = false;
//        ContentValues cv;
//
//        Cursor cursor = db.rawQuery(Constants.SELECT_QUERY + DBContract.FeedsStatistics.TABLE_NAME_STATISTICS, null);
//
//        if (cursor.moveToFirst()) {
//            for (StatMonth statMonth : statisticsList) {
//
//                do {
//
//                    int seek = cursor.getInt(cursor.getColumnIndex(DBContract.FeedsStatistics.SEEK));
//                    int loose = cursor.getInt(cursor.getColumnIndex(DBContract.FeedsStatistics.LOOSE));
//                    int month = cursor.getInt(cursor.getColumnIndex(DBContract.FeedsStatistics.MONTH));
//
//
//                    if (month == statMonth.getMonth()) {
//                        if (seek != statMonth.getSeek()) {
//                            cv = new ContentValues();
//                            cv.put(DBContract.FeedsStatistics.SEEK, statMonth.getSeek());
//                            db.update(DBContract.FeedsStatistics.TABLE_NAME_STATISTICS, cv, "month=?", new String[]{String.valueOf(month)});
//                        }
//
//                        if (loose != statMonth.getLoose()) {
//                            cv = new ContentValues();
//                            cv.put(DBContract.FeedsStatistics.LOOSE, statMonth.getLoose());
//                            db.update(DBContract.FeedsStatistics.TABLE_NAME_STATISTICS, cv, "month=?", new String[]{String.valueOf(month)});
//                        }
//                        dbHasRaw = true;
//                        break;
//                    }
//                } while (cursor.moveToNext());
//
//                cursor.moveToFirst();
//
//                if (!dbHasRaw) {
//                    cv = new ContentValues();
//                    cv.put(DBContract.FeedsStatistics.MONTH, statMonth.getMonth());
//                    cv.put(DBContract.FeedsStatistics.YEAR, statMonth.getYear());
//                    cv.put(DBContract.FeedsStatistics.SEEK, statMonth.getSeek());
//                    cv.put(DBContract.FeedsStatistics.LOOSE, statMonth.getLoose());
//                    db.insert(DBContract.FeedsStatistics.TABLE_NAME_STATISTICS, null, cv);
//                }
//
//                dbHasRaw = false;
//            }
//        }
//        dbHelper.close();
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_update, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                item.setActionView(getProgressBarView());
                loadStatistics(item);
                return true;
            default: return false;
        }
    }

    private View getProgressBarView(){
        View view = LayoutInflater.from(activity).inflate(R.layout.progress_bar, null);
        return view;
    }
}
