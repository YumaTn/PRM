package com.example.prmproject.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prmproject.R;
import com.example.prmproject.adapter.CategoryAdapter;
import com.example.prmproject.adapter.CategoryHomeAdapter;
import com.example.prmproject.adapter.ToyHomeAdapter;
import com.example.prmproject.dao.CategoryDAO;
import com.example.prmproject.dao.ToyDAO;
import com.example.prmproject.databinding.FragmentHomeBinding;
import com.example.prmproject.model.Category;
import com.example.prmproject.model.Toy;

import java.util.List;

public class HomeFragment extends Fragment {

    CategoryDAO categoryDAO;
    ToyDAO toyDAO;


    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        categoryDAO = new CategoryDAO(getContext());
        toyDAO = new ToyDAO(getContext());

        EditText search = view.findViewById(R.id.search);

        loadDataCategories(view);
        loadDataToys(view, "");

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadDataToys(view, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void loadDataCategories(View view) {
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.list_category);

        List<Category> categories = categoryDAO.getAll();

        CategoryHomeAdapter categoryHomeAdapter = new CategoryHomeAdapter(getContext(), categories);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        recyclerView.setAdapter(categoryHomeAdapter);
    }

    public void loadDataToys(View view, String search) {
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerViewToy = view.findViewById(R.id.list_toy);
        recyclerViewToy.setLayoutManager(verticalLayoutManager);

        List<Toy> toys = toyDAO.getByName(search);

        SharedPreferences preferences = getContext().getSharedPreferences("INFO", Context.MODE_PRIVATE);
        ToyHomeAdapter toyHomeAdapter = new ToyHomeAdapter(getContext(), toys, preferences);
        recyclerViewToy.setAdapter(toyHomeAdapter);
    }
}