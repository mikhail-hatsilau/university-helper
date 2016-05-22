package com.ggu.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ggu.activities.R;
import com.ggu.callbacks.DeleteCallBack;
import com.ggu.constants.Constants;
import com.ggu.database.DBContract;
import com.ggu.database.DBHelper;
import com.ggu.interfaces.IDelete;
import com.ggu.listeners.ClickListenerForDeletePerson;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.Student;

import java.util.ArrayList;

/**
 * Created by Михаил on 30.09.2014.
 */
public class GroupListAdapter extends BaseAdapter implements IDelete{

    private ArrayList<Student> students;
    private Context context;
    private ProgressDialog progressDialog;

    public GroupListAdapter(Context context, ArrayList<Student> students) {
        this.context = context;
        this.students = students;
    }

    @Override
    public int getCount() {
        return students.size();
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

        //position = i;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.group_list_item, viewGroup, false);
            TextView textName = (TextView) view.findViewById(R.id.text_name_stud);
            ImageButton delete_button = (ImageButton) view.findViewById(R.id.image_btn_delete_student);
            ViewHolder vh = new ViewHolder(textName, delete_button);
            view.setTag(vh);
        }

        ViewHolder vh = (ViewHolder) view.getTag();

        vh.textName.setText(students.get(i).getName());
        vh.delete_button.setOnClickListener(new ClickListenerForDeletePerson(i, this));

        return view;
    }

    @Override
    public void deleteItem(int pos) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.wait));
        progressDialog.show();

        final int itemPosition = pos;

        SendRequest getInf = new SendRequest(context, getApiUrl(pos), Constants.POST, new DeleteCallBack(){
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);

                deleteItemIntoDb(itemPosition);
                students.remove(itemPosition);
                notifyDataSetChanged();

                progressDialog.dismiss();

            }

            @Override
            public void onError() {
                super.onError();
                progressDialog.dismiss();
            }
        });
        getInf.execute();
    }

    private void deleteItemIntoDb(int itemPos) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String tableName = DBContract.FeedsGroup.TABLE_NAME_GROUP;

        db.delete(tableName, DBContract.FeedsGroup._ID + " = ?", new String[]{String.valueOf(students.get(itemPos).getId())});

        dbHelper.close();
    }

    private String getApiUrl(int position) {
        SharedPreferences pref = context.getSharedPreferences(Constants.USER_DATA_PREF, Context.MODE_PRIVATE);
        String api = Constants.API_URL + "action=remove_user" + "&" + "id=" + pref.getInt(Constants.USER_ID, -1) +
                "&" + "secret=" + pref.getString(Constants.SECRET, "") + "&" + "group=" + pref.getInt(Constants.GROUP, -1) +
                "&" + "user_id=" + students.get(position).getId();

        Log.d(Constants.LOG, api);

        return api;
    }

    private class ViewHolder {

        public TextView textName;
        public ImageButton delete_button;

        public ViewHolder(TextView textName, ImageButton delete_button) {
            this.textName = textName;
            this.delete_button = delete_button;
        }
    }
}
