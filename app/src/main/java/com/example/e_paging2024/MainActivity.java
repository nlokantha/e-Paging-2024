package com.example.e_paging2024;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.e_paging2024.Fragment.DeviceListFragment;
import com.example.e_paging2024.Fragment.HomeFragment;
import com.example.e_paging2024.Fragment.UpdateUserFragment;
import com.example.e_paging2024.Fragment.UserDetailsFragment;
import com.example.e_paging2024.Models.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener,
        UserDetailsFragment.UserDetailsFragmentListener, UpdateUserFragment.UpdateListener
        , DeviceListFragment.DeviceListFragmentListener {

    private static final String TAG = "demo";
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice mDevice;
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mSocket;
    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new HomeFragment(), "home-fragment")
                .commit();


        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void gotoUserDetails() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new UserDetailsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoDeviceListFragment() {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.rootView, new DeviceListFragment())
                .commit();
    }

    @Override
    public void sendtoEpage(String combine) {
        try {
            if (outputStream != null) {
                outputStream.write(combine.getBytes());
                outputStream.flush();
                Toast.makeText(this, "Data sent over Bluetooth", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bluetooth output stream not available", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error sending data over Bluetooth: " + e.getMessage());
        }
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
    public void showInBoard(User user) {
        String combo = user.getName()+"$"+user.getFilghtNumber()+"$"+user.getTime()+"$";
        try {
            if (outputStream != null) {
                outputStream.write(combo.getBytes());
                outputStream.flush();
                Toast.makeText(this, "Data sent over Bluetooth", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bluetooth output stream not available", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error sending data over Bluetooth: " + e.getMessage());
        }
    }

    @Override
    public void cancelDeviceListFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void selectedDevice(BluetoothDevice device) {
        this.mDevice=device;

        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home-fragment");
        if (homeFragment != null) {
            homeFragment.setDevice(device);
            getSupportFragmentManager().popBackStack();
        }

        mDevice=bluetoothAdapter.getRemoteDevice(device.getAddress());
        try {
            mSocket = mDevice.createRfcommSocketToServiceRecord(uuid);
            mSocket.connect();
            if (mSocket.isConnected()){
                Toast.makeText(this, "Connected !!!!", Toast.LENGTH_SHORT).show();
                outputStream = mSocket.getOutputStream();
            }else {
                Toast.makeText(this, "Not Connect !!!!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mSocket != null) {
                mSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}