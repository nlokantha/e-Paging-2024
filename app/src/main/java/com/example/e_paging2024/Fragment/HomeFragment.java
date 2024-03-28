package com.example.e_paging2024.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e_paging2024.Database.AppDatabase;
import com.example.e_paging2024.Models.User;
import com.example.e_paging2024.R;
import com.example.e_paging2024.databinding.FragmentHomeBinding;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    AppDatabase db;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Setup Bluetooth Connection", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.textInputEditTextName.getText().toString();
                String flightNumber = binding.textInputEditTextFlightNumber.getText().toString();
                String time = binding.textInputEditTextTime.getText().toString();

                if (name.isEmpty()){
                    binding.textInputLayoutName.setError("Please Enter Name");

                } else if (flightNumber.isEmpty()) {
                    binding.textInputLayoutFlightNumber.setError("Please Enter Flight Number");
                } else if (time.isEmpty()) {
                    binding.textInputLayoutTime.setError("Please Set Time");
                }else {
                    binding.textInputLayoutName.setError("");
                    binding.textInputLayoutFlightNumber.setError("");
                    binding.textInputLayoutTime.setError("");

                    db = Room.databaseBuilder(getActivity(), AppDatabase.class,"users-db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();

                    User user = new User(name,flightNumber,time);
                    db.userDao().insert(user);

                    binding.textInputEditTextName.setText("");
                    binding.textInputEditTextFlightNumber.setText("");
                    binding.textInputEditTextTime.setText("");

                }
            }
        });

        binding.buttonViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoUserDetails();
            }
        });

    }
    HomeFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (HomeFragmentListener) context;
    }

    public interface HomeFragmentListener{
        void gotoUserDetails();
    }
}