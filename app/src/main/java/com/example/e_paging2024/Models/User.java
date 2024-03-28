package com.example.e_paging2024.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users")
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    public String name;
    public String filghtNumber;
    public String time;


    public User() {
    }

    public User(String name, String filghtNumber, String time) {
        this.name = name;
        this.filghtNumber = filghtNumber;
        this.time = time;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilghtNumber() {
        return filghtNumber;
    }

    public void setFilghtNumber(String filghtNumber) {
        this.filghtNumber = filghtNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}


