package com.ggu.parsedclasses;

import java.util.ArrayList;

/**
 * Created by Михаил on 27.10.2014.
 */
public class GroupInfrormation {

    private int id;
    private String name;
    private int master;
    private int univercity;
    private ArrayList<Student> users = new ArrayList<Student>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaster() {
        return master;
    }

    public void setMaster(int master) {
        this.master = master;
    }

    public int getUnivercity() {
        return univercity;
    }

    public void setUnivercity(int univercity) {
        this.univercity = univercity;
    }

    public ArrayList<Student> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Student> users) {
        this.users = users;
    }
}
