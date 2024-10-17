package com.example.prmproject.adapter;

import static com.example.prmproject.util.format.formatPrice;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prmproject.R;
import com.example.prmproject.dao.CategoryDAO;
import com.example.prmproject.dao.ToyDAO;
import com.example.prmproject.model.Category;
import com.example.prmproject.model.Toy;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ToyAdapter extends RecyclerView.Adapter<ToyAdapter.ViewHolder> {
    private Context context;
    private List<Toy> toys;

    public ToyAdapter(Context context, List<Toy> toys) {
        this.context = context;
        this.toys = toys;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_toy, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Toy toy = toys.get(position);

            CategoryDAO categoryDAO = new CategoryDAO(context);

            holder.txtId.setText(String.valueOf(toy.getId()));
            holder.txtName.setText(toy.getName());
            holder.txtPrice.setText(formatPrice(toy.getPrice()));
            holder.txtAmount.setText(String.valueOf(toy.getAmount()));

            Category category = categoryDAO.getById(toy.getCategoryId());
            holder.txtCategory.setText(category.getName());


            // Load ảnh
            Glide.with(context).load(toy.getThumbnail()).into(holder.thumbnail);

            holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogUpdate(toy);
                }
            });

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogDelete(toy);
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return toys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtName, txtPrice, txtAmount, txtCategory;
        Button btnUpdate, btnDelete;
        ImageView thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txt_id);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtAmount = itemView.findViewById(R.id.txt_amount);
            txtCategory = itemView.findViewById(R.id.txt_category);
            thumbnail = itemView.findViewById(R.id.thumbnail);

            btnUpdate = itemView.findViewById(R.id.btn_update);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    private void showDialogUpdate(Toy toy) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
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
        Button btnSubmit = view.findViewById(R.id.btn_submit);
        Button btnBack = view.findViewById(R.id.btn_back);
        RadioGroup categoryId = view.findViewById(R.id.categoryId);


        title.setText("Cập nhật sản phẩm");
        btnSubmit.setText("Cập nhật sản phẩm");
        edtName.setText(toy.getName());
        edtPrice.setText(String.valueOf(toy.getPrice()));
        edtAmount.setText(String.valueOf(toy.getAmount()));
        edtThumbnail.setText(toy.getThumbnail());

        // load dữ liệu categories lên
        AtomicReference<String> selectedCategoryId = new AtomicReference<>("");
        CategoryDAO categoryDAO = new CategoryDAO(context);
        List<Category> categories = categoryDAO.getAll();

        for (int i = 0; i < categories.size(); i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(categories.get(i).getName());
            radioButton.setId(categories.get(i).getId()); // Đặt ID cho RadioButton, có thể là một ID duy nhất từ dữ liệu hoặc sử dụng i như ở đây
            categoryId.addView(radioButton);

            // Nếu muốn set RadioButton đầu tiên là checked
            if (categories.get(i).getId() == toy.getCategoryId()) {
                radioButton.setChecked(true);
                selectedCategoryId.set(String.valueOf(toy.getCategoryId()));
            }
        }

        // Lắng nghe sự kiện khi RadioButton thay đổi

        categoryId.setOnCheckedChangeListener((group, checkedId) -> {
            // checkedId là ID của RadioButton được chọn
            RadioButton checkedRadioButton = view.findViewById(checkedId);
            selectedCategoryId.set(String.valueOf(checkedRadioButton.getId()));
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toy toyData = new Toy(
                            toy.getId(),
                            edtName.getText().toString(),
                            Integer.parseInt(edtPrice.getText().toString()),
                            Integer.parseInt(edtAmount.getText().toString()),
                            edtThumbnail.getText().toString(),
                            Integer.parseInt(selectedCategoryId.get())
                    );
                    ToyDAO toyDAO = new ToyDAO(context);
                    boolean isSuccess = toyDAO.update(toyData);
                    if (isSuccess) {
                        Toast.makeText(context, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();

                        toys.clear();
                        toys = toyDAO.getAll();
                        notifyDataSetChanged();

                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(context, "Cập nhật sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

                    System.out.println(e.getMessage());
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

    private void showDialogDelete(Toy toy) {
        ToyDAO toyDAO = new ToyDAO(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn chắc chắn xoá có id là: " + toy.getId());

        builder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isSuccess = toyDAO.deleteOne(toy);
                if (isSuccess) {
                    Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show();

                    toys.clear();
                    toys.addAll(toyDAO.getAll());
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
