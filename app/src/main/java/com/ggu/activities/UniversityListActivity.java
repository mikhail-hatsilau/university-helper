package com.ggu.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ggu.callbacks.UniversityListCallBack;
import com.ggu.constants.Constants;
import com.ggu.database.DBContract;
import com.ggu.database.DBHelper;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.Univercities;
import com.ggu.parsedclasses.University;

import java.util.ArrayList;

/**
 * Created by Михаил on 24.10.2014.
 */
public class UniversityListActivity extends Activity implements View.OnClickListener{

    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ArrayList<University> universityArrayList;
    private Button confirmButton;
    private int position;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_list);

        spinner = (Spinner) findViewById(R.id.universities_spinner);
        confirmButton = (Button) findViewById(R.id.confirm_university);
        confirmButton.setOnClickListener(this);

        pref= getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE);

        getUniversities();



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                position = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(Constants.LOG, "onNothingSelected");
            }
        });
    }

    @Override
    public void onClick(View view) {
        saveUniversityIdInPref(position);
        UniversityListActivity.this.setResult(RESULT_OK);
        UniversityListActivity.this.finish();
    }

    private void showSpinner(){
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getUniversitiesNames());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    private void getUniversities(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.wait));
        progressDialog.show();

        SendRequest getInformation = new SendRequest(this, getApiUrl(), Constants.GET, new UniversityListCallBack(){
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                progressDialog.dismiss();
                universityArrayList = ((Univercities) response).getUnivercity_list();
                showSpinner();
            }

            @Override
            public void onError() {
                super.onError();
                progressDialog.dismiss();
            }
        });

        getInformation.execute();
    }

    private String getApiUrl(){
        String apiUrl = Constants.API_URL+"action=get_univercity_list"+"&"+"id="+pref.getInt(Constants.USER_ID,-1)+
                "&"+"secret="+pref.getString(Constants.SECRET,"");

        return apiUrl;
    }

    private void saveUniversityIdInPref(int position){

        editor = pref.edit();

        editor.putInt(Constants.UNIVERSITY_ID, universityArrayList.get(position).getId());
        editor.commit();
    }

    private String[] getUniversitiesNames(){

        //getUniversitiesFromDb();

        String[] universities = new String[universityArrayList.size()];

        for (int i = 0; i < universityArrayList.size(); i++){
            universities[i] = universityArrayList.get(i).getName();
        }

        return universities;
    }

    private void getUniversitiesFromDb(){

        universityArrayList = new ArrayList<University>();

        DBHelper dbHelper= new DBHelper(this);
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
