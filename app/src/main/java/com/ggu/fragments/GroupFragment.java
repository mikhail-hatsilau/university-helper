package com.ggu.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ggu.activities.R;
import com.ggu.adapters.GroupListAdapter;
import com.ggu.callbacks.GroupCallBack;
import com.ggu.callbacks.SavePersonCallBack;
import com.ggu.constants.Constants;
import com.ggu.database.DBContract;
import com.ggu.database.DBHelper;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.NewUser;
import com.ggu.parsedclasses.Student;
import com.ggu.parsedclasses.Students;

import java.util.ArrayList;

/**
 * Created by Михаил on 25.10.2014.
 */
public class GroupFragment extends Fragment implements View.OnClickListener {


    private ListView list;
    private ProgressBar progressBar;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ArrayList<Student> students;
    private Button buttonSave, buttonAdd;
    private EditText editName, editEmail;
    private View footerView;
    private boolean footerFlag;
    private SharedPreferences pref;
    private GroupListAdapter adapter;
    private ProgressDialog progressDialog;
    private Students studs;
    private NewUser newUser;
    private Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        list = (ListView) view.findViewById(R.id.list_group);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_group);

        buttonAdd = (Button) view.findViewById(R.id.button_group_add);
        buttonSave = (Button) view.findViewById(R.id.button_group_save);

        footerView = LayoutInflater.from(activity).inflate(R.layout.add_new_person_footer, null);

        editName = (EditText) footerView.findViewById(R.id.edit_person_name);
        editEmail = (EditText) footerView.findViewById(R.id.edit_person_email);


        buttonAdd.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

        pref = activity.getSharedPreferences(Constants.USER_DATA_PREF, activity.MODE_PRIVATE);

        dbHelper = new DBHelper(activity);
        db = dbHelper.getReadableDatabase();

        cursor = db.rawQuery(Constants.SELECT_QUERY + DBContract.FeedsGroup.TABLE_NAME_GROUP + ";", null);

        if (!cursor.moveToFirst()) {
            progressBar.setVisibility(View.VISIBLE);
            SendRequest getInf = new SendRequest(activity, generateApi(), Constants.GET, new GroupCallBack() {
                @Override
                public void onComplete(Object response) {
                    super.onComplete(response);
                    progressBar.setVisibility(View.GONE);

                    studs = (Students) response;
                    getStudentsList();
                    writeGroupToDb();
                    showStudents();
                }

                @Override
                public void onError() {
                    super.onError();
                    progressBar.setVisibility(View.GONE);
                }
            });

            getInf.execute();

        } else {
            students = setListFromDb();
            showStudents();

        }

        return view;
    }

    private void showStudents() {
        list.addFooterView(footerView);
        adapter = new GroupListAdapter(activity, students);
        list.setAdapter(adapter);
        list.removeFooterView(footerView);
    }

    private void getStudentsList() {
        students = studs.getUsers();
    }

    private void writeGroupToDb() {
        DBHelper dbHelper = new DBHelper(activity);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (int i = 0; i < students.size(); i++) {
            ContentValues cv = new ContentValues();

            cv.put(DBContract.FeedsGroup._ID, students.get(i).getId());
            cv.put(DBContract.FeedsGroup.PERSON_NAME, students.get(i).getName());

            db.insert(DBContract.FeedsGroup.TABLE_NAME_GROUP, null, cv);
        }

        dbHelper.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Constants.LOG, "onResumeUsers");
    }

    @Override
    public void onPause() {
        super.onPause();
        list.removeFooterView(footerView);
        footerFlag = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_group_add:
                if (!footerFlag) {
                    list.addFooterView(footerView);
                    list.setSelection(adapter.getCount());
                    editName.setText("");
                    editEmail.setText("");
                    footerFlag = true;
                }
                break;
            case R.id.button_group_save:
                if (footerFlag) {
                    savePerson();
                }
                break;
        }
    }

    private void savePerson() {
        if (editName.getText().toString().trim().equals("")) {
            Toast.makeText(activity, getResources().getString(R.string.wrong_name), Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(getResources().getString(R.string.wait));
        progressDialog.show();

        SendRequest getInf = new SendRequest(activity, generateApiSavePerson(), Constants.POST, new SavePersonCallBack() {
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                progressDialog.dismiss();
                Toast.makeText(activity, getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                newUser = (NewUser) response;
                updateList();
                writeNewUserToDb();
                footerFlag = false;
                list.removeFooterView(footerView);
            }

            @Override
            public void onError() {
                super.onError();
                progressDialog.dismiss();
                Toast.makeText(activity, getResources().getString(R.string.user_was_not_save), Toast.LENGTH_SHORT).show();
            }
        });
        getInf.execute();


    }

    private void updateList() {
        Student student = new Student();
        student.setId(newUser.getUser_id());
        student.setName(editName.getText().toString());
        students.add(students.size(), student);
        adapter.notifyDataSetChanged();
    }

    private void writeNewUserToDb() {


        DBHelper dbHepler = new DBHelper(activity);
        SQLiteDatabase db = dbHepler.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(DBContract.FeedsGroup._ID, students.get(students.size() - 1).getId());
        cv.put(DBContract.FeedsGroup.PERSON_NAME, students.get(students.size() - 1).getName());

        db.insert(DBContract.FeedsGroup.TABLE_NAME_GROUP, null, cv);

        dbHepler.close();
    }

    private String generateApiSavePerson() {

        String api = Constants.API_URL + "action=add_group_user" + "&" + "id=" + pref.getInt(Constants.USER_ID, -1) + "&" +
                "secret=" + pref.getString(Constants.SECRET, "") + "&" + "group=" + pref.getInt(Constants.GROUP, -1) + "&" +
                "name=" + editName.getText().toString().replace(" ", "+") + "&" + "email=" + editEmail.getText().toString();

        return api;
    }

    private ArrayList<Student> setListFromDb() {

        ArrayList<Student> studentsList = new ArrayList<Student>();

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(cursor.getInt(cursor.getColumnIndex(DBContract.FeedsGroup._ID)));
                student.setName(cursor.getString(cursor.getColumnIndex(DBContract.FeedsGroup.PERSON_NAME)));
                studentsList.add(student);
            }
            while (cursor.moveToNext());
        }

        dbHelper.close();

        return studentsList;
    }

    private String generateApi() {

        Log.d(Constants.LOG, "GroupActivity: generateApi()");
        String api = Constants.API_URL + "action=group_users" + "&" + "id=" + pref.getInt(Constants.USER_ID, -1) + "&" + "secret=" +
                pref.getString(Constants.SECRET, "") + "&" + "group=" + pref.getInt(Constants.GROUP, -1);

        Log.d(Constants.LOG, "GroupActivity: " + api);

        return api;
    }

}
