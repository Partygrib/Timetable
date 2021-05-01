package Parsing;

import java.util.ArrayList;

public class Lesson {
    private String name;
    private String time;
    private String type;
    private ArrayList<String> groups;
    private String teachers;
    private String place;
    private String day;

    public String getName() {
        return name;
    }
    public void setName(String name) {this.name = name;}
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public ArrayList<String> getGroups() {
        return groups;
    }
    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTeachers() {
        return teachers;
    }
    public void setTeachers(String teachers) {this.teachers = teachers;}
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
}
