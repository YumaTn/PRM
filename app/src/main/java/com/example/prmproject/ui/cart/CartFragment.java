package com.example.prmproject.ui.cart;

import static com.example.prmproject.util.format.formatPrice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prmproject.R;
import com.example.prmproject.adapter.CartAdapter;
import com.example.prmproject.dao.CartDAO;
import com.example.prmproject.dao.OrderDAO;
import com.example.prmproject.dao.ToyDAO;
import com.example.prmproject.databinding.FragmentHomeBinding;
import com.example.prmproject.model.Cart;
import com.example.prmproject.model.Order;
import com.example.prmproject.model.Toy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CartFragment extends Fragment {

    private FragmentHomeBinding binding;
    CartDAO cartDAO;
    ToyDAO toyDAO;
    OrderDAO orderDAO;
    SharedPreferences preferences;
    List<Cart> carts;
    CartAdapter cartAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartDAO = new CartDAO(getContext());
        toyDAO = new ToyDAO(getContext());
        orderDAO = new OrderDAO(getContext());
        preferences = getContext().getSharedPreferences("INFO", Context.MODE_PRIVATE);
        carts = cartDAO.getByUsername(preferences.getString("username_logged", ""));

        Button btn_submit = view.findViewById(R.id.btn_submit);
        TextView total_price = view.findViewById(R.id.total_price);

        total_price.setText(formatPrice(calculateTotalPrice()));

        loadDataCarts(view);
        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (carts.size() == 0) {
                        Toast.makeText(getContext(), "Bạn không có sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT).show();
                    } else {
                        Order order = new Order(preferences.getString("username_logged", ""), calculateTotalPrice());
                        boolean isSuccess = orderDAO.add(order);
                        if (isSuccess) {
//                            carts.forEach(cart -> {
//                                Toy toy = toyDAO.getById(cart.getToyId());
//                                toyDAO.updateQuantity(cart.getQuantity(), toy);
//                            });

                            total_price.setText("0 VNĐ");

                            carts.clear();
                            cartAdapter.notifyDataSetChanged();

                            Toast.makeText(getContext(), "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }


                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void loadDataCarts(View view) {
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerViewToy = view.findViewById(R.id.list_cart);
        recyclerViewToy.setLayoutManager(verticalLayoutManager);

        cartAdapter = new CartAdapter(getContext(), carts, preferences);
        recyclerViewToy.setAdapter(cartAdapter);
    }

    public int calculateTotalPrice() {
        int sum = 0;

        if (carts.size() > 0) {
            for (Cart c : carts) {
                Toy toy = toyDAO.getById(c.getToyId());
                sum += toy.getPrice() * c.getQuantity();
            }
        }

        return sum;
    }

}