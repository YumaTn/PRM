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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prmproject.R;
import com.example.prmproject.adapter.ToyAdapter;
import com.example.prmproject.dao.CategoryDAO;
import com.example.prmproject.dao.ToyDAO;
import com.example.prmproject.model.Category;
import com.example.prmproject.model.Toy;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ManageToyActivity extends AppCompatActivity {

    ToyDAO toyDAO;
    CategoryDAO categoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_toy);

        toyDAO = new ToyDAO(this);
        categoryDAO = new CategoryDAO(this);

        Button btn_back = findViewById(R.id.btn_back);
        Button btn_add = findViewById(R.id.btn_add);
        Button btn_search = findViewById(R.id.btn_search);

        loadData("");

        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageToyActivity.this, DashboardActivity.class);
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

    public void loadData(String name) {
        RecyclerView recyclerView = findViewById(R.id.list_toy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Toy> toys;
        if (name.equals("")) {
            toys = toyDAO.getAll();
        } else {
            toys = toyDAO.getByName(name);
        }


        ToyAdapter toyAdapter = new ToyAdapter(this, toys);
        recyclerView.setAdapter(toyAdapter);
    }

    private void showDialogForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.modal_form_toy, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView title = view.findViewById(R.id.title);
        EditText edtName = view.findViewById(R.id.edt_name);
        EditText edtPrice = view.findViewById(R.id.edt_price);
        EditText edtAmount = view.findViewById(R.id.edt_amount);
        EditText edtThumbnail = view.findViewById(R.id.edt_thumbnail);
        RadioGroup categoryId = view.findViewById(R.id.categoryId);

        Button btnSubmit = view.findViewById(R.id.btn_submit);
        Button btnBack = view.findViewById(R.id.btn_back);

        // load dữ liệu categories lên
        List<Category> categories = categoryDAO.getAll();

        for (int i = 0; i < categories.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(categories.get(i).getName());
            radioButton.setId(categories.get(i).getId()); // Đặt ID cho RadioButton, có thể là một ID duy nhất từ dữ liệu hoặc sử dụng i như ở đây
            categoryId.addView(radioButton);

            // Nếu muốn set RadioButton đầu tiên là checked
            if (i == 0) {
                radioButton.setChecked(true);
            }
        }

        AtomicReference<String> selectedCategoryId = new AtomicReference<>("");

        // Lắng nghe sự kiện khi RadioButton thay đổi
        categoryId.setOnCheckedChangeListener((group, checkedId) -> {
            // Lấy RadioButton từ RadioGroup
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            if (checkedRadioButton != null) {
                // Lấy ID của RadioButton đã chọn
                selectedCategoryId.set(String.valueOf(checkedRadioButton.getId()));
            }
        });

        btnSubmit.setText("Thêm đồ chơi");
        title.setText("Thêm đồ chơi");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toy employee = new Toy(edtName.getText().toString(), Integer.parseInt(edtPrice.getText().toString()), Integer.parseInt(edtAmount.getText().toString()), edtThumbnail.getText().toString(), Integer.parseInt(selectedCategoryId.get()));
                boolean isSuccess = toyDAO.addOne(employee);
                if (isSuccess) {
                    Toast.makeText(ManageToyActivity.this, "Thêm đồ chơi thành công", Toast.LENGTH_SHORT).show();
                    loadData("");
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(ManageToyActivity.this, "Thêm đồ chơi thất bại", Toast.LENGTH_SHORT).show();
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

        EditText edtName = view.findViewById(R.id.edt_name);
        Button btnSearch = view.findViewById(R.id.btn_search);
        Button btnBack = view.findViewById(R.id.btn_back);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Toy> toys = toyDAO.getByName(edtName.getText().toString());
                if (toys.size() > 0) {
                    loadData(edtName.getText().toString());
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(ManageToyActivity.this, "Không tìm thấy đồ chơi nào", Toast.LENGTH_SHORT).show();
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