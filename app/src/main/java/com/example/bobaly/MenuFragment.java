//Fragment code for menu (bottom navigation)
package com.example.bobaly;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import java.util.ArrayList;

public class MenuFragment extends Fragment {
    private RecyclerView rv_newArrival,rv_milktea,rv_fuittea;
    private LinearLayoutManager linearLayoutManager;
    private MenuRVAdapter menuRVAdapter;
    private ViewFlipper simpleViewFlipper;
    int[] images={R.drawable.banner1,R.drawable.banner2,R.drawable.banner3,R.drawable.banner4};

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_menu,container,false);

        //slideshow viewflipper settings
        simpleViewFlipper=v.findViewById(R.id.viewFlipper);

        for(int i=0;i<images.length;i++){
            //create object of image view
            ImageView imageView= new ImageView(getContext());
            imageView.setImageResource(images[i]); //set image in imageview
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            simpleViewFlipper.addView(imageView); //add imageview to viewflipper
        }

        //slideshow animation settings
        Animation in = AnimationUtils.loadAnimation(getContext(),android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);
        //set animation type's to viewflipper
        simpleViewFlipper.setInAnimation(in);
        simpleViewFlipper.setOutAnimation(out);
        //set interval time for flipping slideshow
        simpleViewFlipper.setFlipInterval(3000);//3 second per image
        simpleViewFlipper.setAutoStart(true); //set suto flipping for slideshow
        //end of slideshow view flipper settings


        //new arrival recycler view
        rv_newArrival = v.findViewById(R.id.rv_new);
        linearLayoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        //content in card view
        //title arraylist
        ArrayList<String>arrivalList = new ArrayList<>();
        arrivalList.add("Mango Smoothie");
        arrivalList.add("Matcha Yogurt Smoothie");
        arrivalList.add("Oolong Iced Tea");
        arrivalList.add("Honeydew Smoothie");

        //image arraylist
        ArrayList<Integer>imageList = new ArrayList<>();
        imageList.add(R.drawable.new1);
        imageList.add(R.drawable.new2);
        imageList.add(R.drawable.new3);
        imageList.add(R.drawable.new4);

        //id arraylist
        ArrayList<String>idList = new ArrayList<>();
        idList.add("new1");
        idList.add("new2");
        idList.add("new3");
        idList.add("new4");

        //price arraylist
        ArrayList<String>priceList=new ArrayList<>();
        priceList.add("9.90");
        priceList.add("12.90");
        priceList.add("6.90");
        priceList.add("9.90");

        menuRVAdapter = new MenuRVAdapter(getActivity(),arrivalList,imageList,idList,priceList);

        rv_newArrival.setLayoutManager(linearLayoutManager);
        rv_newArrival.setAdapter(menuRVAdapter);
        // end of new arrival recycler view


       // milktea recycler view
        rv_milktea = v.findViewById(R.id.rv_milktea);
        linearLayoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        //content in card view
        //title arraylist
        ArrayList<String>milkteaList = new ArrayList<>();
        milkteaList.add("Matcha Bubble Tea");
        milkteaList.add("Signature Bubble Tea");
        milkteaList.add("Cheese Hojicha Tea");
        milkteaList.add("Oolong Milk Bubble Tea");

        //image arraylist
        ArrayList<Integer>imageList2 = new ArrayList<>();
        imageList2.add(R.drawable.boba1);
        imageList2.add(R.drawable.boba2);
        imageList2.add(R.drawable.boba3);
        imageList2.add(R.drawable.boba4);

        //id arraylist
        ArrayList<String>idList2 = new ArrayList<>();
        idList2.add("milktea1");
        idList2.add("milktea2");
        idList2.add("milktea3");
        idList2.add("milktea4");

        //price arraylist
        ArrayList<String>priceList2=new ArrayList<>();
        priceList2.add("10.90");
        priceList2.add("12.90");
        priceList2.add("12.90");
        priceList2.add("8.90");

        menuRVAdapter = new MenuRVAdapter(getActivity(),milkteaList,imageList2,idList2,priceList2);
        rv_milktea.setLayoutManager(linearLayoutManager);
        rv_milktea.setAdapter(menuRVAdapter);
        // end of milktea recycler view



        // fruit tea recycler view
        rv_fuittea = v.findViewById(R.id.rv_fruittea);
        linearLayoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        //content in card view
        //title arraylist
        ArrayList<String> fruitteaList = new ArrayList<>();
        fruitteaList.add("Grape Cheese");
        fruitteaList.add("Peach Cheese");
        fruitteaList.add("Lychee Cheese");
        fruitteaList.add("Mixed Fruit");

        //image arraylist
        ArrayList<Integer>imageList3 = new ArrayList<>();
        imageList3.add(R.drawable.tea1);
        imageList3.add(R.drawable.tea2);
        imageList3.add(R.drawable.tea3);
        imageList3.add(R.drawable.tea4);

        //id arraylist
        ArrayList<String>idList3 = new ArrayList<>();
        idList3.add("fruittea1");
        idList3.add("fruittea2");
        idList3.add("fruittea3");
        idList3.add("fruittea4");

        //price arraylist
        ArrayList<String>priceList3=new ArrayList<>();
        priceList3.add("12.90");
        priceList3.add("12.90");
        priceList3.add("12.90");
        priceList3.add("17.90");

        menuRVAdapter = new MenuRVAdapter(getActivity(),fruitteaList,imageList3,idList3,priceList3);

        rv_fuittea.setLayoutManager(linearLayoutManager);
        rv_fuittea.setAdapter(menuRVAdapter);
        // end of fruit tea recycler view

        return v;
    }
}
