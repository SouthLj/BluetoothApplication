package com.example.bluetoothapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class Sc60BroadcastReceiver extends BroadcastReceiver {
    private final static String TAG = "MyReceiver";
    private Handler handler;
    public static int BT_CONNECTED_STATE = 0;

    @Override
    public void onReceive(Context context, Intent intent){
       // Log.e(TAG, "bluetooth scan mode is changed111" );
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.e(TAG, "device name:" + device.getName() + "   address:" + device.getAddress());
            Message msg = handler.obtainMessage();
            msg.what = 2;
            msg.obj = device;
            handler.sendMessage(msg);
            // adapter.notifyDataSetChanged();
        }
        if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)){
            Log.e(TAG, "bluetooth scan mode is changed" );
        }
        if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)){
            BT_CONNECTED_STATE = 1;
            //Toast.makeText(context, "设备失去连接", Toast.LENGTH_SHORT).show();
            Log.d("liujian", "设备失去连接");
        }
        if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
            BT_CONNECTED_STATE = 0;
        }
        if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
            Message msg = handler.obtainMessage();
            msg.what = 4;
            handler.sendMessage(msg);
        }

    }

    public Sc60BroadcastReceiver(Handler handler){
        this.handler = handler;
    }
}
