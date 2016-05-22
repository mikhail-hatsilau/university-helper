package com.ggu.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ggu.adapters.GroupsListAdapter;
import com.ggu.callbacks.GroupListCallBack;
import com.ggu.constants.Constants;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.GroupList;
import com.ggu.activities.ChangeGroupActivity;
import com.ggu.activities.R;

public class GroupsFragment extends Fragment implements AdapterView.OnItemClickListener{

    private final int NEW_GROUP = -1;

    private SharedPreferences preferences;
    private ListView listView;
    private GroupsListAdapter listAdapter;
    private GroupList groups;
    private Activity activity;
    private ProgressBar progressBar;

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
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        listView = (ListView) view.findViewById(R.id.list_groups);
        progressBar = (ProgressBar) view.findViewById(R.id.groups_progress_bar);

        preferences = activity.getSharedPreferences(Constants.USER_DATA_PREF, activity.MODE_PRIVATE);

        getGroups();

        return view;
    }

    private void getGroups() {

        progressBar.setVisibility(View.VISIBLE);
        SendRequest getInformation = new SendRequest(activity, getApiUrl(), Constants.GET, new GroupListCallBack() {
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                progressBar.setVisibility(View.GONE);
                groups = (GroupList) response;
                showList();

            }

            @Override
            public void onError() {
                super.onError();
                progressBar.setVisibility(View.GONE);
            }
        });

        getInformation.execute();
    }

    private void showList(){
        listAdapter = new GroupsListAdapter(activity, groups.getGroups());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
    }


    private String getApiUrl() {
        String apiUrl = Constants.API_URL + "action=get_groups_list" + "&" + "id=" + preferences.getInt(Constants.USER_ID, -1) +
                "&" + "secret=" + preferences.getString(Constants.SECRET, "") + "&" +
                "univercity=" + preferences.getInt(Constants.UNIVERSITY_ID, -1);
        return apiUrl;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.add_group).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(Constants.LOG,"add group");
        addGroup(NEW_GROUP);
        return true;
    }

    private void addGroup(int groupId){
        Intent intent = new Intent(activity, ChangeGroupActivity.class);
        intent.putExtra(Constants.GROUP_ID, groupId);

        activity.startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        addGroup(groups.getGroups().get(i).getId());
        Log.d(Constants.LOG, "groupsItemClick");
    }
}
