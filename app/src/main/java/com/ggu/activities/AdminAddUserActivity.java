package com.ggu.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ggu.callbacks.GroupListCallBack;
import com.ggu.callbacks.SaveCallBack;
import com.ggu.constants.Constants;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.GroupList;

/**
 * Created by Михаил on 04.11.2014.
 */
public class AdminAddUserActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private Button buttonCancel, buttonSave;
    private EditText editName, editEmail, editPassword;
    private CheckBox checkBoxIsMaster;
    private Spinner groupsSpinner;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;
    private GroupList groups;
    private ArrayAdapter<String> spinnerAdapter;
    private int groupId, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        buttonCancel = (Button) findViewById(R.id.admin_edit_user_cancel);
        buttonSave = (Button) findViewById(R.id.admin_edit_user_save);
        editName = (EditText) findViewById(R.id.admin_edit_user_name);
        editEmail = (EditText) findViewById(R.id.admin_edit_user_email);
        editPassword = (EditText) findViewById(R.id.admin_edit_user_password);
        checkBoxIsMaster = (CheckBox) findViewById(R.id.admin_edit_user_is_master);
        groupsSpinner = (Spinner) findViewById(R.id.admin_edit_user_groups_spinner);

        buttonCancel.setOnClickListener(this);
        buttonSave.setOnClickListener(this);


        preferences = getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE);

        String message = getResources().getString(R.string.wait);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);

        getGroupsList();
    }

    private void getGroupsList() {
        progressDialog.show();
        SendRequest getInformation = new SendRequest(this, getApiUrlGroups(), Constants.GET, new GroupListCallBack() {
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                progressDialog.dismiss();
                groups = (GroupList) response;
                showGroupsInSpinner();
            }

            @Override
            public void onError() {
                super.onError();
                progressDialog.dismiss();
            }
        });

        getInformation.execute();
    }

    private void showGroupsInSpinner() {
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getGroupsNames());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupsSpinner.setAdapter(spinnerAdapter);
        groupsSpinner.setOnItemSelectedListener(this);
    }

    private String[] getGroupsNames() {
        String[] names = new String[groups.getGroups().size()];

        for (int i = 0; i < groups.getGroups().size(); i++) {
            names[i] = groups.getGroups().get(i).getName();
        }

        return names;
    }

    private String getApiUrlGroups() {
        String apiUrl = Constants.API_URL + "action=get_groups_list" + "&" + "id=" + preferences.getInt(Constants.USER_ID, -1) +
                "&" + "secret=" + preferences.getString(Constants.SECRET, "") +
                "&" + "univercity=" + preferences.getInt(Constants.UNIVERSITY_ID, -1);

        Log.d(Constants.LOG, apiUrl);

        return apiUrl;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.admin_edit_user_cancel:
                finish();
                break;
            case R.id.admin_edit_user_save:
                saveUser();
        }
    }

    private void saveUser() {
        if (editName.getText().toString().trim().length() == 0) {
            Toast.makeText(this, getResources().getString(R.string.admin_add_user_empty_name), Toast.LENGTH_SHORT).show();
            return;
        }

        if (editEmail.getText().toString().trim().length() == 0) {
            Toast.makeText(this, getResources().getString(R.string.admin_add_user_empty_email), Toast.LENGTH_SHORT).show();
            return;
        }

        if (editPassword.getText().toString().trim().length() == 0) {
            Toast.makeText(this, getResources().getString(R.string.admin_add_user_empty_password), Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        SendRequest getInformation = new SendRequest(this, getApiUrl(), Constants.POST, new SaveCallBack() {
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                progressDialog.dismiss();
                Toast.makeText(AdminAddUserActivity.this, getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError() {
                super.onError();
                progressDialog.dismiss();
                Toast.makeText(AdminAddUserActivity.this, getResources().getString(R.string.user_was_not_save), Toast.LENGTH_SHORT).show();
            }
        });

        getInformation.execute();
    }

    private String getApiUrl() {

        role = checkBoxIsMaster.isChecked() ? 1 : 0;

        String apiUrl = Constants.API_URL + "action=add_new_user" + "&" + "id=" + preferences.getInt(Constants.USER_ID, -1) +
                "&" + "secret=" + preferences.getString(Constants.SECRET, "") +
                "&" + "name=" + editName.getText().toString().replace(" ","+") +
                "&" + "group=" + groupId + "&" + "email=" + editEmail.getText().toString().replace(" ","+") +
                "&" + "pass=" + editPassword.getText().toString().replace(" ","") + "&" + "role="+role+
                "&"+"univercity="+preferences.getInt(Constants.UNIVERSITY_ID,-1);

        return apiUrl;

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        groupId = groups.getGroups().get(i).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
