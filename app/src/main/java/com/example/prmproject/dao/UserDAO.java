package com.example.prmproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prmproject.database.DBHelper;
import com.example.prmproject.model.User;
import com.example.prmproject.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private DBHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public boolean login(String username, String password) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from user where username = ? and password = ?", new String[]{username, password});

        return cursor.getCount() > 0;
    }

    public boolean register(String username, String password, String fullName) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("fullName", fullName);
        cv.put("username", username);
        cv.put("password", password);
        cv.put("role", "customer");

        long insert = sqLiteDatabase.insert("user", null, cv);

        return insert != -1;
    }

    public boolean isUsernameExist(String username) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from user where username = ? ", new String[]{username});
        return cursor.getCount() > 0;

    }

    public User getByUsername(String username) {
        String queryString = "SELECT * FROM user WHERE username LIKE ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, new String[]{username});

        if (cursor.moveToFirst()) {
            User user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));

            cursor.close();
            db.close();

            return user;
        }
        return null;
    }

    public List<User> getByName(String nameSearch) {
        List<User> users = new ArrayList<>();

        String queryString = "SELECT * FROM user" + " WHERE fullName" + " LIKE '%" + nameSearch + "%'";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                users.add(user);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();

        String queryString = "SELECT * FROM user";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                users.add(user);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }

    public boolean deleteOne(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowDeleted = db.delete("user", "username = ?", new String[]{user.getUsername()});
        db.close();

        return rowDeleted > 0;
    }

    public boolean update(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("fullName", user.getFullName());
        cv.put("role", user.getRole());

        long updated = db.update("user", cv, "username = ?", new String[]{user.getUsername()});

        return updated > 0;
    }

}
