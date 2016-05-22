package com.ggu.parsedclasses;

/**
 * Created by Михаил on 29.10.2014.
 */
public class AttendanceItem {

    private int id;
    private String name;
    private int loose;
    private int seek;

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

    public int getLoose() {
        return loose;
    }

    public void setLoose(int loose) {
        this.loose = loose;
    }

    public int getSeek() {
        return seek;
    }

    public void setSeek(int seek) {
        this.seek = seek;
    }
}
