package com.ggu.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ggu.constants.Constants;
import com.ggu.database.DBContract;
import com.ggu.database.DBHelper;
import com.ggu.parsedclasses.University;
import com.ggu.activities.R;

import java.util.ArrayList;

/**
 * Created by Михаил on 25.10.2014.
 */
public class UnivercitiesListFragment extends Fragment {

    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ArrayList<University> universityArrayList;
    private Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_university_list, container, false);

        spinner = (Spinner) view.findViewById(R.id.universities_spinner);

        spinnerAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, getUniversities());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                saveUniversityIdInPref(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private void saveUniversityIdInPref(int position){
        pref= activity.getSharedPreferences(Constants.USER_DATA_PREF, activity.MODE_PRIVATE);
        editor = pref.edit();

        editor.putInt(Constants.UNIVERSITY_ID, universityArrayList.get(position).getId());
        editor.commit();
    }

    private String[] getUniversities(){

        getUniversitiesFromDb();

        String[] universities = new String[universityArrayList.size()];

        for (int i = 0; i < universityArrayList.size(); i++){
            universities[i] = universityArrayList.get(i).getName();
        }

        return universities;
    }

    private void getUniversitiesFromDb(){

        universityArrayList = new ArrayList<University>();

        DBHelper dbHelper= new DBHelper(activity);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(Constants.SELECT_QUERY+ DBContract.FeedsUniversity.TABLE_NAME, null);

        if (cursor.moveToFirst()) {

            do {
                University university = new University();
                university.setId(cursor.getInt(cursor.getColumnIndex(DBContract.FeedsUniversity._ID)));
                university.setName(cursor.getString(cursor.getColumnIndex(DBContract.FeedsUniversity.UNIVERSITY_NAME)));
                universityArrayList.add(university);
            }
            while (cursor.moveToNext());
        }

    }
}
