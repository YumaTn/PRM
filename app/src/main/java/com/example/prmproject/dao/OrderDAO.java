package com.example.prmproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prmproject.database.DBHelper;
import com.example.prmproject.model.Cart;
import com.example.prmproject.model.Order;
import com.example.prmproject.model.Toy;
import com.example.prmproject.model.Order;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderDAO {
    private DBHelper dbHelper;
    private CartDAO cartDAO;
    private ToyDAO toyDAO;
    SharedPreferences preferences;

    public OrderDAO(Context context) {
        dbHelper = new DBHelper(context);
        cartDAO = new CartDAO(context);
        toyDAO = new ToyDAO(context);
        preferences = context.getSharedPreferences("INFO", Context.MODE_PRIVATE);
    }


    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();

        try (SQLiteDatabase db = dbHelper.getReadableDatabase(); Cursor cursor = db.rawQuery("SELECT * FROM orders", null)) {

            if (cursor.moveToFirst()) {
                do {
                    Order order = new Order(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3));
                    orders.add(order);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public List<Order> getByUsername(String username) {
        List<Order> orders = new ArrayList<>();

        try (SQLiteDatabase db = dbHelper.getReadableDatabase(); Cursor cursor = db.rawQuery("SELECT * FROM orders WHERE username = ?", new String[]{username})) {

            if (cursor.moveToFirst()) {
                do {
                    Order order = new Order(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3));
                    orders.add(order);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public boolean add(Order order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean success = false;

        try {
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp createdAt = new Timestamp(currentTimeMillis);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            String createdAtString = sdf.format(createdAt);

            ContentValues insertValues = new ContentValues();
            insertValues.put("totalPrice", order.getTotalPrice());
            insertValues.put("username", order.getUsername());
            insertValues.put("createdAt", createdAtString);

            long inserted = db.insert("orders", null, insertValues);

            if (inserted != -1) {
                String username = preferences.getString("username_logged", "");
                List<Cart> carts = cartDAO.getByUsername(username);
                carts.forEach(c -> {
                    Toy toy = toyDAO.getById(c.getToyId());
                    toy.setAmount(toy.getAmount() - c.getQuantity());
                    toyDAO.update(toy);
                });
                cartDAO.deleteByUsername(username);
                success = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }
}
