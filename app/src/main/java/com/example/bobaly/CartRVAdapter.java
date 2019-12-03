//adapter for cart recycler view
package com.example.bobaly;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartRVAdapter extends RecyclerView.Adapter<CartRVAdapter.ViewHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Order> orderList = new ArrayList<Order>();
    Context context;

    public CartRVAdapter(Context context,
                         ArrayList<Order> orderList) {
        this.orderList = orderList;
        this.context = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,sweet,price,addon,qty;
        ImageButton delete;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cart_title);
            sweet = itemView.findViewById(R.id.cart_sweet);
            price = itemView.findViewById(R.id.cart_price);
            addon = itemView.findViewById(R.id.cart_addon);
            qty = itemView.findViewById(R.id.cart_quantity);
            delete=itemView.findViewById(R.id.cart_delete);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list, parent, false);

        CartRVAdapter.ViewHolder vh = new CartRVAdapter.ViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String str1="",str2="",str3="",str4="";
        final Order order = orderList.get(position);

        holder.title.setText(order.getTitle());
        holder.sweet.setText("Sweet level: "+order.getSweetLvl()+"");
        holder.price.setText("RM "+order.getPrice());
        holder.qty.setText("x "+order.getQty());

        //for add on textview
        if(order.isBoba()){
            str1="-Boba\n";
        }
        if(order.isGrass()){
            str2="-Grass Jelly\n";
        }
        if(order.isRedbean()){
            str3="-Red Bean\n";
        }
        if(order.isPudding()){
            str4= "-Pudding\n";
        }
        holder.addon.setText("Add On:\n"+str1+str2+str3+str4);

        if(order.isBoba()==false&&order.isGrass()==false&&order.isRedbean()==false&&order.isPudding()==false){
            holder.addon.setText("Add On:-");
        }

        //to delete order
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context, R.style.AlertDialogStyle).setTitle("Delete Order")
                        .setMessage("Confirm remove this order?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("orders").document(order.getId()+"").delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Order deleted!\nPull down screen to refresh cart!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .setIcon(R.drawable.deletepopout)
                        .show();
            }
        });
        //end of to delete order
    }

    @Override
    public int getItemCount() {

        return orderList.size();
    }
}
