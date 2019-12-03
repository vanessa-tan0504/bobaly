//for menu recycler views
package com.example.bobaly;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class MenuRVAdapter  extends RecyclerView.Adapter<MenuRVAdapter.ViewHolder> {

    ArrayList<String> menuList = new ArrayList<String>();
    ArrayList<Integer> image = new ArrayList<Integer>();
    ArrayList<String> idList = new ArrayList<String>();
    ArrayList<Integer> counter = new ArrayList<Integer>();
    ArrayList<String> priceList = new ArrayList<String>();
    Context context;

    public MenuRVAdapter(Context context,
                         ArrayList<String> menuList,
                         ArrayList<Integer> image,
                         ArrayList<String> idList,
                         ArrayList<String> priceList) {
        this.menuList = menuList;
        this.image = image;
        this.idList=idList;
        this.priceList=priceList;
        this.context = context;


        for (int i = 0; i < menuList.size(); i++) {
            counter.add(0);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        CardView cardView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.menu_title);
            //dropBtn = itemView.findViewById(R.id.goto_arrow);
            cardView = itemView.findViewById(R.id.menu_cardView);
            imageView = itemView.findViewById(R.id.menu_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_list, parent, false);

        MenuRVAdapter.ViewHolder vh = new MenuRVAdapter.ViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.text.setText(menuList.get(position));
        holder.imageView.setBackgroundResource(image.get(position));

        //onclick to another activity for menu
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass text and image to another activity
                Intent intent= new Intent(context,OrderActivity.class);
                intent.putExtra("drinksTitle",holder.text.getText());
                intent.putExtra("drinksID", idList.get(position));
                intent.putExtra("drinksPrice",priceList.get(position));
                context.startActivity(intent);

            }
        });
        //end of onclick to another activity for menu

    }

    @Override
    public int getItemCount() {

        return menuList.size();
    }
}
