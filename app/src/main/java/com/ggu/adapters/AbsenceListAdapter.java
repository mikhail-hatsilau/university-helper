package com.ggu.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.ggu.constants.Constants;
import com.ggu.parsedclasses.UserAbsence;
import com.ggu.activities.R;

import java.util.ArrayList;

/**
 * Created by Михаил on 07.10.2014.
 */
public class AbsenceListAdapter extends BaseAdapter {

    private ArrayList<UserAbsence> users;
    private Context context;

    public AbsenceListAdapter(Context context, ArrayList<UserAbsence> users) {
        this.users = users;
        this.context = context;
    }

    public ArrayList<UserAbsence> getUsers() {
        return users;
    }

    @Override
    public int getCount() {
        return users.size();
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

            view = LayoutInflater.from(context).inflate(R.layout.absence_list_item, viewGroup, false);

            TextView name = (TextView) view.findViewById(R.id.text_absence_name);
            EditText loose = (EditText) view.findViewById(R.id.absence_loose);
            EditText seek = (EditText) view.findViewById(R.id.absence_seek);

//            CustomTextWatcher looseTextWatcher = new CustomTextWatcher(loose);
//            CustomTextWatcher seekTextWatcher = new CustomTextWatcher(seek);

            loose.addTextChangedListener(new CustomTextWatcher(loose));
            seek.addTextChangedListener(new CustomTextWatcher(seek));

            //CustomTextWatcher textWatcher = new CustomTextWatcher();

            ViewHolder vh = new ViewHolder();

            vh.name = name;
            vh.loose = loose;
            vh.seek = seek;
//            vh.looseTextWatcher = looseTextWatcher;
//            vh.seekTextWatcher = seekTextWatcher;

            view.setTag(vh);
        }

        ViewHolder vh = (ViewHolder) view.getTag();

        vh.loose.setTag(i);
        vh.seek.setTag(i);

//        vh.looseTextWatcher.setPosition(i);
//        vh.seekTextWatcher.setPosition(i);

        vh.name.setText(users.get(i).getName());
        vh.loose.setText("" + users.get(i).getLoose());
        vh.seek.setText("" + users.get(i).getSeek());

//        vh.loose.addTextChangedListener(new CustomTextWatcher(i, vh.loose));
//        vh.seek.addTextChangedListener(new CustomTextWatcher(i, vh.seek));


//        vh.textWatcher.setPosition(i);
//        vh.textWatcher.setViewHolder(vh);


        return view;
    }

    private class ViewHolder {
        public TextView name;
        public EditText loose, seek;
        public CustomTextWatcher looseTextWatcher, seekTextWatcher;
    }

    private class CustomTextWatcher implements TextWatcher {

        private int position;
        private EditText editText;

        public CustomTextWatcher(EditText editText) {
            this.editText = editText;
        }

        public void setPosition(int position){
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            position = (Integer) editText.getTag();
            Log.d(Constants.LOG, "TextChanged " + editable.toString() + " position: " + position);



            int number;
            if (editable.toString().trim().isEmpty()) {
                number = -1;
            } else {
                number = Integer.valueOf(editable.toString().trim());
            }

            switch (editText.getId()){
                case R.id.absence_loose:
                    users.get(position).setLoose(number);
                    break;
                case R.id.absence_seek:
                    users.get(position).setSeek(number);
            }
        }
    }
}
