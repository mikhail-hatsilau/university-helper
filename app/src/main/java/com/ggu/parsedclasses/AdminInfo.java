package com.ggu.parsedclasses;

import java.util.ArrayList;

/**
 * Created by Михаил on 24.10.2014.
 */
public class AdminInfo extends UserInfo{

//    private int id;
//    private String name;
//    private int group;
//    private int role;
//    private String university;
//    private String group_name;
    private ArrayList<University> univercity = new ArrayList<University>();

   
    public ArrayList<University> getUnivercity() {
        return univercity;
    }

    public void setUnivercity(ArrayList<University> univercity) {
        this.univercity = univercity;
    }
}
