package com.example.prmproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prmproject.database.DBHelper;
import com.example.prmproject.model.Category;
import com.example.prmproject.model.User;
import com.example.prmproject.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private DBHelper dbHelper;

    public CategoryDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public List<Category> getByName(String nameSearch) {
        List<Category> categories = new ArrayList<>();

        String queryString = "SELECT * FROM category" + " WHERE name" + " LIKE '%" + nameSearch + "%'";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                categories.add(category);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categories;
    }

    public Category getById(int id) {
        String queryString = "SELECT * FROM category WHERE id = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            Category category = new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2));

            cursor.close();
            db.close();

            return category;
        }
        return null;
    }

    public List<Category> getAll() {
        List<Category> categories = new ArrayList<>();

        String queryString = "SELECT * FROM category";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                categories.add(category);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categories;
    }

    public boolean addOne(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", category.getName());
        cv.put("thumbnail", category.getThumbnail());

        long insert = db.insert("category", null, cv);

        return insert != -1;
    }

    public boolean deleteOne(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowDeleted = db.delete("category", "id = ?", new String[]{String.valueOf(category.getId())});
        db.close();

        return rowDeleted > 0;
    }

    public boolean update(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", category.getName());
        cv.put("thumbnail", category.getThumbnail());

        long updated = db.update("category", cv, "id = ?", new String[]{String.valueOf(category.getId())});

        return updated > 0;
    }
}
