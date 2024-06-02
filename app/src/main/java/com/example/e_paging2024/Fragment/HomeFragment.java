package com.example.e_paging2024.Fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.e_paging2024.Database.AppDatabase;
import com.example.e_paging2024.Models.User;
import com.example.e_paging2024.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    AppDatabase db;
    private BluetoothDevice device;
    BluetoothAdapter bluetoothAdapter;
    public static final int REQUEST_ENABLE_BT = 100;
    String selectedTime;

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null && result.getResultCode() == RESULT_OK) {
                mListener.gotoDeviceListFragment();
            } else {
                Toast.makeText(getActivity(), "You Must Turn On Bluetooth", Toast.LENGTH_SHORT).show();
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
        binding.timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                selectedTime = hourOfDay + ":" + minute;
            }
        });
        binding.buttonBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndStartBluetooth();
            }
        });
        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.textInputEditTextName.getText().toString();
                String flightNumber = binding.textInputEditTextFlightNumber.getText().toString();
                String time = selectedTime;

                String combine = name + "$" + flightNumber + "$" + time + "$";
                mListener.sendtoEpage(combine);
            }
        });
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.textInputEditTextName.getText().toString();
                String flightNumber = binding.textInputEditTextFlightNumber.getText().toString();
                String time = selectedTime;

                if (name.isEmpty()) {
                    binding.textInputLayoutName.setError("Please Enter Name");
                } else if (flightNumber.isEmpty()) {
                    binding.textInputLayoutFlightNumber.setError("Please Enter Flight Number");
                } else {
                    Toast.makeText(getActivity(), "Save Successful", Toast.LENGTH_SHORT).show();
                    binding.textInputLayoutName.setError("");
                    binding.textInputLayoutFlightNumber.setError("");

                    db = Room.databaseBuilder(getActivity(), AppDatabase.class, "users-db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();

                    User user = new User(name, flightNumber, time);
                    db.userDao().insert(user);

                    binding.textInputEditTextName.setText("");
                    binding.textInputEditTextFlightNumber.setText("");
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

    private void checkPermissionsAndStartBluetooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 and above
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_ENABLE_BT);
            } else {
                startBluetooth();
            }
        } else {
            // Below Android 12
            startBluetooth();
        }
    }

    @SuppressLint("MissingPermission")
    private void startBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startForResult.launch(enableBtIntent);
        } else {
            mListener.gotoDeviceListFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBluetooth();
            } else {
                Toast.makeText(getContext(), "Bluetooth permission is required", Toast.LENGTH_SHORT).show();
            }
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
