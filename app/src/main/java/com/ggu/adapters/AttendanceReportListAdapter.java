package com.ggu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ggu.constants.Constants;
import com.ggu.parsedclasses.AttendanceItem;
import com.ggu.activities.R;

import java.util.ArrayList;

/**
 * Created by Михаил on 29.10.2014.
 */
public class AttendanceReportListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<AttendanceItem> attendanceList;

    public AttendanceReportListAdapter(Context context, ArrayList<AttendanceItem> attendanceList){
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @Override
    public int getCount() {
        return attendanceList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        if (view==null){

            view = LayoutInflater.from(context).inflate(R.layout.attendance_report_list_item, viewGroup, false);

            TextView name = (TextView) view.findViewById(R.id.attendance_student_name_text);
            TextView seek = (TextView) view.findViewById(R.id.attendance_good_abcense);
            TextView loose = (TextView) view.findViewById(R.id.attendance_bad_abcense);

            ViewHolder vh = new ViewHolder();

            vh.name = name;
            vh.loose = loose;
            vh.seek = seek;

            view.setTag(vh);

        }

        ViewHolder vh = (ViewHolder) view.getTag();

        int attendanceLoose = attendanceList.get(i).getLoose();

        vh.name.setText(attendanceList.get(i).getName());
        vh.seek.setText(String.valueOf(attendanceList.get(i).getSeek()));
        vh.loose.setText(String.valueOf(attendanceLoose));



        if ((attendanceLoose >= Constants.LOOSE_1)&&(attendanceLoose < Constants.LOOSE_2)){
            setTextColor(context.getResources().getColor(R.color.loose_1), vh.name, vh.seek, vh.loose);
        } else {
            if ((attendanceLoose >= Constants.LOOSE_2)&&(attendanceLoose < Constants.LOOSE_3)){
                setTextColor(context.getResources().getColor(R.color.loose_2), vh.name, vh.seek, vh.loose);
            } else {
                if (attendanceLoose > Constants.LOOSE_3) {
                    setTextColor(context.getResources().getColor(R.color.loose_3), vh.name, vh.seek, vh.loose);
                } else {
                    setTextColor(context.getResources().getColor(R.color.default_color_loose), vh.name, vh.seek, vh.loose);
                }
            }
        }

        return view;
    }

    private void setTextColor(int color, TextView name, TextView seek, TextView loose){
        name.setTextColor(color);
        seek.setTextColor(color);
        loose.setTextColor(color);
    }

    private class ViewHolder{
        public TextView name, seek, loose;
    }
}
