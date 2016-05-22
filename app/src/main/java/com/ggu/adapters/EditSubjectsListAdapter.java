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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ggu.activities.R;
import com.ggu.constants.Constants;
import com.ggu.interfaces.IDelete;
import com.ggu.interfaces.IRadioChanged;
import com.ggu.listeners.ClickListenerForDeletePerson;
import com.ggu.listeners.RadioCheckChangedListener;
import com.ggu.parsedclasses.Day;

import java.util.Map;

/**
 * Created by Михаил on 29.09.2014.
 */
public class EditSubjectsListAdapter extends BaseAdapter implements IDelete, IRadioChanged {

    private final int numberOfSubjects = 6;

    private Context context;
    private Map<Integer, Day> subjects;
    private EditText editTextTime, editTextName, editTextCab;
    private ImageButton deleteSubjectButton;
    private RadioGroup radioGroup;

    public EditSubjectsListAdapter(Context context, Map<Integer, Day> subjects) {
        this.context = context;
        this.subjects = subjects;
    }

    public Map<Integer, Day> getSubjects() {
        return subjects;
    }

    @Override
    public int getCount() {
        return numberOfSubjects;
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

        view = LayoutInflater.from(context).inflate(R.layout.edit_schedule_item, viewGroup, false);
        editTextTime = (EditText) view.findViewById(R.id.edit_time);
        editTextName = (EditText) view.findViewById(R.id.edit_name_subject);
        editTextCab = (EditText) view.findViewById(R.id.edit_cabinet);
        deleteSubjectButton = (ImageButton) view.findViewById(R.id.delete_subject);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioCheckChangedListener(i, this));

        deleteSubjectButton.setOnClickListener(new ClickListenerForDeletePerson(i, this));

        if (subjects.containsKey(i)) {
            editTextTime.setText(subjects.get(i).getTime());
            editTextName.setText(subjects.get(i).getName());
            editTextCab.setText(subjects.get(i).getLocation());
            switch (subjects.get(i).getType()){
                case 1:
                    ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
                    break;
                case 2:
                    ((RadioButton)radioGroup.getChildAt(1)).setChecked(true);
                    break;
                default:
                    ((RadioButton)radioGroup.getChildAt(2)).setChecked(true);
            }
        }

        editTextTime.addTextChangedListener(new EditTextChangedWatcher(editTextTime, editTextName, editTextCab, i));
        editTextName.addTextChangedListener(new EditTextChangedWatcher(editTextTime, editTextName, editTextCab, i));
        editTextCab.addTextChangedListener(new EditTextChangedWatcher(editTextTime, editTextName, editTextCab, i));

        return view;
    }

    @Override
    public void deleteItem(int position) {
        if (subjects.containsKey(position)) {
            subjects.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public void radioChecked(int position, int id) {

        if (!subjects.containsKey(position)) {
            return;
        }

        switch (id) {
            case R.id.radio_over_line:
                subjects.get(position).setType(1);
                break;
            case R.id.radio_under_line:
                subjects.get(position).setType(2);
                break;
            case R.id.radio_none:
                subjects.get(position).setType(0);
                break;
        }
    }


    private class EditTextChangedWatcher implements TextWatcher {

        private EditText time, name, cabinet;
        private int position;


        public EditTextChangedWatcher(EditText time, EditText name, EditText cabinet, int position) {
            this.time = time;
            this.name = name;
            this.cabinet = cabinet;
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

            Log.d(Constants.LOG, "text Changed" + editable);

            if (subjects.containsKey(position)) {
                if (name.getText().hashCode() == editable.hashCode()) {

                    if ("".equals(editable.toString())) {

                        if (("".equals(subjects.get(position).getTime())) && ("".equals(subjects.get(position).getLocation()))) {
                            subjects.remove(position);
                        } else {
                            subjects.get(position).setName(editable.toString());
                        }
                    } else {
                        subjects.get(position).setName(editable.toString());
                    }

                } else {
                    if (time.getText().hashCode() == editable.hashCode()) {
                        if ("".equals(editable.toString())) {

                            if (("".equals(subjects.get(position).getName())) && ("".equals(subjects.get(position).getLocation()))) {
                                subjects.remove(position);
                            } else {
                                subjects.get(position).setTime(editable.toString());
                            }
                        } else {
                            subjects.get(position).setTime(editable.toString());
                        }
                    } else {
                        if ("".equals(editable.toString())) {

                            if (("".equals(subjects.get(position).getName())) && ("".equals(subjects.get(position).getTime()))) {
                                subjects.remove(position);
                            } else {
                                subjects.get(position).setLocation(editable.toString());
                            }

                        } else {
                            subjects.get(position).setLocation(editable.toString());
                        }
                    }
                }

            } else {
                subjects.put(position, putNewSubject(name, time, cabinet));
            }

        }

        private Day putNewSubject(EditText name, EditText time, EditText location) {
            Day day = new Day();
            day.setName(name.getText().toString());
            day.setTime(time.getText().toString());
            day.setLocation(location.getText().toString());
            return day;
        }

    }
}
