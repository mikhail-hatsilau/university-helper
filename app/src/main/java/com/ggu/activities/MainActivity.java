package com.ggu.activities;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ggu.adapters.ViewPagerAdapter;
import com.ggu.constants.Constants;
import com.ggu.fragments.EditPersonAbsenceFragment;
import com.ggu.fragments.GroupFragment;
import com.ggu.fragments.GroupsFragment;
import com.ggu.fragments.ReportsFragment;
import com.ggu.fragments.ScheduleFragment;
import com.ggu.fragments.StatisticsFragment;
import com.ggu.fragments.UsersFragment;
import com.ggu.listeners.CustomTabListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private SharedPreferences preferences;
    private ActionBar actionBar;
    private ActionBar.Tab tab;
    private ViewPager pager;
    private FragmentStatePagerAdapter viewPagerAdapter;
    private List<Fragment> fragments;
    private String[] titlesOfTabs;

    private final static int REQUEST_CODE_LOGIN = 0;
    private final static int REQUEST_CODE_UNIVERSITY = 1;
    private final static int USER_DOESNT_EXIST = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG, "onCreate MainActivity");
        setContentView(R.layout.activity_main);

        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOnPageChangeListener(this);
        preferences = getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE);

        if (preferences.getInt(Constants.USER_ID, USER_DOESNT_EXIST) == USER_DOESNT_EXIST) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_CODE_LOGIN);
        } else {
            createTabs();
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        actionBar.setSelectedNavigationItem(savedInstanceState.getInt(Constants.TAB_POSITION, 0));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(Constants.LOG, "onNewIntent MainActivity");

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            reCreateFragment(intent.getStringExtra(SearchManager.QUERY));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Constants.TAB_POSITION, actionBar.getSelectedNavigationIndex());

    }

    private void reCreateFragment(String query) {
//        FragmentManager fragmentManager = getFragmentManager();
        UsersFragment fragment = (UsersFragment) getSupportFragmentManager().findFragmentById(R.id.pager);
        fragment.getUsersByQuery(query);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_CODE_LOGIN) && (resultCode == RESULT_OK)) {
            createTabs();
        } else {
            if ((requestCode == REQUEST_CODE_UNIVERSITY) && (resultCode == RESULT_OK)) {
                setTabsForAdmin();
            } else {
                finish();
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void createTabs() {

        switch (preferences.getInt(Constants.ROLE, USER_DOESNT_EXIST)) {
            case 0:
                titlesOfTabs = getResources().getStringArray(R.array.user);
                setTabsForStudent();
                break;
            case 1:
                titlesOfTabs = getResources().getStringArray(R.array.master);
                setTabsForBoss();
                break;
            case 2:
                titlesOfTabs = getResources().getStringArray(R.array.admin);
                if (preferences.getInt(Constants.UNIVERSITY_ID, USER_DOESNT_EXIST) == USER_DOESNT_EXIST) {
                    Intent intent = new Intent(MainActivity.this, UniversityListActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_UNIVERSITY);
                } else {
                    setTabsForAdmin();
                }
                break;

        }
    }


    private void setTabsForAdmin() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new UsersFragment());
        fragments.add(new GroupsFragment());
        fragments.add(new ReportsFragment());
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(viewPagerAdapter);
        createNewTab(R.drawable.ic_action_group);
        createNewTab(R.drawable.group_folder);
        createNewTab(R.drawable.edit);
    }

    private void createNewTab(int icon){
        tab = actionBar.newTab();
        tab.setIcon(getResources().getDrawable(icon));
        tab.setTabListener(new CustomTabListener(this, pager));
        actionBar.addTab(tab);
    }

    private void setTabsForBoss() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new ScheduleFragment());
        fragments.add(new StatisticsFragment());
        fragments.add(new GroupFragment());
        fragments.add(new EditPersonAbsenceFragment());
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(viewPagerAdapter);
        createNewTab(R.drawable.shedule_icon);
        createNewTab(R.drawable.statistics_icon);
        createNewTab(R.drawable.ic_action_group);
        createNewTab(R.drawable.edit);

    }

    private void setTabsForStudent() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new ScheduleFragment());
        fragments.add(new StatisticsFragment());
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(viewPagerAdapter);
        createNewTab(R.drawable.shedule_icon);
        createNewTab(R.drawable.statistics_icon);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Log.d(Constants.LOG, "onCreateOptionsMenu");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        Log.d(Constants.LOG, "onPrepareOptionsMenu");
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTitle(titlesOfTabs[position]);
        actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
