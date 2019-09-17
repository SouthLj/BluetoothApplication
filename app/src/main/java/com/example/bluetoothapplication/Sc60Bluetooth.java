package com.example.bluetoothapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class Sc60Bluetooth extends Activity {
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler handler;
    private Context context;
    String TAG = "Sc20Bluetooth";

    /**
     * @brief Sc20Bluetooth
     * @author liujian
     * @date 20190703
     * @version 1.0
     */
    Sc60Bluetooth(Context context, Handler handler, BluetoothAdapter mBluetoothAdapter){
        this.context = context;
        this.handler = handler;
        this.mBluetoothAdapter = mBluetoothAdapter;

        if(mBluetoothAdapter != null ) {
             mBluetoothAdapter.enable();
            if(!mBluetoothAdapter.isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else{
                Log.d(TAG,"bluetooth is  enable");
            }
        }
        else{
            Toast.makeText(context, "No support bluetoooth", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    /**
     * @brief checkEnable
     * @author liujian
     * @date 20190704
     * @version 1.0
     */
    public boolean checkEnable(){
        return mBluetoothAdapter.isEnabled();
    }

    /**
     * @brief enableDiscovery
     * @author liujian
     * @date 20190704
     * @version 1.0
     */
    public void enableDiscovery(){

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    /**
     * @brief queryPairedDevides
     * @author liujian
     * @date 20190703
     * @version 1.0
     */
    public ArrayList<ChildInfo> queryPairedDevides(){
        ArrayList<ChildInfo> list = new ArrayList<ChildInfo>();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                ChildInfo temp = new ChildInfo();
                //list.add(device.getName() + "\n" + device.getAddress());
                temp.bluetoothName = device.getName();
                temp.bluetoothAddress = device.getAddress();
                list.add(temp);
            }

        }
        return list;
    }

    /**
     * @brief enableDiscoverability
     * @author liujian
     * @date 20190703
     * @version 1.0.1
     * @modify 20190704
     */
    public void enableVisiblity(){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        context.startActivity(discoverableIntent);
    }
    /**
     * @brief close
     * @author liujian
     * @date 20190703
     * @version 1.0
     */
    public void close(){
        mBluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(),"bluetooth off", Toast.LENGTH_LONG)
                .show();
    }
}
