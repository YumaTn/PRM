package com.example.prmproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prmproject.admin.DashboardActivity;
import com.example.prmproject.dao.UserDAO;
import com.example.prmproject.model.User;

public class LoginActivity extends AppCompatActivity {

    String usernameResponse = null;
    String passwordResponse = null;

    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDAO = new UserDAO(this);

        Button btn_login = findViewById(R.id.btn_login);
        Button btn_register = findViewById(R.id.btn_register);
        EditText edt_username = findViewById(R.id.edt_username);
        EditText edt_password = findViewById(R.id.edt_password);
        CheckBox checkbox_remember = findViewById(R.id.checkbox_remember);

        // Nếu đã tích remember trc đó rồi thì khi vào login sẽ có sẵn username và password
        SharedPreferences preferences = getSharedPreferences("INFO", MODE_PRIVATE);
        boolean isRemember = preferences.getBoolean("isRemember", false);
        if (isRemember) {
            String username = preferences.getString("username", "");
            String password = preferences.getString("password", "");

            edt_username.setText(username);
            edt_password.setText(password);
            checkbox_remember.setChecked(isRemember);

            usernameResponse = username;
            passwordResponse = password;
        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = edt_username.getText().toString();
                String password = edt_password.getText().toString();
                if (username.equals("") && password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isSuccess = userDAO.login(username, password);
                    if (isSuccess) {
                        SharedPreferences preferences = getSharedPreferences("INFO", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        if (checkbox_remember.isChecked()) {
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.putBoolean("isRemember", checkbox_remember.isChecked());

                        } else {
                            editor.clear();

                        }
                        User user = userDAO.getByUsername(username);

                        editor.putString("username_logged", username);
                        editor.putString("fullName", user.getFullName());

                        editor.apply();

                        if (!user.getRole().equals("admin")) {
                            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                        }
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}