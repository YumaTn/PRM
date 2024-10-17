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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prmproject.R;
import com.example.prmproject.dao.UserDAO;
import com.example.prmproject.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> users;
    Button btnUpdate, btnDelete;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_user, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        holder.txtUsername.setText(String.valueOf(users.get(position).getUsername()));
        holder.txtFullName.setText(users.get(position).getFullName());
        holder.txtRole.setText(users.get(position).getRole());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpdate(users.get(holder.getAdapterPosition()));
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogDelete(users.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtFullName, txtRole;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtUsername = itemView.findViewById(R.id.txt_username);
            txtFullName = itemView.findViewById(R.id.txt_fullName);
            txtRole = itemView.findViewById(R.id.txt_role);
            btnUpdate= itemView.findViewById(R.id.btn_update);
            btnDelete = itemView.findViewById(R.id.btn_delete);

        }
    }

    private void showDialogUpdate(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.modal_form_user, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtUsername = view.findViewById(R.id.txt_username);
        EditText edtFullName = view.findViewById(R.id.edt_fullName);
        RadioButton radioButtonCustomer = view.findViewById(R.id.radioButtonCustomer);
        RadioButton radioButtonAdmin = view.findViewById(R.id.radioButtonAdmin);
        Button btnSubmit = view.findViewById(R.id.btn_submit);
        Button btnBack = view.findViewById(R.id.btn_back);

        txtUsername.setText(user.getUsername());
        edtFullName.setText(user.getFullName());

        if (user.getRole().equals("admin")) {
            radioButtonAdmin.setChecked(true);
        } else {
            radioButtonCustomer.setChecked(true);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userData = new User(user.getUsername(), user.getPassword(), edtFullName.getText().toString(), radioButtonAdmin.isChecked() ? "admin" : "customer");
                UserDAO userDAO = new UserDAO(context);
                boolean isSuccess = userDAO.update(userData);
                if (isSuccess) {
                    Toast.makeText(context, "Cập nhật tài khoản thành công", Toast.LENGTH_SHORT).show();

                    users.clear();
                    users = userDAO.getAll();
                    notifyDataSetChanged();

                    alertDialog.dismiss();
                } else {
                    Toast.makeText(context, "Cập nhật nhân viên thất bại", Toast.LENGTH_SHORT).show();
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

    private void showDialogDelete(User user) {
        UserDAO userDAO = new UserDAO(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Thông báo");
        builder.setMessage("Bạn chắc chắn xoá tài khoản có là: " + user.getUsername());

        builder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isSuccess = userDAO.deleteOne(user);
                if (isSuccess) {
                    Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show();

                    users.clear();
                    users = userDAO.getAll();
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Huỷ", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
