package com.example.e_paging2024.Fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

    private BluetoothDevice device;
    BluetoothAdapter bluetoothAdapter;
    public static final int REQUEST_ENABLE_BT = 100;

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null && result.getResultCode() == RESULT_OK){
                mListener.gotoDeviceListFragment();
            }else {
                Toast.makeText(getActivity(), "You Must Turn On IT Broo", Toast.LENGTH_SHORT).show();
            }

        }
    });

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        binding.buttonBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isEnabled()){
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startForResult.launch(enableBtIntent);
                }else {
                    mListener.gotoDeviceListFragment();
                }
            }
        });

        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = binding.textInputEditTextName.getText().toString();
                String flightNumber = binding.textInputEditTextFlightNumber.getText().toString();
                String time = binding.textInputEditTextTime.getText().toString();

                String combine = name+"$"+flightNumber+"$"+time+"$";
                mListener.sendtoEpage(combine);

            }
        });

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.textInputEditTextName.getText().toString();
                String flightNumber = binding.textInputEditTextFlightNumber.getText().toString();
                String time = binding.textInputEditTextTime.getText().toString();

                if (name.isEmpty()) {
                    binding.textInputLayoutName.setError("Please Enter Name");

                } else if (flightNumber.isEmpty()) {
                    binding.textInputLayoutFlightNumber.setError("Please Enter Flight Number");
                } else if (time.isEmpty()) {
                    binding.textInputLayoutTime.setError("Please Set Time");
                } else {

                    Toast.makeText(getActivity(), "Save Successful", Toast.LENGTH_SHORT).show();

                    binding.textInputLayoutName.setError("");
                    binding.textInputLayoutFlightNumber.setError("");
                    binding.textInputLayoutTime.setError("");

                    db = Room.databaseBuilder(getActivity(), AppDatabase.class, "users-db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();

                    User user = new User(name, flightNumber, time);
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

        if (device == null) {
            binding.textViewBluetooth.setText("Please Select Your e-Paging Board");
        } else {
            binding.textViewBluetooth.setText("Connect With - " + device.getName());
        }

    }

    HomeFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (HomeFragmentListener) context;
    }

    public interface HomeFragmentListener {
        void gotoUserDetails();

        void gotoDeviceListFragment();
        void sendtoEpage(String combine);
    }
}