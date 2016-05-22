package com.ggu.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ggu.callbacks.GroupInformationCallBack;
import com.ggu.callbacks.SaveCallBack;
import com.ggu.constants.Constants;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.GroupInfrormation;
import com.ggu.parsedclasses.Student;

import java.util.ArrayList;

/**
 * Created by Михаил on 04.11.2014.
 */
public class ChangeGroupActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private int groupId, masterId;
    private EditText groupName;
    private Spinner groupMembersSpinner;
    private Button buttonCancel, buttonSave;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private GroupInfrormation groupInfrormation;
    private ArrayAdapter<String> adapterForSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_group);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        groupId = getIntent().getIntExtra(Constants.GROUP_ID, -1);

        groupName = (EditText) findViewById(R.id.edit_text_name_group);
        groupMembersSpinner = (Spinner) findViewById(R.id.group_members_spinner);
        buttonCancel = (Button) findViewById(R.id.edit_group_cancel);
        buttonSave = (Button) findViewById(R.id.edit_group_save);


        buttonCancel.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        String message = getResources().getString(R.string.wait);
        progressDialog.setMessage(message);

        preferences = getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE);

        if (groupId != -1) {
            getGroupInf();
        }

    }

    private void getGroupInf() {
        progressDialog.show();
        SendRequest getInformation = new SendRequest(this, getApiUrlForGroupInf(), Constants.GET, new GroupInformationCallBack() {
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                progressDialog.dismiss();
                groupInfrormation = (GroupInfrormation) response;
                showGroupInf();
            }

            @Override
            public void onError() {
                super.onError();
                progressDialog.dismiss();
            }
        });
        getInformation.execute();
    }

    private void showGroupInf() {
        ArrayList<Student> users = groupInfrormation.getUsers();
        String[] usersNames = getUsersNames(users);

        adapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, usersNames);
        adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        groupMembersSpinner.setAdapter(adapterForSpinner);

        groupName.setText(groupInfrormation.getName());
    }

    private String[] getUsersNames(ArrayList<Student> users) {
        String[] names = new String[users.size()];

        for (int i = 0; i < users.size(); i++) {
            names[i] = users.get(i).getName();
        }

        return names;
    }

    private String getApiUrlForGroupInf() {
        String apiUrl = Constants.API_URL + "action=view_group_info" + "&" + "id=" + preferences.getInt(Constants.USER_ID, -1) +
                "&" + "secret=" + preferences.getString(Constants.SECRET, "") + "&" + "group_id=" + groupId;

        return apiUrl;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_group_cancel:
                finish();
                break;
            case R.id.edit_group_save:
                if (groupId != -1) {
                    saveGroup(getApiUrlEdit());
                } else {
                    saveGroup(getApiUrlSave());
                }
                break;
        }
    }


    private void saveGroup(String apiUrl) {
        if (groupName.getText().toString().trim().length() > 0) {
            progressDialog.show();
            SendRequest getInformation = new SendRequest(this, apiUrl, Constants.POST, new SaveCallBack() {
                @Override
                public void onComplete(Object response) {
                    super.onComplete(response);
                    progressDialog.dismiss();
                    finish();
                }

                @Override
                public void onError() {
                    super.onError();
                    progressDialog.dismiss();
                    String message = getResources().getString(R.string.group_was_not_saved);
                    Toast.makeText(ChangeGroupActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });

            getInformation.execute();
        } else {
            String message = getResources().getString(R.string.empty_group_name);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private String getApiUrlSave() {


        String apiUrl = Constants.API_URL + "action=add_group" + "&" + "id=" + preferences.getInt(Constants.USER_ID, -1) +
                "&" + "secret=" + preferences.getString(Constants.SECRET, "") + "&" +
                "univercity=" + preferences.getInt(Constants.UNIVERSITY_ID, -1) + "&" + "group_name=" +
                groupName.getText().toString().replace(" ", "+") + "&" + "gm_id=" + masterId;

        return apiUrl;
    }

    private String getApiUrlEdit() {


        String apiUrl = Constants.API_URL + "action=admin_edit_group" + "&" + "id=" + preferences.getInt(Constants.USER_ID, -1) +
                "&" + "secret=" + preferences.getString(Constants.SECRET, "") + "&" +
                "group_id=" + groupId + "&" + "group_name=" +
                groupName.getText().toString().replace(" ", "+") + "&" + "gm_id=" + masterId;

        return apiUrl;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        masterId = groupInfrormation.getUsers().get(i).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
