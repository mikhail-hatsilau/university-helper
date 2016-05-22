package com.ggu.parsedclasses;

import java.util.ArrayList;

/**
 * Created by Михаил on 28.10.2014.
 */
public class AdminUsers {

    private ArrayList<User> users = new ArrayList<User>();

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
