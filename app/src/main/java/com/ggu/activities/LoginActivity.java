package com.ggu.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ggu.callbacks.LoginCallBack;
import com.ggu.constants.Constants;
import com.ggu.converters.Converter;
import com.ggu.database.DataBaseOperations;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.AdminInfo;
import com.ggu.parsedclasses.StudInfo;
import com.ggu.parsedclasses.UserInfo;

/**
 * Created by Михаил on 08.07.2014.
 */
public class LoginActivity extends Activity implements View.OnClickListener {


    private ActionBar bar;
    private Button loginButton, webButton;
    private EditText editLogin, editPass;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        bar = getActionBar();
        bar.hide();

        loginButton = (Button) findViewById(R.id.login_button);
        webButton = (Button) findViewById(R.id.website_button);
        editLogin = (EditText) findViewById(R.id.email_edit);
        editPass = (EditText) findViewById(R.id.pass_edit);

        webButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.login_button:
                loginUser();
                break;
            case R.id.website_button:
                openBrowser();
                break;
        }

    }

    public void loginUser() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.wait));

        if (editLogin.getText().toString().trim().length() == 0) {
            Toast.makeText(this, getResources().getString(R.string.enter_login), Toast.LENGTH_SHORT).show();
        } else {
            if (editPass.getText().toString().trim().length() == 0) {
                Toast.makeText(this, getResources().getString(R.string.enter_pass), Toast.LENGTH_SHORT).show();
            } else {

                progressDialog.show();
                String md5Pass = Converter.md5(editPass.getText().toString().trim() + Constants.SECRET_LOGIN_KEY);

                String apiUrlQuery = Constants.API_URL + "action=login" + "&login=" + editLogin.getText().toString().trim() + "&password=" + md5Pass;

                SendRequest request = new SendRequest(this, apiUrlQuery, Constants.POST, new LoginCallBack() {
                    @Override
                    public void onComplete(Object response) {
                        super.onComplete(response);

                        if (response instanceof StudInfo) {
                            StudInfo studInfo = (StudInfo) response;
                            if (studInfo.getShedule() != null) {
                                putInfoToDb(studInfo);
                            } else {
                                saveInfToPref(studInfo);
                            }
                        } else {
                            AdminInfo adminInfo = (AdminInfo) response;
                            saveInfToPref(adminInfo);
                        }


                        progressDialog.dismiss();
                        LoginActivity.this.setResult(RESULT_OK);
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
                    }
                });
                request.execute();
            }
        }


    }

//    private void putAdminInfoToDb(){
//
//        ArrayList<University> universities = adminInfo.getUnivercity();
//
//        dbHelper = new DBHelper(this);
//        db = dbHelper.getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//
//        for (University university : universities) {
//            cv.put(DBContract.FeedsUniversity._ID, university.getId());
//            cv.put(DBContract.FeedsUniversity.UNIVERSITY_NAME, university.getName());
//            db.insert(DBContract.FeedsUniversity.TABLE_NAME, null, cv);
//        }
//
//        db.close();
//
//        saveInfToPref(adminInfo);
//
//    }

    private void putInfoToDb(StudInfo studInfo) {

        for (int i = 0; i < studInfo.getShedule().size(); i++) {
            DataBaseOperations.writeScheduleToDataBase(studInfo.getShedule().get(i), i, this);
        }
        saveInfToPref(studInfo);

    }

    private void saveInfToPref(UserInfo userInfo) {

        SharedPreferences preferences = getSharedPreferences(Constants.USER_DATA_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        String secret = Converter.md5(userInfo.getId() + Constants.SECRET_KEY + editPass.getText().toString().trim());
        editor = preferences.edit();
        editor.putInt(Constants.USER_ID, userInfo.getId());
        editor.putString(Constants.NAME, userInfo.getName());
        editor.putString(Constants.SECRET, secret);
        editor.putInt(Constants.GROUP, userInfo.getGroup());
        editor.putInt(Constants.ROLE, userInfo.getRole());
        editor.putString(Constants.UNIVERSITY, userInfo.getUniversity());
        editor.putString(Constants.GROUP_NAME, userInfo.getGroup_name());
        editor.commit();

    }


    public void openBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Constants.UNIVERSITY_URL));
        startActivity(intent);
    }
}
