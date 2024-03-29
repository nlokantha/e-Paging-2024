package com.example.e_paging2024;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.e_paging2024.Fragment.DeviceListFragment;
import com.example.e_paging2024.Fragment.HomeFragment;
import com.example.e_paging2024.Fragment.UpdateUserFragment;
import com.example.e_paging2024.Fragment.UserDetailsFragment;
import com.example.e_paging2024.Models.User;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener,
        UserDetailsFragment.UserDetailsFragmentListener, UpdateUserFragment.UpdateListener
, DeviceListFragment.DeviceListFragmentListener {
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView,new HomeFragment())
                .commit();


        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void gotoUserDetails() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView,new UserDetailsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoDeviceListFragment() {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.rootView,new DeviceListFragment())
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
    @Override
    public void updateUser(User user) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, UpdateUserFragment.newInstance(user))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancleDeviceListFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void selectedDevice(BluetoothDevice device) {

    }
}