package com.pchailam.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity   {
    ViewPager2 viewPager2;
    ViewPager2Adapter viewPager2Adapter;
    BottomNavigationView bottomNavigationView;
//    NotesFragment notesFragment;
//    TypeFragment typeFragment;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        viewPager2 = findViewById(R.id.viewPager2);
        viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager2.setAdapter(viewPager2Adapter);

//        notesFragment = new NotesFragment();
//        typeFragment = new TypeFragment();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.notes);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.types);
                        break;
                }
            }
        });

//        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,notesFragment).commit();
//        bottomNavigationView.setSelectedItemId(R.id.notes);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.notes) {
//                    switchFragment(notesFragment,false);
                    viewPager2.setCurrentItem(0);
                    return true;
                } else if(id == R.id.types) {
//                    switchFragment(typeFragment,true);
                    viewPager2.setCurrentItem(1);
                    return true;
                } else
                    return false;
            }
        });
    }
//    private void switchFragment(Fragment fragment, boolean isLeftToRight) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        if (isLeftToRight) {
//            transaction.setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_right_to_left,
//                    R.anim.enter_right_to_left, R.anim.exit_right_to_left);
//        } else {
//            transaction.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right,
//                    R.anim.enter_left_to_right, R.anim.exit_left_to_right);
//        }
//        transaction.replace(R.id.flFragment, fragment)
//                .addToBackStack(null)
//                .commit();
//    }

}