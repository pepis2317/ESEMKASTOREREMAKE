package com.example.utsprep;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.utsprep.models.CartAdapter;
import com.example.utsprep.models.CartItem;
import com.example.utsprep.models.Product;
import com.example.utsprep.models.Rating;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    private List<CartItem> items;
    private RecyclerView recyclerView;
    private CartAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        items = new ArrayList<>();
        Product p1 = new Product(1, "Product 1", 10.0, "Description 1", "Category 1", "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg", new Rating(1,1));
        Product p2 = new Product(2, "Product 2", 15.0, "Description 2", "Category 2", "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg", new Rating(1,1));
        CartItem c1 = new CartItem(1, p1, 2);
        CartItem c2 = new CartItem(2, p2, 1);
        CartItem c3 = new CartItem(3, p2, 1);
        CartItem c4 = new CartItem(4, p2, 1);
        CartItem c5 = new CartItem(5, p2, 1);
        CartItem c6 = new CartItem(6, p2, 1);
        items.add(c1);
        items.add(c2);
        items.add(c3);
        items.add(c4);
        items.add(c5);
        items.add(c6);
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new CartAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}