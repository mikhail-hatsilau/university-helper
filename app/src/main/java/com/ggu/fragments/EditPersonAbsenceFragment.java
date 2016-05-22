package com.ggu.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.ggu.adapters.AbsenceListAdapter;
import com.ggu.callbacks.AbsenceCallBack;
import com.ggu.callbacks.SaveCallBack;
import com.ggu.constants.Constants;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.AbsenceStat;
import com.ggu.parsedclasses.UserAbsence;
import com.ggu.activities.R;

import java.util.ArrayList;

/**
 * Created by Михаил on 25.10.2014.
 */
public class EditPersonAbsenceFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private ListView list;
    private String api;
    private ProgressBar progressBar;
    private AbsenceListAdapter absenceListAdapter;
    private Button buttonSave;
    private SharedPreferences pref;
    private int month;
    private ArrayList<UserAbsence> usersAbsenceList;
    private ProgressDialog progressDialog;
    private AbsenceStat absenceStat;
    private Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_absence, container, false);


        spinner = (Spinner) view.findViewById(R.id.spinner_months);
        list = (ListView) view.findViewById(R.id.absence_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_absence);
        buttonSave = (Button) view.findViewById(R.id.button_absence_save);


        spinnerAdapter = ArrayAdapter.createFromResource(activity, R.array.months, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(this);

        buttonSave.setOnClickListener(this);

        pref = activity.getSharedPreferences(Constants.USER_DATA_PREF, activity.MODE_PRIVATE);

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getResources().getString(R.string.wait));


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private String getApiUrl() {

        String api = Constants.API_URL + "action=master_stat_month" + "&" + "id=" + pref.getInt(Constants.USER_ID, -1) + "&" +
                "secret=" + pref.getString(Constants.SECRET, "") + "&" + "month=" + month;
        return api;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        month = i;
        api = getApiUrl();

        usersAbsenceList = new ArrayList<UserAbsence>();

        showList();

        progressBar.setVisibility(View.VISIBLE);

        SendRequest getInf = new SendRequest(activity, api, Constants.GET, new AbsenceCallBack() {
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                progressBar.setVisibility(View.GONE);
                absenceStat = (AbsenceStat) response;
                usersAbsenceList = absenceStat.getStat();
                showList();
            }

            @Override
            public void onError() {
                super.onError();
                progressBar.setVisibility(View.GONE);

            }
        });
        getInf.execute();

    }

    private void showList() {
        absenceListAdapter = new AbsenceListAdapter(activity, usersAbsenceList);
        list.setAdapter(absenceListAdapter);
    }


    private String getApiForSave(ArrayList<UserAbsence> users) {

        StringBuilder api = new StringBuilder();

        api.append(Constants.API_URL + "action=master_stat_save" + "&" + "id=" + pref.getInt(Constants.USER_ID, -1) + "&" + "secret=" +
                pref.getString(Constants.SECRET, "") + "&" + "month=" + month + "&" + "group=" + pref.getInt(Constants.GROUP, -1));

        for (int i = 0; i < users.size(); i++) {
            api.append("&" + "line" + (i + 1) + "=" + users.get(i).getId() + Constants.SYMBOLS_UNION + users.get(i).getLoose() +
                    Constants.SYMBOLS_UNION + users.get(i).getSeek());
        }

        Log.d(Constants.LOG, api.toString());

        return api.toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_absence_save:
                saveAbsence();
                break;
        }
    }

    private void saveAbsence() {

        //Log.d(Constants.LOG,""+absenceListAdapter.getUsers().get(0).getLoose());

        ArrayList<UserAbsence> users = absenceListAdapter.getUsers();

        for (UserAbsence user : users) {
            if ((user.getSeek() < 0) || (user.getLoose() < 0)) {
                Toast.makeText(activity, activity.getResources().getString(R.string.wrong_absence), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        progressDialog.show();
        SendRequest getInf = new SendRequest(activity, getApiForSave(users), Constants.POST, new SaveCallBack() {
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                progressDialog.dismiss();
                String message = activity.getResources().getString(R.string.saved);
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                super.onError();
                progressDialog.dismiss();
                String message = activity.getResources().getString(R.string.absence_was_not_saved);
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });

        getInf.execute();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
