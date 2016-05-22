package com.ggu.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ggu.callbacks.DeleteCallBack;
import com.ggu.constants.Constants;
import com.ggu.interfaces.IDelete;
import com.ggu.listeners.ClickListenerForDeletePerson;
import com.ggu.loaders.SendRequest;
import com.ggu.parsedclasses.User;
import com.ggu.activities.R;

import java.util.ArrayList;

/**
 * Created by Михаил on 28.10.2014.
 */
public class AdminUsersListAdapter extends BaseAdapter implements IDelete {

    private Context context;
    private ArrayList<User> users;
    private ProgressDialog progressDialog;

    public AdminUsersListAdapter(Context context, ArrayList<User> users) {
        this.users = users;
        this.context = context;
    }

    @Override
    public int getCount() {
        return users.size();
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

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.group_list_item, viewGroup, false);
            TextView name = (TextView) view.findViewById(R.id.text_name_stud);
            ImageButton deleteButton = (ImageButton) view.findViewById(R.id.image_btn_delete_student);
            ViewHolder vh = new ViewHolder();
            vh.name = name;
            vh.deleteButton = deleteButton;
            view.setTag(vh);
        }

        ViewHolder vh = (ViewHolder) view.getTag();

        vh.name.setText(users.get(i).getName());
        vh.deleteButton.setOnClickListener(new ClickListenerForDeletePerson(i, this));

        return view;
    }

    @Override
    public void deleteItem(final int position) {
        String message = context.getResources().getString(R.string.wait);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.show();

        SendRequest getInformation = new SendRequest(context, getApiUrl(position), Constants.POST, new DeleteCallBack() {
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                progressDialog.dismiss();
                users.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onError() {
                super.onError();
                progressDialog.dismiss();
                Toast.makeText(context, context.getResources().getString(R.string.user_was_not_deleted), Toast.LENGTH_SHORT).show();
            }
        });
        getInformation.execute();
    }

    private String getApiUrl(int position) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.USER_DATA_PREF, Context.MODE_PRIVATE);

        String apiUrl = Constants.API_URL + "action=admin_remove_user" + "&" + "id=" + preferences.getInt(Constants.USER_ID, -1) +
                "&" + "secret=" + preferences.getString(Constants.SECRET, "") +
                "&" + "user_id=" + users.get(position).getId();

        return apiUrl;
    }

    private class ViewHolder {
        public TextView name;
        public ImageButton deleteButton;
    }
}
