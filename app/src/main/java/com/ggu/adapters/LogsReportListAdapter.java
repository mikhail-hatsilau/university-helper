package com.ggu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ggu.parsedclasses.LogsItem;
import com.ggu.activities.R;

import java.util.ArrayList;

/**
 * Created by Михаил on 28.10.2014.
 */
public class LogsReportListAdapter extends BaseAdapter {


    private ArrayList<LogsItem> logs = new ArrayList<LogsItem>();
    private Context context;

    public LogsReportListAdapter(Context context, ArrayList<LogsItem> logs) {
        this.logs = logs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return logs.size();
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


        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.logs_report_list_item, viewGroup, false);

            TextView groupName = (TextView) view.findViewById(R.id.text_logs_report_group_name);
            TextView postDate = (TextView) view.findViewById(R.id.text_logs_report_post_date);

            ViewHolder vh = new ViewHolder();

            vh.goupName = groupName;
            vh.postDate = postDate;

            view.setTag(vh);
        }

        ViewHolder vh = (ViewHolder) view.getTag();

        vh.goupName.setText(logs.get(i).getGroup());
        vh.postDate.setText(logs.get(i).getLast_update());

        return view;
    }

    private class ViewHolder {
        public TextView goupName, postDate;
    }
}
