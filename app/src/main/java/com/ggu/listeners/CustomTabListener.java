package com.ggu.listeners;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.view.ViewPager;

public class CustomTabListener implements ActionBar.TabListener {

    private Fragment mFragment;
    private Context context;
    private ViewPager pager;

    public CustomTabListener(Context context, ViewPager pager){
        this.pager = pager;
        this.context = context;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//            if (mFragment==null){
//                mFragment = Fragment.instantiate(context, mClass.getName());
//                fragmentTransaction.add(R.id.fragment_content, mFragment);
//            } else {
//                fragmentTransaction.attach(mFragment);
//            }
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

//        if (mFragment!=null){
//            fragmentTransaction.detach(mFragment);
//        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
