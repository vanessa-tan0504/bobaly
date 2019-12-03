//Fragment code for cart (bottom navigation)
package com.example.bobaly;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView rv_cart;
    private LinearLayoutManager linearLayoutManager;
    private CartRVAdapter cartRVAdapter;
    private ArrayList<Order> orderList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private TextView textView,price_total,qty_total;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String userUID;
    private FirebaseUser currentUser;
    private Button btn_sent;
    private DocumentReference documentReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart,container,false);

        swipeRefreshLayout=v.findViewById(R.id.swipeRefresh);
        textView=v.findViewById(R.id.tv_noOrder);
        textView.setVisibility(View.INVISIBLE);
        rv_cart = v.findViewById(R.id.rv_cart);
        progressBar=v.findViewById(R.id.progressBar_order);
        linearLayoutManager= new LinearLayoutManager(getActivity());
        price_total=v.findViewById(R.id.tv_price);
        qty_total=v.findViewById(R.id.tv_item);
        btn_sent=v.findViewById(R.id.btn_sent);

        //content in card view
        orderList = new ArrayList<>();
        cartRVAdapter = new CartRVAdapter(getActivity(),orderList);

        //set layout manager and adapter
        rv_cart.setLayoutManager(linearLayoutManager);
        rv_cart.setAdapter(cartRVAdapter);

        //set firebase instance
        db=FirebaseFirestore.getInstance();

        //get current user id
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        userUID=currentUser.getUid();

        //retrieve data from firebase
        db.collection("orders").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        textView.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        int qty=0;
                        double price=0;

                        if(!queryDocumentSnapshots.isEmpty()){ //if firebase db got data
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list){
                                Order o =d.toObject(Order.class);

                                //get specific each account's order and order status is false,
                                // if true, means already sent order so no need show
                                if(o.getUser().equals(userUID)&&(!o.getOrder_status())){
                                    orderList.add(o);
                                    qty+=o.getQty();
                                    price+=o.getPrice();
                                }
                            }
                            qty_total.setText(qty+"");
                            price_total.setText(String.format("RM %.2f",price));
                            btn_sent.setEnabled(true);//enable sent order button if any data shown
                            cartRVAdapter.notifyDataSetChanged();

                            if(orderList.isEmpty()){ //if specific user no order anything
                                qty_total.setText("None");
                                price_total.setText("RM 0.00");
                                textView.setVisibility(View.VISIBLE);
                                btn_sent.setEnabled(false); //disable sent order button if no data shown
                            }
                        }
                        else{ //if firebase db no data
                            qty_total.setText("None");
                            price_total.setText("RM 0.00");
                            textView.setVisibility(View.VISIBLE);
                            btn_sent.setEnabled(false);//disable sent order button if no data shown
                        }
                    }
                });
        //end of retrieve data from firebase

        //pull down to refresh cart list
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(CartFragment.this).attach(CartFragment.this).commit();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        //end of pull down to refresh cart list

        //sent order and change order_status to true (update document)
        btn_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity(),R.style.AlertDialogStyle).setTitle("Sent Order")
                        .setMessage("Confirm sent all order in the cart?")
                        .setPositiveButton("Sent", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(int i=0;i<orderList.size();i++) {
                                    documentReference=db.collection("orders").document(orderList.get(i).getId()+"");
                                    documentReference.update("order_status",true) //update here
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getContext(), "Your order has been sent.\nPlease pay at the counter", Toast.LENGTH_SHORT).show();
                                                    FragmentTransaction ft = getFragmentManager().beginTransaction(); //refresh fragment after sent order
                                                    ft.detach(CartFragment.this).attach(CartFragment.this).commit();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Your order cannot been sent.\nPlease try again", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .setIcon(R.drawable.checkout)
                        .show();
            }
        });
        //end of sent order and change order_status to true (update document)

        return v;
    }

}
