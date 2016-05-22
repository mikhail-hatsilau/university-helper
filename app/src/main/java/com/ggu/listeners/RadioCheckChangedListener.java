package com.ggu.listeners;

import android.widget.RadioGroup;

import com.ggu.interfaces.IRadioChanged;

/**
 * Created by Михаил on 12.11.2014.
 */
public class RadioCheckChangedListener implements RadioGroup.OnCheckedChangeListener {

    private int position;
    private IRadioChanged radioChangedInt;

    public RadioCheckChangedListener(int position, IRadioChanged radioChangedInt){
        this.position=position;
        this.radioChangedInt = radioChangedInt;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        radioChangedInt.radioChecked(position, i);
    }
}
