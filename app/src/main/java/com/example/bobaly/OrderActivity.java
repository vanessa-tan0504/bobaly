//order activity code
package com.example.bobaly;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class OrderActivity extends AppCompatActivity {
    private String title,drinksId;
    private TextView tv_title,tv_sugar,tv_amt,tv_add,tv_price;
    private ImageView drinks_image;
    private ConstraintLayout expand,collapse,expand2,collapse2;
    private ImageButton btn_expand,btn_expand2;
    private SeekBar seekBar_sweet;
    private int sweetLevel=0;//default sugar level 0
    private CheckBox checkBoba,checkBean,checkGrass,checkPudding;
    private Button btn_minus,btn_add;
    private int amount=1; //default quantity is 1
    private double drink_price,addOn_price,amt_price,total_price,boba_price,red_price,grass_price,pudding_price;
    private boolean boba_status=false,redbean_status=false,grass_status=false,pudding_status=false;
    private FirebaseFirestore db;
    private Date date = new Date();
    private DateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
    private String orderId=sdf.format(date);
    private String userUID;
    private FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //declare vairables
        tv_title = findViewById(R.id.order_title);
        drinks_image=findViewById(R.id.order_image);
        collapse=findViewById(R.id.layout_option1_collapse);
        expand=findViewById(R.id.layout_option1_expand);
        collapse2=findViewById(R.id.layout_option2_collapse);
        expand2=findViewById(R.id.layout_option2_expand);
        btn_expand=findViewById(R.id.btn_expand1);
        btn_expand2=findViewById(R.id.btn_expand2);
        tv_sugar=(TextView)findViewById(R.id.tv_sugarlvl);
        seekBar_sweet=findViewById(R.id.seekbar_sugarlvl);
        checkBoba=findViewById(R.id.checkBox_boba);
        checkBean=findViewById(R.id.checkBox_redbean);
        checkGrass=findViewById(R.id.checkBox_grass);
        checkPudding=findViewById(R.id.checkBox_pudding);
        btn_minus=findViewById(R.id.btn_minus);
        btn_add=findViewById(R.id.btn_add);
        tv_amt=findViewById(R.id.tv_number);
        tv_add=findViewById(R.id.tv_addon);
        tv_price=findViewById(R.id.order_price);

        //set instance for firestore and firebase user
        db=FirebaseFirestore.getInstance();

        //get current user id
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        userUID=currentUser.getUid();


        //get title and price from menu fragment
        Intent getData = getIntent();
        title =getData.getStringExtra("drinksTitle");
        drinksId=getData.getStringExtra("drinksID"); //for image purpose
        drink_price = Double.parseDouble(getData.getStringExtra("drinksPrice"));

        tv_price.setText(String.format("RM %.2f",drink_price)); //show price
        total_price=drink_price; //initialize original drinks price

        //set title and image for each order
        tv_title.setText("\t"+title);
        switch(drinksId){
            case "new1": drinks_image.setImageResource(R.drawable.new1);
                break;
            case "new2": drinks_image.setImageResource(R.drawable.new2);
                break;
            case "new3": drinks_image.setImageResource(R.drawable.new3);
                break;
            case "new4": drinks_image.setImageResource(R.drawable.new4);
                break;
            case "milktea1": drinks_image.setImageResource(R.drawable.boba1);
                break;
            case "milktea2": drinks_image.setImageResource(R.drawable.boba2);
                break;
            case "milktea3": drinks_image.setImageResource(R.drawable.boba3);
                break;
            case "milktea4": drinks_image.setImageResource(R.drawable.boba4);
                break;
            case "fruittea1": drinks_image.setImageResource(R.drawable.tea1);
                break;
            case "fruittea2": drinks_image.setImageResource(R.drawable.tea2);
                break;
            case "fruittea3": drinks_image.setImageResource(R.drawable.tea3);
                break;
            case "fruittea4": drinks_image.setImageResource(R.drawable.tea4);
                break;
        }
        //end of set title and image for each order


        // hide until its title is clicked (make expandable view)
        expand.setVisibility(View.GONE);
        btn_expand.setImageResource(R.drawable.down);
        tv_sugar.setText(R.string.sweet0);
        expand2.setVisibility(View.GONE);
        btn_expand2.setImageResource(R.drawable.down);

        //seekbar for drinks sweetness level
        seekBar_sweet.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress=0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress=progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                showSweetLevel(progress);
            }
        });
        //end of seekbar for drinks sweetness level
    }

    //update sweetness level
    public void showSweetLevel(int progress){
        switch(progress){
            case 0: sweetLevel=0;
                    tv_sugar.setText(R.string.sweet0);
                    break;
            case 1: sweetLevel=25;
                tv_sugar.setText(R.string.sweet1);
                break;
            case 2: sweetLevel=50;
                tv_sugar.setText(R.string.sweet2);
                break;
            case 3: sweetLevel=75;
                tv_sugar.setText(R.string.sweet3);
                break;
            case 4: sweetLevel=100;
                tv_sugar.setText(R.string.sweet4);
                break;
        }
    }
    //end of update sweetness level

    //control expandable view 1
    public void expand_content(View view){
        expand.setVisibility( expand.isShown()
                ? View.GONE
                : View.VISIBLE );
        if(expand.isShown()){//if view got expand
            btn_expand.setImageResource(R.drawable.up);
        }
        else{
            btn_expand.setImageResource(R.drawable.down);
        }

    }
    //end of control expandable view 1

    //control expandable view 2
    public void expand_content2(View view){
        expand2.setVisibility( expand2.isShown()
                ? View.GONE
                : View.VISIBLE );
        if(expand2.isShown()){//if view got expand
            btn_expand2.setImageResource(R.drawable.up);
        }
        else{
            btn_expand2.setImageResource(R.drawable.down);
        }

    }
    //end of control expandable view 2

    //check box for add on
    public void onCheckboxClicked(View view) {
        boolean topping_check=((CheckBox)view).isChecked();//checkbox checked status
        switch (view.getId()){
            case R.id.checkBox_boba: //rm3.00
                if(topping_check){
                    boba_status=true;
                    boba_price=3.00;
                }
                else{
                    boba_status=false;
                    boba_price=0;
                }
                break;
            case R.id.checkBox_redbean://rm2.00
                if(topping_check){
                    redbean_status=true;
                    red_price=2.00;
                }
                else{
                    redbean_status=false;
                    red_price=0;
                }
                break;
            case R.id.checkBox_grass://rm1.50
                if(topping_check){
                    grass_status=true;
                    grass_price=1.50;
                }
                else{
                    grass_status=false;
                    grass_price=0;
                }
                break;
            case R.id.checkBox_pudding://rm2.50
                if(topping_check){
                    pudding_status=true;
                    pudding_price=2.50;
                }
                else{
                    pudding_status=false;
                    pudding_price=0;
                }
                break;
        }
        addOn_price=boba_price+red_price+grass_price+pudding_price; //add all add on price
        amt_price=drink_price+addOn_price; //cost per drink
        total_price=amt_price*amount; //calculate total price
        tv_price.setText(String.format("RM %.2f",total_price));
    }
    //end of check box for add on

    //minus button
    public void btnMinus_onclick(View view) {
        if(amount==1){ //if is 1 cannot minus
            btn_minus.setEnabled(false);
            btn_add.setEnabled(true);
        }
        else if(amount>1){
            btn_add.setEnabled(true);
            btn_minus.setEnabled(true);
            amount--;
            tv_amt.setText(String.valueOf(amount));
        }
        addOn_price=boba_price+red_price+grass_price+pudding_price; //add all add on price
        amt_price=drink_price+addOn_price; //cost per drink
        total_price=amt_price*amount; //calculate total price
        tv_price.setText(String.format("RM %.2f",total_price));
    }
    //end of minus button

    //add button
    public void btnAdd_onclick(View view) {
        if(amount==20){
            btn_add.setEnabled(false);
        }
        else {
            amount++;
            tv_amt.setText(String.valueOf(amount));
            btn_minus.setEnabled(true);//if more than 1 can minus
        }
        addOn_price=boba_price+red_price+grass_price+pudding_price; //add all add on price
        amt_price=drink_price+addOn_price; //cost per drink
        total_price=amt_price*amount; //calculate total price
        tv_price.setText(String.format("RM %.2f",total_price));
    }
    //end of add button

    //back button to main menu
    public void btnBack_onclick(View view) {
        finish();
    }

    //add to order database
    public void btnCart_onclick(View view) {
        Toast.makeText(this,"Order placed to cart", Toast.LENGTH_SHORT).show();

        final Order order = new Order( //create new order class to store order info to firebase
             title,
             Math.round(total_price*100.0)/100.0d, //convert to 2 decimal place
             sweetLevel,
             Integer.parseInt(tv_amt.getText().toString()), //chg to amount , check for amount data in -+button
             boba_status,
             redbean_status,
             grass_status,
             pudding_status,
             orderId,
             userUID,
             false //initially all order not sent yet . so order status false
        );
        db.collection("orders").document(order.getId()+"").set(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                      Toast.makeText(OrderActivity.this, "Cart updated!", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //end of add to order database
}
