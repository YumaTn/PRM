package com.example.prmproject.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prmproject.R;
import com.example.prmproject.adapter.UserAdapter;
import com.example.prmproject.dao.UserDAO;
import com.example.prmproject.model.User;

import java.util.List;

public class ManageUserActivity extends AppCompatActivity {

    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        Button btn_back = findViewById(R.id.btn_back);
        Button btn_search = findViewById(R.id.btn_search);

        userDAO = new UserDAO(this);

        loadData("");

        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageUserActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSearch();
            }
        });
    }

    public void loadData (String name) {
        RecyclerView recyclerView = findViewById(R.id.list_user);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<User> users;
        if(name.equals("")) {
            users = userDAO.getAll();
        } else {
            users = userDAO.getByName(name);
        }


        UserAdapter userAdapter = new UserAdapter(this, users);
        recyclerView.setAdapter(userAdapter);
    }

    private void showDialogForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.modal_form_toy, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = findViewById(R.id.title);
        EditText edtName = view.findViewById(R.id.edt_name);
        EditText edtPrice = view.findViewById(R.id.edt_price);
        EditText edtAmount = view.findViewById(R.id.edt_amount);

        Button btnSubmit = view.findViewById(R.id.btn_submit);
        Button btnBack = view.findViewById(R.id.btn_back);

        btnSubmit.setText("Thêm đồ chơi");
        title.setText("Thêm đồ chơi");

//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Employee employee = new Employee(edtFullName.getText().toString(), edtDepartment.getText().toString());
//                boolean isSuccess = employeeDAO.addOne(employee);
//                if (isSuccess) {
//                    Toast.makeText(HrActivity3.this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
//                    loadEmployees("");
//                    alertDialog.dismiss();
//                } else {
//                    Toast.makeText(HrActivity3.this, "Thêm nhân viên thất bại", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void showDialogSearch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.modal_search, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edtFullName = view.findViewById(R.id.edt_name);
        Button btnSearch = view.findViewById(R.id.btn_search);
        Button btnBack = view.findViewById(R.id.btn_back);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User> users = userDAO.getByName(edtFullName.getText().toString());
                if (users.size() > 0) {
                    loadData(edtFullName.getText().toString());
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(ManageUserActivity.this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}