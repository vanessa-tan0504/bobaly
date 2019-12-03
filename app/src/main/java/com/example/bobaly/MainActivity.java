//main code
package com.example.bobaly;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    //private TextView mTextMessage;
    private Toolbar toolbar;
    private BottomNavigationView navigation;
    private ViewPager viewPager;
    private MenuFragment fragment1 = new MenuFragment();
    private CartFragment fragment2 = new CartFragment();
    private ProfileFragment fragment3 = new ProfileFragment();
    private HashMap<Integer, String> mFragmentTags = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar); // toolbar on top of the screen
        setSupportActionBar(toolbar);

        //viewpager settings
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(this);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //first fragment item (menu) appear when launch , show the "menu" fragment by default
        toolbar.setTitle("Bobaly | Menu");
        viewPager.setCurrentItem(0);

        //viewpager swiping settings (swiping navigation method)
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return fragment1;
                    case 1:
                        return fragment2;
                    case 2:
                        return fragment3;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }

    //for bottom navigation (pressing navigation method)
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.cart:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.profile:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };
    //end of for bottom navigation (pressing navigation method)


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //highlight bottom nav bar if to indicate which screen on page
        navigation.getMenu().getItem(position).setChecked(true);

        //to set title when navigating
        switch (position) {
            case 0:
                toolbar.setTitle("Bobaly | Menu");
                break;
            case 1:
                toolbar.setTitle("Bobaly | Cart");
                break;
            case 2:
                toolbar.setTitle("Bobaly | Profile");
                break;
        }
        //end of to set title when navigating
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //make sure if user have not logged out, user remain logged in
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null)
            Toast.makeText(this, "user not logged", Toast.LENGTH_SHORT).show();
    }
}


