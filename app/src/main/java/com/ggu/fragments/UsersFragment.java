package com.ggu.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.ggu.adapters.AdminUsersListAdapter;
import com.ggu.callbacks.UsersByQueryCallBack;
import com.ggu.constants.Constants;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.AdminUsers;
import com.ggu.activities.AdminAddUserActivity;
import com.ggu.activities.R;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;

/**
 * Created by Михаил on 25.10.2014.
 */
public class UsersFragment extends Fragment {


    private Activity activity;
    private ListView usersListView;
    private AdminUsersListAdapter listAdapter;
    private ProgressDialog progressDialog;
    private AdminUsers adminUsers;
    private LinearLayout imageSearch;


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
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        usersListView = (ListView) view.findViewById(R.id.admin_users_list);
        imageSearch = (LinearLayout) view.findViewById(R.id.search_users_empty);

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getResources().getString(R.string.wait));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.search).setVisible(true);
        menu.findItem(R.id.add_group).setVisible(true);

        SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));


        try {
            changeSearchViewIcon(searchView);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {

        }

    }

    private void changeSearchViewIcon(SearchView searchView) throws NoSuchFieldException, IllegalAccessException {

        Field searchField = SearchView.class.getDeclaredField("mCloseButton");
        searchField.setAccessible(true);

        ImageView closeButton = (ImageView) searchField.get(searchView);
        closeButton.setImageResource(R.drawable.ic_action_cancel);

        searchField = SearchView.class.getDeclaredField("mSearchButton");
        searchField.setAccessible(true);

        ImageView searchButton = (ImageView) searchField.get(searchView);
        searchButton.setImageResource(R.drawable.ic_action_search);

        int editTextId = searchView.getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText editText = (EditText) searchView.findViewById(editTextId);
        editText.setTextColor(getResources().getColor(android.R.color.white));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(Constants.LOG, "users menu item selected");
        switch (item.getItemId()) {
            case R.id.add_group:
                showAddUserActivity();
                break;
        }
        return true;
    }

    public void getUsersByQuery(String query) {
        Log.d(Constants.LOG, "Users fragment setQueryForSearch");

        String encodedQuery = null;
        try {
            encodedQuery = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d(Constants.LOG, e.toString());
            return;
        }


        progressDialog.show();

        SendRequest getInformation = new SendRequest(activity, getApiUrl(encodedQuery), Constants.GET, new UsersByQueryCallBack() {
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                progressDialog.dismiss();
                adminUsers = (AdminUsers) response;
                showUsersList();
            }

            @Override
            public void onError() {
                super.onError();
                progressDialog.dismiss();
            }
        });

        getInformation.execute();
    }

    private String getApiUrl(String query) {
        SharedPreferences preferences = activity.getSharedPreferences(Constants.USER_DATA_PREF, activity.MODE_PRIVATE);

        String apiUrl = Constants.API_URL + "action=users_search" + "&" + "id=" + preferences.getInt(Constants.USER_ID, -1) +
                "&" + "secret=" + preferences.getString(Constants.SECRET, "") +
                "&" + "query=" + query + "&" + "univercity=" + preferences.getInt(Constants.UNIVERSITY_ID, -1);

        Log.d(Constants.LOG, apiUrl);

        return apiUrl;
    }

    private void showUsersList() {
        if (adminUsers.getUsers().size() != 0) {
            imageSearch.setVisibility(View.GONE);
        }
        listAdapter = new AdminUsersListAdapter(activity, adminUsers.getUsers());
        usersListView.setAdapter(listAdapter);
    }


    private void showAddUserActivity() {
        Intent intent = new Intent(activity, AdminAddUserActivity.class);
        activity.startActivity(intent);
    }
}
