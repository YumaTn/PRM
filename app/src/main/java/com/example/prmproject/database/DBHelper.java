package com.example.prmproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "LABMAU", null, 3);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String userTable = "CREATE TABLE user (username TEXT PRIMARY KEY, password TEXT, fullName TEXT, role TEXT)";
        db.execSQL(userTable);

        String categoryTable = "CREATE TABLE category (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, thumbnail TEXT)";
        db.execSQL(categoryTable);

        String toyTable = "CREATE TABLE toy (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INT, amount INT, thumbnail TEXT, categoryId INT)";
        db.execSQL(toyTable);

        String cartTable = "CREATE TABLE cart (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, toyId INT, quantity INT)";
        db.execSQL(cartTable);

        String orderTable = "CREATE TABLE orders (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, totalPrice INT, createdAt TIMESTAMP)";
        db.execSQL(orderTable);

        String insertUser = "INSERT INTO user (username, password, fullName, role) VALUES ('pezoiks1', '123456aA', 'Phạm Ngọc Viễn Đông', 'admin'), ('pezoiks2', '123456aA', 'Đông Phạm', 'customer')";
        String insertCategory = "INSERT INTO category (name, thumbnail) VALUES ('Búp bê', 'https://cdn1.concung.com/2023/11/64627-106566-large_mobile/bo-bup-be-chuyen-gia-trang-diem-16pcs-yn529808-c308-xam.png')";
        String insertToy = "INSERT INTO toy (name, price, amount, thumbnail, categoryId) VALUES ('Búp bê pin', 45000, 12, 'https://png.pngtree.com/png-clipart/20240218/original/pngtree-3d-cartoon-cute-doll-image-png-image_14353825.png', 1)";

        db.execSQL(insertCategory);
        db.execSQL(insertToy);
        db.execSQL(insertUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS user");
            db.execSQL("DROP TABLE IF EXISTS category");
            db.execSQL("DROP TABLE IF EXISTS toy");
            db.execSQL("DROP TABLE IF EXISTS cart");
            db.execSQL("DROP TABLE IF EXISTS orders");
            onCreate(db);
        }
    }
}
