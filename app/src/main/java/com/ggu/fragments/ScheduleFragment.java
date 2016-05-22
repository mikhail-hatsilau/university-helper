package com.ggu.fragments;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ExpandableListView;

import com.ggu.activities.R;
import com.ggu.adapters.ScheduleExpListAdapter;
import com.ggu.callbacks.GetScheduleCallBack;
import com.ggu.constants.Constants;
import com.ggu.database.DataBaseOperations;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.Day;
import com.ggu.parsedclasses.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Михаил on 25.10.2014.
 */
public class ScheduleFragment extends Fragment {

    private ExpandableListView list;
    private ScheduleExpListAdapter adapter;
    private List<ArrayList<Day>> daysList;
    private Activity activity;
    private SharedPreferences preferences;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Constants.LOG, "onResumeSchedule");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);


        list = (ExpandableListView) view.findViewById(R.id.schedule_list);

        preferences = activity.getSharedPreferences(Constants.USER_DATA_PREF, Context.MODE_PRIVATE);

        daysList = DataBaseOperations.getScheduleFromDb(activity);

        if (daysList.size() == 0){
            loadSchedule(null);
        } else {
            showList();
        }

        return view;
    }

    private void showList() {
        adapter = new ScheduleExpListAdapter(daysList, activity);
        list.setAdapter(adapter);
        for (int i = 0; i < daysList.size(); i++) {
            list.expandGroup(i);
        }

        list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
    }

    private void loadSchedule(final MenuItem item){
        SendRequest getInformation = new SendRequest(activity, getApiSchedule(), Constants.GET, new GetScheduleCallBack() {
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                Schedule shedule = (Schedule) response;
                hideProgress(item);
                getNewScheduleList(shedule);
            }

            @Override
            public void onError() {
                super.onError();
                hideProgress(item);
                Log.d(Constants.LOG, "update schedule error");
            }
        });

        getInformation.execute();
    }

    private void hideProgress(MenuItem item){
        if (item != null){
            item.setActionView(null);
        }
    }
    private void getNewScheduleList(Schedule shedule) {

        daysList = shedule.getShedule();
        showList();

        for (int i = 0; i < daysList.size(); i++) {
            DataBaseOperations.writeScheduleToDataBase(daysList.get(i), i, activity);
        }

        for (int i = daysList.size(); i < Constants.NUMBER_OF_DAYS; i++){
            Log.d(Constants.LOG, ""+i);
            DataBaseOperations.deleteDayFromScheduleTable(activity, i);
        }
    }

    private String getApiSchedule() {
        String apiUrl = Constants.API_URL + "action=shedule_get" + "&" + "id=" + preferences.getInt(Constants.USER_ID, -1) +
                "&" + "secret=" + preferences.getString(Constants.SECRET, "");
        return apiUrl;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_update, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.update:
                View view = getProgressBarView();
                item.setActionView(view);
                loadSchedule(item);
                return true;
            default:
                return false;
        }
    }


    private View getProgressBarView(){
        View progressBar = LayoutInflater.from(activity).inflate(R.layout.progress_bar, null);
        return progressBar;
    }
}
