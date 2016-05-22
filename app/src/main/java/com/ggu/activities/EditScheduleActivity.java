package com.ggu.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ggu.adapters.EditSubjectsListAdapter;
import com.ggu.callbacks.SaveCallBack;
import com.ggu.constants.Constants;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.Day;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Михаил on 17.09.2014.
 */
public class EditScheduleActivity extends Activity implements View.OnClickListener {

    private Map<Integer, Day> daySchedule;
    private TextView headText;
    private String nameDay;
    private int dayNumber;
    private ListView list;
    private EditSubjectsListAdapter adapter;
    private Button saveButton, cancelButton;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.wait));

        headText = (TextView) findViewById(R.id.head_text);

        daySchedule = (HashMap<Integer, Day>) getIntent().getSerializableExtra(Constants.DAY_OF_WEEK);
        nameDay = getIntent().getStringExtra(Constants.NAME_DAY);
        dayNumber = getIntent().getIntExtra(Constants.DAY_NUMBER, -1);


        headText.setText(nameDay);

        list = (ListView) findViewById(R.id.list_edit_subject);
        cancelButton = (Button) findViewById(R.id.button_cancel);
        saveButton = (Button) findViewById(R.id.button_save);

        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        adapter = new EditSubjectsListAdapter(this, daySchedule);
        list.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_cancel:
                finish();
                break;
            case R.id.button_save:
                saveSchedule();
                break;
        }
    }

    private void saveSchedule() {
        daySchedule = adapter.getSubjects();

        for (int i = 0; i < adapter.getCount(); i++) {
            if (daySchedule.containsKey(i)) {
                Day day = daySchedule.get(i);
                if ("".equals(day.getName())) {
                    showToast(getResources().getString(R.string.empty_subject_name));
                    return;
                }

                if ("".equals(day.getTime())) {
                    showToast(getResources().getString(R.string.empty_subject_time));
                    return;
                }

                if ("".equals(day.getLocation())) {
                    showToast(getResources().getString(R.string.empty_subject_location));
                    return;
                }
            }
        }

        progressDialog.show();
        SendRequest getInformation = new SendRequest(this, getApiForSaveSchedule(), Constants.POST, new SaveCallBack() {
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                progressDialog.dismiss();
                Toast.makeText(EditScheduleActivity.this, EditScheduleActivity.this.getResources().getString(R.string.saved),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                super.onError();
                progressDialog.dismiss();
            }
        });

        getInformation.execute();

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String getApiForSaveSchedule() {

        StringBuilder apiUrl = new StringBuilder();

        apiUrl.append(Constants.API_URL + "action=shed_save" + "&" + "id=" + preferences.getInt(Constants.USER_ID, -1) +
                "&" + "secret=" + preferences.getString(Constants.SECRET, "") +
                "&" + "group=" + preferences.getInt(Constants.GROUP, -1) +
                "&" + "day=" + dayNumber);


        int numberOfSubject = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            if (daySchedule.containsKey(i)) {
                numberOfSubject++;
                Day day = daySchedule.get(i);
                apiUrl.append("&" + "line" + numberOfSubject + "=" + day.getTime() + Constants.SYMBOLS_UNION + day.getName() +
                        Constants.SYMBOLS_UNION + day.getLocation() + Constants.SYMBOLS_UNION + day.getType());
            }
        }

        return apiUrl.toString();
    }

}
