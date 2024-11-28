package com.example.utsprep;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.utsprep.models.CartAdapter;
import com.example.utsprep.models.CartItem;
import com.example.utsprep.models.Product;
import com.example.utsprep.models.Rating;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    private Button ok;
    private LinearLayout thanks;
    private Button checkOut;
    private TextView total;
    private LinearLayout pBar;
    private List<CartItem> items;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private String email = "";
    FirebaseFirestore db ;
    FirebaseAuth auth;
    FirebaseUser user;

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
        FirebaseApp.initializeApp(getContext());
        ok = view.findViewById(R.id.ok);
        total = view.findViewById(R.id.total);
        thanks = view.findViewById(R.id.thanks);
        checkOut = view.findViewById(R.id.checkOut);
        pBar = view.findViewById(R.id.pBar);
        items = new ArrayList<>();
        db =  FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new CartAdapter(getContext(), items, this::updateTotal);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(user == null){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        }else{
            email =user.getEmail();
        }

        db.collection("cartCollection")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        // Loop through the documents in the query result
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Access each document's data
                            String docId = document.getId();
                            int quantity = document.getDouble("quantity").intValue();
                            int id = document.getDouble("id").intValue();
                            String title = document.getString("title");
                            double price = document.getDouble("price").doubleValue();
                            String description = document.getString("description");
                            String category = document.getString("category");
                            String image = document.getString("image");
                            double rate = document.getDouble("rate").doubleValue();
                            int count = document.getDouble("count").intValue();

                            Product p = new Product(id, title, price, description, category, image, new Rating(rate, count));
                            CartItem c = new CartItem(docId, p, quantity);
                            items.add(c);

                        }
                        pBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        updateTotal();
                    } else {
                        // Handle any errors
                        Log.e("FirestoreQuery", "Error getting documents: ", task.getException());
                    }
                });
        checkOut.setOnClickListener(e -> {
            db.collection("cartCollection")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                        if (documents.isEmpty()) {
                            // No documents to delete, update immediately
                            return;
                        }

                        // Collect all delete tasks
                        List<Task<Void>> deleteTasks = new ArrayList<>();
                        for (DocumentSnapshot document : documents) {
                            deleteTasks.add(document.getReference().delete());
                        }

                        // Wait for all delete tasks to complete
                        Tasks.whenAll(deleteTasks)
                                .addOnSuccessListener(aVoid -> {
                                    // All deletions are complete
                                    items.clear();
                                    adapter.notifyDataSetChanged();
                                    updateTotal();
                                    thanks.setVisibility(View.VISIBLE);

                                })
                                .addOnFailureListener(error -> {
                                    Log.e("Firestore", "Error during batch delete", error);
                                });
                    })
                    .addOnFailureListener(error -> {
                        Log.e("Firestore", "Error fetching documents", error);
                    });
        });
        ok.setOnClickListener(e->{
            thanks.setVisibility(View.GONE);
        });


    }
    private void updateTotal() {
        double totalPrice = 0;
        for (CartItem item : items) {
            totalPrice += item.getProduct().getPrice() * item.getQuantity();
        }
        total.setText(String.format("Total: $%.2f", totalPrice));
    }
}