package com.ggu.parsedclasses;

/**
 * Created by Михаил on 10.07.2014.
 */
public class UserInfo {

    private int id;
    private String name;
    private int group;
    private int role;
    private String university;
    private String group_name;
    //private Shedule shedule;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getGroup() {
        return group;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

//    public Shedule getShedule() {
//        return shedule;
//    }
//
//    public void setShedule(Shedule shedule) {
//        this.shedule = shedule;
//    }
}
