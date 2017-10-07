package ch.ethz.inf.vs.a1.ankoller.ble;

import android.Manifest;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity:-> Log: ";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int FINE_LOCATION_PERMISSION = 2;

    private final int SCAN_TIMEOUT = 60_000;

    private int timeLeftToScan = SCAN_TIMEOUT;

    private BluetoothAdapter bluetoothAdapter;      // Bluetooth Adapter

    private ArrayList<BluetoothDevice> deviceList;
    private ListView listView;
    private ArrayAdapter<BluetoothDevice> viewAdapter;
    private BluetoothLeScanner bleScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceList = new ArrayList<BluetoothDevice>();
        listView = (ListView) findViewById(R.id.list_view);
        viewAdapter = new ArrayAdapter<BluetoothDevice>(this, android.R.layout.simple_list_item_1, deviceList);
        listView.setAdapter(viewAdapter);
        listView.setOnItemClickListener(this);

        Log.i(TAG, "ListView created.");

        // ProgressBar View
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        Log.i(TAG, "ProgressBar created.");

        initializeBluetooth();




    }

    private void initializeBluetooth() {


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);
        }else{

            // Use this check to determine whether BLE is supported on the device.  Then you can
            // selectively disable BLE-related features.
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
                finish();
            }

            final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();
            if (bluetoothAdapter == null) {
                // Device does not support Bluetooth
                Toast.makeText(this, R.string.bl_not_supported, Toast.LENGTH_SHORT).show();
                finish();
            }

            if(bluetoothAdapter.isEnabled()) {
                if (timeLeftToScan > 0) {
                    //startBLEScan();
                }
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }


        }
    }

    private void startBLEScan() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        //onDeviceSelected(deviceList.get(position));
    }

    ScanCallback callback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //updateList(callbackType, result);
        }
    };
}

