package com.ggu.callbacks;

/**
 * Created by Михаил on 16.10.2014.
 */
public abstract class CallBack {

    public void onComplete(Object response){}
    public void onError(){}

    public abstract Object getResponse(String json);

}
