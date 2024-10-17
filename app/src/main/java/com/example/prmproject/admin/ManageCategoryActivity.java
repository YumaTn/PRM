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
import com.example.prmproject.adapter.CategoryAdapter;
import com.example.prmproject.dao.CategoryDAO;
import com.example.prmproject.model.Category;

import java.util.List;

public class ManageCategoryActivity extends AppCompatActivity {

    CategoryDAO categoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        categoryDAO = new CategoryDAO(this);

        Button btn_back = findViewById(R.id.btn_back);
        Button btn_add = findViewById(R.id.btn_add);
        Button btn_search = findViewById(R.id.btn_search);

        loadData("");

        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageCategoryActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogForm();
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
        RecyclerView recyclerView = findViewById(R.id.list_category);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Category> categories;
        if(name.equals("")) {
            categories = categoryDAO.getAll();
        } else {
            categories = categoryDAO.getByName(name);
        }


        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories);
        recyclerView.setAdapter(categoryAdapter);
    }

    private void showDialogForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.modal_form_category, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = view.findViewById(R.id.title);
        EditText edtName = view.findViewById(R.id.edt_name);
        EditText edtThumbnail = view.findViewById(R.id.edt_thumbnail);
        Button btnSubmit = view.findViewById(R.id.btn_submit);
        Button btnBack = view.findViewById(R.id.btn_back);

        btnSubmit.setText("Thêm danh mục");
        title.setText("Thêm danh mục");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category employee = new Category(edtName.getText().toString(), edtThumbnail.getText().toString());
                boolean isSuccess = categoryDAO.addOne(employee);
                if (isSuccess) {
                    Toast.makeText(ManageCategoryActivity.this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
                    loadData("");
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(ManageCategoryActivity.this, "Thêm danh mục thất bại", Toast.LENGTH_SHORT).show();
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
                List<Category> categories = categoryDAO.getByName(edtFullName.getText().toString());
                if (categories.size() > 0) {
                    loadData(edtFullName.getText().toString());
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(ManageCategoryActivity.this, "Không tìm thấy danh mục nào", Toast.LENGTH_SHORT).show();
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