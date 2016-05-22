package com.ggu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ggu.activities.R;
import com.ggu.constants.Constants;
import com.ggu.parsedclasses.StatMonth;

import java.util.List;

/**
 * Created by Михаил on 30.08.2014.
 */
public class StatisticsListAdapter extends BaseAdapter {

    private List<StatMonth> statistics;
    private Context context;
    private String[] months;
    private ViewHolder vh;

    public StatisticsListAdapter (Context context, List<StatMonth> statistics){
        this.statistics = statistics;
        this.context = context;
        this.months = this.context.getResources().getStringArray(R.array.months);
    }

    @Override
    public int getCount() {
        return statistics.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.statistics_list_item, viewGroup, false);
            TextView textView_month = (TextView) view.findViewById(R.id.textView_month);
            TextView textView_good = (TextView) view.findViewById(R.id.textView_count_good);
            TextView textView_not_good = (TextView) view.findViewById(R.id.textView_count_not_good);
            TextView textView_good_const = (TextView) view.findViewById(R.id.textGood_const);
            TextView textView_not_good_const = (TextView) view.findViewById(R.id.textNotGood_const);
            LinearLayout layout_strip = (LinearLayout) view.findViewById(R.id.strip_const);

            ViewHolder vh = new ViewHolder(textView_month, textView_good, textView_not_good, textView_good_const, textView_not_good_const, layout_strip);
            view.setTag(vh);
        }

        vh = (ViewHolder) view.getTag();


        vh.text_month.setText(months[statistics.get(i).getMonth()]);

        if ((statistics.get(i).getLoose() >= Constants.LOOSE_1)&&(statistics.get(i).getLoose()<Constants.LOOSE_2)){
            setColor(context.getResources().getColor(R.color.loose_1));
        } else {
            if ((statistics.get(i).getLoose() >= Constants.LOOSE_2)&&(statistics.get(i).getLoose()<Constants.LOOSE_3)){
                setColor(context.getResources().getColor(R.color.loose_2));
            } else {
                if (statistics.get(i).getLoose()>=Constants.LOOSE_3){
                    setColor(context.getResources().getColor(R.color.loose_3));
                } else {
                    setColor(context.getResources().getColor(R.color.default_color_loose));
                }
            }
        }

        vh.text_good.setText(String.valueOf(statistics.get(i).getSeek()));
        vh.text_not_good.setText(String.valueOf(statistics.get(i).getLoose()));

        return view;
    }

    private void setColor(int color){
        vh.text_month.setTextColor(color);
        vh.text_good.setTextColor(color);
        vh.text_not_good.setTextColor(color);
        vh.text_const_good.setTextColor(color);
        vh.text_not_good_const.setTextColor(color);
        vh.strip.setBackgroundColor(color);
    }


    private class ViewHolder {
        private TextView text_month, text_good, text_not_good, text_const_good, text_not_good_const;
        private LinearLayout strip;

        public ViewHolder(TextView text_month, TextView text_good, TextView text_not_good, TextView text_const_good, TextView text_not_good_const, LinearLayout strip){
            this.text_month = text_month;
            this.text_good = text_good;
            this.text_not_good = text_not_good;
            this.text_const_good = text_const_good;
            this.text_not_good_const = text_not_good_const;
            this.strip = strip;
        }
    }
}
