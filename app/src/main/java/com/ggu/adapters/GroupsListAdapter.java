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
import com.ggu.parsedclasses.Group;
import com.ggu.activities.R;

import java.util.ArrayList;

/**
 * Created by Михаил on 24.10.2014.
 */
public class GroupsListAdapter extends BaseAdapter implements IDelete{

    private Context context;
    private ArrayList<Group> groupList;
    private ProgressDialog progressDialog;

    public GroupsListAdapter(Context context, ArrayList<Group> groupList) {
        this.groupList = groupList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return groupList.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.groups_admin_list_item, viewGroup, false);

            TextView nameGroup = (TextView) view.findViewById(R.id.group_name);
            ImageButton deleteButton = (ImageButton) view.findViewById(R.id.button_delete_group);

            ViewHolder vh = new ViewHolder();
            vh.nameGroup = nameGroup;
            vh.deleteButton = deleteButton;

            view.setTag(vh);
        }

        ViewHolder vh = (ViewHolder) view.getTag();

        vh.nameGroup.setText(groupList.get(i).getName());

        vh.deleteButton.setOnClickListener(new ClickListenerForDeletePerson(i, this));

        return view;
    }


    private String getApiUrl(int position){
        SharedPreferences preferences = context.getSharedPreferences(Constants.USER_DATA_PREF, context.MODE_PRIVATE);

        String apiUrl = Constants.API_URL+"action=remove_group"+"&"+"id="+preferences.getInt(Constants.USER_ID,-1)+
                "&"+"secret="+preferences.getString(Constants.SECRET, "")+"&"+"univercity="+preferences.getInt(Constants.UNIVERSITY_ID,-1)+
                "&"+"group_id="+groupList.get(position).getId();

        return apiUrl;
    }

    @Override
    public void deleteItem(int position) {
        final int pos = position;

        String message = context.getResources().getString(R.string.wait);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.show();

        SendRequest getInformation = new SendRequest(context, getApiUrl(position), Constants.POST, new DeleteCallBack(){
            @Override
            public void onComplete(Object response) {
                super.onComplete(response);
                progressDialog.dismiss();
                groupList.remove(pos);
                notifyDataSetChanged();

                String message = context.getResources().getString(R.string.deleted);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                super.onError();
                progressDialog.dismiss();
            }
        });

        getInformation.execute();
    }

    private class ViewHolder {
        public TextView nameGroup;
        public ImageButton deleteButton;
    }
}
