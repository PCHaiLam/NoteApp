package com.pchailam.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity   {
    BottomNavigationView bottomNavigationView;
    NotesFragment notesFragment;
    TypeFragment typeFragment;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesFragment = new NotesFragment();
        typeFragment = new TypeFragment();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,notesFragment).commit();
        bottomNavigationView.setSelectedItemId(R.id.notes);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.notes) {
                    switchFragment(notesFragment,false);
                } else if(id == R.id.types) {
                    switchFragment(typeFragment,true);
                } else
                    return false;
                return true;
            }
        });
    }
    private void switchFragment(Fragment fragment, boolean isLeftToRight) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isLeftToRight) {
            transaction.setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_right_to_left,
                    R.anim.enter_right_to_left, R.anim.exit_right_to_left);
        } else {
            transaction.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right,
                    R.anim.enter_left_to_right, R.anim.exit_left_to_right);
        }
        transaction.replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit();
    }

}