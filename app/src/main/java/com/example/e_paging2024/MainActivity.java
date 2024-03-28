package com.example.e_paging2024;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.e_paging2024.Fragment.HomeFragment;
import com.example.e_paging2024.Fragment.UpdateUserFragment;
import com.example.e_paging2024.Fragment.UserDetailsFragment;
import com.example.e_paging2024.Models.User;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener, UserDetailsFragment.UserDetailsFragmentListener, UpdateUserFragment.UpdateListener {

    @Override
    public void updateUser(User user) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, UpdateUserFragment.newInstance(user))
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView,new HomeFragment())
                .commit();
    }

    @Override
    public void gotoUserDetails() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView,new UserDetailsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancleUserDetails() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void cancleUpdate() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void doneUpdate() {
        getSupportFragmentManager().popBackStack();
    }
}