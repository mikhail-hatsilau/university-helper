package com.ggu.listeners;

import android.view.View;

import com.ggu.interfaces.IDelete;

/**
 * Created by Михаил on 21.10.2014.
 */
public class ClickListenerForDeletePerson implements View.OnClickListener {

    private int pos;
    private IDelete adapter;

    public ClickListenerForDeletePerson(int pos, IDelete adapter){
        this.pos = pos;
        this.adapter = adapter;
    }

    @Override
    public void onClick(View view) {
        adapter.deleteItem(pos);
    }
}
