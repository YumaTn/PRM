package com.example.prmproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prmproject.R;
import com.example.prmproject.dao.CategoryDAO;
import com.example.prmproject.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);

        holder.txtId.setText(String.valueOf(category.getId()));
        holder.txtName.setText(category.getName());

        // Load ảnh
        Glide.with(context).load(category.getThumbnail()).into(holder.thumbnail);

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpdate(category);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDelete(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtName;
        Button btnUpdate, btnDelete;
        ImageView thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txt_id);
            txtName = itemView.findViewById(R.id.txt_name);
            thumbnail = itemView.findViewById(R.id.thumbnail);

            btnUpdate = itemView.findViewById(R.id.btn_update);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    private void showDialogUpdate(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
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

        title.setText("Cập nhật danh mục");
        btnSubmit.setText("Cập nhật danh mục");
        edtName.setText(category.getName());
        edtThumbnail.setText(category.getThumbnail());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category categoryData = new Category(
                        category.getId(),
                        edtName.getText().toString(),
                        edtThumbnail.getText().toString()
                );
                CategoryDAO categoryDAO = new CategoryDAO(context);
                boolean isSuccess = categoryDAO.update(categoryData);
                if (isSuccess) {
                    Toast.makeText(context, "Cập nhật danh mục thành công", Toast.LENGTH_SHORT).show();

                    categories.clear();
                    categories.addAll(categoryDAO.getAll());
                    notifyDataSetChanged();

                    alertDialog.dismiss();
                } else {
                    Toast.makeText(context, "Cập nhật danh mục thất bại", Toast.LENGTH_SHORT).show();
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

    private void showDialogDelete(Category category) {
        CategoryDAO categoryDAO = new CategoryDAO(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn chắc chắn xoá có id là: " + category.getId());

        builder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isSuccess = categoryDAO.deleteOne(category);
                if (isSuccess) {
                    Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show();

                    categories.clear();
                    categories.addAll(categoryDAO.getAll());
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
