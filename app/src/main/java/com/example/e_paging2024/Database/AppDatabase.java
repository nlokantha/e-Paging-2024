package com.example.e_paging2024.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.e_paging2024.Models.User;

@Database(entities = {User.class},version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
