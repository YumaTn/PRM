package com.example.prmproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prmproject.database.DBHelper;
import com.example.prmproject.model.Cart;
import com.example.prmproject.model.Cart;
import com.example.prmproject.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    private DBHelper dbHelper;

    public CartDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public List<Cart> getAll() {
        List<Cart> carts = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM cart", null);

            if (cursor.moveToFirst()) {
                do {
                    Cart cart = new Cart(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
                    carts.add(cart);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return carts;
    }

    public List<Cart> getByUsername(String username) {
        List<Cart> carts = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM cart WHERE username = ?", new String[]{username});

            if (cursor.moveToFirst()) {
                do {
                    Cart cart = new Cart(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
                    carts.add(cart);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return carts;
    }

    public boolean deleteOne(Cart cart) {
        SQLiteDatabase db = null;
        boolean success = false;

        try {
            db = dbHelper.getWritableDatabase();
            int rowDeleted = db.delete("cart", "id = ?", new String[]{String.valueOf(cart.getId())});
            success = rowDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return success;
    }

    public boolean deleteByUsername(String username) {
        SQLiteDatabase db = null;
        boolean success = false;

        try {
            db = dbHelper.getWritableDatabase();
            int rowDeleted = db.delete("cart", "username = ?", new String[]{username});
            success = rowDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return success;
    }

    public boolean update(Cart cart) {
        SQLiteDatabase db = null;
        boolean success = false;

        try {
            db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("quantity", cart.getQuantity());
            cv.put("username", cart.getUsername());
            cv.put("toyId", cart.getToyId());

            int updated = db.update("cart", cv, "id = ?", new String[]{String.valueOf(cart.getId())});
            success = updated > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return success;
    }

    public boolean add(Cart cart) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean success = false;

        try {
            db.beginTransaction();

            // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
            Cursor cursor = db.rawQuery("SELECT * FROM cart WHERE username = ? AND toyId = ?",
                    new String[]{cart.getUsername(), String.valueOf(cart.getToyId())});

            if (cursor.moveToFirst()) {
                // Sản phẩm đã tồn tại, tăng số lượng
                int newQuantity = cursor.getInt(3) + cart.getQuantity();
                ContentValues updateValues = new ContentValues();
                updateValues.put("quantity", newQuantity);

                int rowsUpdated = db.update("cart", updateValues,
                        "username = ? AND toyId = ?", new String[]{cart.getUsername(), String.valueOf(cart.getToyId())});

                if (rowsUpdated > 0) {
                    success = true;
                }
            } else {
                // Sản phẩm chưa tồn tại, thêm mới vào giỏ hàng
                ContentValues insertValues = new ContentValues();
                insertValues.put("quantity", cart.getQuantity());
                insertValues.put("username", cart.getUsername());
                insertValues.put("toyId", cart.getToyId());

                long inserted = db.insert("cart", null, insertValues);

                if (inserted != -1) {
                    success = true;
                }
            }

            cursor.close();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        return success;
    }

}

