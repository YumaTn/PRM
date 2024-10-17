package com.example.prmproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prmproject.dao.UserDAO;
import com.example.prmproject.util.regex;

public class RegisterActivity extends AppCompatActivity {

    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDAO = new UserDAO(this);

        EditText edt_fullNameR = findViewById(R.id.edt_fullNameR);
        EditText edt_usernameR = findViewById(R.id.edt_usernameR);
        EditText edt_passwordR = findViewById(R.id.edt_passwordR);
        EditText edt_passwordRC = findViewById(R.id.edt_passwordRC);
        Button btn_submitR = findViewById(R.id.btn_submitR);
        Button btn_back = findViewById(R.id.btn_back);
        TextView notification = findViewById(R.id.tv_notification);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_submitR.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String fullName = edt_fullNameR.getText().toString();
                String username = edt_usernameR.getText().toString();
                String password = edt_passwordR.getText().toString();
                String passwordConfirm = edt_passwordRC.getText().toString();

                if (username.equals("") || password.equals("") || passwordConfirm.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    notification.setText("Hãy nhập đầy đủ thông tin");
                } else if (!username.matches(regex.regexUsername)) {
                    Toast.makeText(RegisterActivity.this, "Username phải tối thiểu 8 ký tự", Toast.LENGTH_SHORT).show();
                    notification.setText("Username phải tối thiểu 8 ký tự");

                } else if (!password.matches(regex.regexPassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu phải tối thiểu 6 ký tự, và phải có ít nhất 1 chứ thường, chữ hoa và chứ số", Toast.LENGTH_SHORT).show();
                    notification.setText("Mật khẩu phải tối thiểu 6 ký tự, và phải có ít nhất 1 chứ thường, chữ hoa và chứ số");

                } else if (!edt_passwordR.getText().toString().equals(edt_passwordRC.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    notification.setText("Mật khẩu không trùng khớp");

                } else {
                    boolean isUsernameExist = userDAO.isUsernameExist(username);
                    if (!isUsernameExist) {
                        userDAO.register(username, password, fullName);
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Username đã tồn tại", Toast.LENGTH_SHORT).show();
                        notification.setText("Username đã tồn tại");
                    }
                }
            }
        });
    }
}