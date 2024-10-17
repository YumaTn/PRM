package com.example.prmproject.adapter;

import static com.example.prmproject.util.format.formatPrice;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prmproject.R;
import com.example.prmproject.dao.CartDAO;
import com.example.prmproject.dao.CategoryDAO;
import com.example.prmproject.dao.ToyDAO;
import com.example.prmproject.model.Cart;
import com.example.prmproject.model.Category;
import com.example.prmproject.model.Toy;

import java.util.List;

public class ToyHomeAdapter extends RecyclerView.Adapter<ToyHomeAdapter.ViewHolder> {
    private Context context;
    private List<Toy> toys;

    ToyDAO toyDAO;
    CartDAO cartDAO;
    private SharedPreferences preferences;

    public ToyHomeAdapter(Context context, List<Toy> toys, SharedPreferences preferences) {
        this.context = context;
        this.toys = toys;
        toyDAO = new ToyDAO(context);
        cartDAO = new CartDAO(context);
        this.preferences = preferences;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_toy_home, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Toy toy = toys.get(position);


        CategoryDAO categoryDAO = new CategoryDAO(context);
        Category category = categoryDAO.getById(toy.getCategoryId());

        holder.toyName.setText(toy.getName());
        holder.categoryName.setText(category.getName());
        holder.price.setText(formatPrice(toy.getPrice()));
        // Load ảnh
        Glide.with(context).load(toy.getThumbnail()).into(holder.thumbnail);

        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cart = new Cart(preferences.getString("username_logged", ""), toy.getId(), 1);
                boolean isSuccess = cartDAO.add(cart);
                if (isSuccess) {
                    Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return toys.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName, toyName, price;
        ImageView thumbnail;
        Button btn_add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            toyName = itemView.findViewById(R.id.toy_name);
            categoryName = itemView.findViewById(R.id.category_name);
            price = itemView.findViewById(R.id.price);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            btn_add = itemView.findViewById(R.id.btn_add);
        }
    }
}
