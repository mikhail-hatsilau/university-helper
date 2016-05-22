package com.ggu.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ggu.activities.R;
import com.ggu.constants.Constants;
import com.ggu.listeners.ClickListenerForBtnList;
import com.ggu.parsedclasses.Day;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Михаил on 26.08.2014.
 */
public class ScheduleExpListAdapter extends BaseExpandableListAdapter {

    private List<ArrayList<Day>> days;
    private Context context;
    private String[] daysOfWeek;

    public ScheduleExpListAdapter(List<ArrayList<Day>> days, Context context) {
        this.days = days;
        this.context = context;
        daysOfWeek = this.context.getResources().getStringArray(R.array.days);
    }

    @Override
    public int getGroupCount() {
        return Constants.NUMBER_OF_DAYS;
    }

    @Override
    public int getChildrenCount(int i) {
        return days.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i2) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i2) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        SharedPreferences preferences = context.getSharedPreferences(Constants.USER_DATA_PREF, Context.MODE_PRIVATE);
        if (view == null) {

            ImageButton editButton = null;
            ClickListenerForBtnList clickListener = null;

            switch (preferences.getInt(Constants.ROLE, -1)) {
                case 0:
                    view = LayoutInflater.from(context).inflate(R.layout.schedule_day_header, viewGroup, false);
                    break;
                case 1:
                    view = LayoutInflater.from(context).inflate(R.layout.schedule_day_header_master, viewGroup, false);
                    editButton = (ImageButton) view.findViewById(R.id.image_view_edit_header);
                    clickListener = new ClickListenerForBtnList(context);
                    break;
            }

            TextView textView = (TextView) view.findViewById(R.id.textView_header);


            ViewHolderParent vh = new ViewHolderParent(textView, editButton, clickListener);
            view.setTag(vh);

        }

        ViewHolderParent vh = (ViewHolderParent) view.getTag();
        if (i >= days.size()){
            days.add(new ArrayList<Day>());
        }
        vh.text.setText(daysOfWeek[i]);

        if (vh.editButton != null) {
            vh.clickListener.setSchedule(days.get(i));
            vh.clickListener.setNameDay(daysOfWeek[i]);
            vh.clickListener.setDayNumber(i);
            vh.editButton.setOnClickListener(vh.clickListener);
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.subjects_item, viewGroup, false);
            TextView textViewName = (TextView) view.findViewById(R.id.subject_textView_name);
            TextView textViewLocation = (TextView) view.findViewById(R.id.subject_textView_location);
            TextView textViewTime = (TextView) view.findViewById(R.id.subject_textView_time);
            LinearLayout layout_subjects = (LinearLayout) view.findViewById(R.id.layout_subjects);
            LinearLayout overLine = (LinearLayout) view.findViewById(R.id.over_line);
            LinearLayout underLine = (LinearLayout) view.findViewById(R.id.under_line);
            ViewHolderChild vhChild = new ViewHolderChild(textViewName, textViewLocation, textViewTime, layout_subjects, overLine, underLine);
            view.setTag(vhChild);
        }


        ViewHolderChild vhChild = (ViewHolderChild) view.getTag();

        if ((days.get(i).size() - 1) == i2) {
            vhChild.layout.setBackgroundResource(R.drawable.item_schedule_child_bottom);
        } else {
            vhChild.layout.setBackgroundResource(R.drawable.item_schedule_child);
        }

        switch (days.get(i).get(i2).getType()) {
            case 1:
                vhChild.underLine.setVisibility(View.VISIBLE);
                break;
            case 2:
                vhChild.overLine.setVisibility(View.VISIBLE);
                break;
            default:
                vhChild.overLine.setVisibility(View.GONE);
                vhChild.underLine.setVisibility(View.GONE);
        }

        vhChild.textName.setText(days.get(i).get(i2).getName());
        vhChild.textLoc.setText(days.get(i).get(i2).getLocation());
        vhChild.textTime.setText(days.get(i).get(i2).getTime());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }

    private class ViewHolderParent {
        private TextView text;
        private ImageButton editButton;
        private ClickListenerForBtnList clickListener;

        public ViewHolderParent(TextView text, ImageButton editButton, ClickListenerForBtnList clickListener) {
            this.text = text;
            this.editButton = editButton;
            this.clickListener = clickListener;

        }
    }

    private class ViewHolderChild {
        private TextView textName, textLoc, textTime;
        private LinearLayout layout, overLine, underLine;

        public ViewHolderChild(TextView textName, TextView textLoc, TextView textTime, LinearLayout layout, LinearLayout overLine, LinearLayout underLine) {
            this.textName = textName;
            this.textLoc = textLoc;
            this.textTime = textTime;
            this.layout = layout;
            this.overLine = overLine;
            this.underLine = underLine;
        }
    }
}
