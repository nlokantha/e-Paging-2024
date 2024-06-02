package com.example.e_paging2024.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e_paging2024.databinding.DeviceListItemsBinding;
import com.example.e_paging2024.databinding.FragmentDeviceListBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeviceListFragment extends Fragment {
    List<BluetoothDevice> deviceList = new ArrayList<>();
    DeviceAdapter adapter = new DeviceAdapter();
    private static final int REQUEST_BLUETOOTH_PERMISSION = 100;
    BluetoothAdapter bluetoothAdapter;

    public DeviceListFragment() {
        // Required empty public constructor
    }

    FragmentDeviceListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDeviceListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            return;
        }

        checkPermissionsAndStart();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelDeviceListFragment();
            }
        });
    }

    private void checkPermissionsAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 and above
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.BLUETOOTH_ADVERTISE},
                        REQUEST_BLUETOOTH_PERMISSION);
            } else {
                getPairedDevices();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6 to Android 11
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.BLUETOOTH,
                                Manifest.permission.BLUETOOTH_ADMIN},
                        REQUEST_BLUETOOTH_PERMISSION);
            } else {
                getPairedDevices();
            }
        } else {
            // Below Android 6, permissions are granted at install time
            getPairedDevices();
        }
    }

    @SuppressLint("MissingPermission")
    private void getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        deviceList.addAll(pairedDevices);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                getPairedDevices();
            } else {
                Toast.makeText(getContext(), "Bluetooth permissions are required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
        @NonNull
        @Override
        public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DeviceViewHolder(DeviceListItemsBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
            BluetoothDevice device = deviceList.get(position);
            holder.setupUI(device);
        }

        @Override
        public int getItemCount() {
            return deviceList.size();
        }

        class DeviceViewHolder extends RecyclerView.ViewHolder {
            DeviceListItemsBinding mBinding;

            public DeviceViewHolder(DeviceListItemsBinding vhBinding) {
                super(vhBinding.getRoot());
                mBinding = vhBinding;
            }

            @SuppressLint("MissingPermission")
            public void setupUI(BluetoothDevice device) {
                mBinding.textViewDeviceName.setText(device.getName());
                mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.selectedDevice(device);
                    }
                });
            }
        }
    }

    DeviceListFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (DeviceListFragmentListener) context;
    }

    public interface DeviceListFragmentListener {
        void cancelDeviceListFragment();

        void selectedDevice(BluetoothDevice device);
    }
}
