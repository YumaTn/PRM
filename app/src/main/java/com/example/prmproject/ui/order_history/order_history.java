package com.example.prmproject.ui.order_history;

import static com.example.prmproject.util.format.formatPrice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.prmproject.R;
import com.example.prmproject.dao.OrderDAO;
import com.example.prmproject.databinding.FragmentHomeBinding;
import com.example.prmproject.model.Order;

import java.util.List;

public class order_history extends Fragment {
    private FragmentHomeBinding binding;
    SharedPreferences preferences;

    OrderDAO orderDAO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        preferences = getContext().getSharedPreferences("INFO", Context.MODE_PRIVATE);
        orderDAO = new OrderDAO(getContext());

        TableLayout tableLayout = view.findViewById(R.id.tableLayout);

        // Example data, you can replace with your actual data from a list or database

        List<Order> orders = orderDAO.getByUsername(preferences.getString("username_logged", ""));

        for (int i = 0; i < orders.size(); i++) {
            TableRow tableRow = new TableRow(requireContext());

            TextView textMaDonHang = createTextView(String.valueOf(orders.get(i).getId()));
            TextView textTongTien = createTextView(formatPrice(orders.get(i).getTotalPrice()));
            TextView textNgayMua = createTextView(orders.get(i).getCreatedAt());

            tableRow.addView(textMaDonHang);
            tableRow.addView(textTongTien);
            tableRow.addView(textNgayMua);

            tableLayout.addView(tableRow);
        }

        return view;
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(requireContext());
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1));
        textView.setText(text);
        textView.setPadding(150, 10, 10, 10);
        return textView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}