package com.example.prmproject.adapter;

import static com.example.prmproject.util.format.formatPrice;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.prmproject.dao.CartDAO;
import com.example.prmproject.dao.CategoryDAO;
import com.example.prmproject.dao.ToyDAO;
import com.example.prmproject.dao.UserDAO;
import com.example.prmproject.model.Cart;
import com.example.prmproject.model.Category;
import com.example.prmproject.model.Cart;
import com.example.prmproject.model.Toy;

import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private List<Cart> carts;

    ToyDAO toyDAO;
    CartDAO cartDAO;
    UserDAO userDAO;
    CategoryDAO categoryDAO;
    private SharedPreferences preferences;

    public CartAdapter(Context context, List<Cart> carts, SharedPreferences preferences) {
        this.context = context;
        this.carts = carts;
        toyDAO = new ToyDAO(context);
        cartDAO = new CartDAO(context);
        userDAO = new UserDAO(context);
        categoryDAO = new CategoryDAO(context);
        this.preferences = preferences;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_cart, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = carts.get(position);

        System.out.println(cart);

        Toy toy = toyDAO.getById(cart.getToyId());
        Category category = categoryDAO.getById(toy.getCategoryId());

        holder.toyName.setText(toy.getName());
        holder.categoryName.setText(category.getName());
        holder.price.setText(formatPrice(toy.getPrice()));
        holder.quantity.setText(String.valueOf(cart.getQuantity()));
        // Load ảnh
        Glide.with(context).load(toy.getThumbnail()).into(holder.thumbnail);
    }


    @Override
    public int getItemCount() {
        return carts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName, toyName, price, quantity;
        ImageView thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            toyName = itemView.findViewById(R.id.toy_name);
            categoryName = itemView.findViewById(R.id.category_name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }
}
