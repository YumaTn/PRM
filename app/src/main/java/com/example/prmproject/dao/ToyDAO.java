package com.example.prmproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prmproject.database.DBHelper;
import com.example.prmproject.model.Toy;
import com.example.prmproject.model.User;
import com.example.prmproject.model.Toy;

import java.util.ArrayList;
import java.util.List;

public class ToyDAO {
    private DBHelper dbHelper;

    public ToyDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public List<Toy> getByName(String nameSearch) {
        List<Toy> toys = new ArrayList<>();

        String queryString = "SELECT * FROM toy" + " WHERE name" + " LIKE '%" + nameSearch + "%'";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                Toy toy = new Toy(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4), cursor.getInt(5) );
                toys.add(toy);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return toys;
    }

    public List<Toy> getAll() {
        List<Toy> toys = new ArrayList<>();

        String queryString = "SELECT * FROM toy";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                Toy toy = new Toy(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4), cursor.getInt(5) );
                toys.add(toy);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return toys;
    }

    public boolean addOne(Toy toy) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", toy.getName());
        cv.put("price", toy.getPrice());
        cv.put("amount", toy.getAmount());
        cv.put("thumbnail", toy.getThumbnail());
        cv.put("categoryId", toy.getCategoryId());


        long insert = db.insert("toy", null, cv);

        return insert != -1;
    }

    public boolean deleteOne(Toy toy) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowDeleted = db.delete("toy", "id = ?", new String[]{String.valueOf(toy.getId())});
        db.close();

        return rowDeleted > 0;
    }

    public boolean update(Toy toy) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", toy.getName());
        cv.put("price", toy.getPrice());
        cv.put("amount", toy.getAmount());
        cv.put("thumbnail", toy.getThumbnail());
        cv.put("categoryId", toy.getCategoryId());

        long updated = db.update("toy", cv, "id = ?", new String[]{String.valueOf(toy.getId())});

        return updated > 0;
    }

    public Toy getById(int id) {
        String queryString = "SELECT * FROM toy WHERE id = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            Toy toy = new Toy(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3),cursor.getString(4), cursor.getInt(5) );

            cursor.close();
            db.close();

            return toy;
        }
        return null;
    }

    public boolean updateQuantity(int quantity, Toy toy) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        int temp = toy.getAmount();
        System.out.println(temp);

        cv.put("name", toy.getName());
        cv.put("price", toy.getPrice());
        cv.put("amount", toy.getAmount() - quantity);
        cv.put("thumbnail", toy.getThumbnail());
        cv.put("categoryId", toy.getCategoryId());

        long updated = db.update("toy", cv, "id = ?", new String[]{String.valueOf(toy.getId())});

        return updated > 0;
    }
}
