package com.example.bluetoothapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class Sc60AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private final BluetoothAdapter mBluetoothAdapter;
    private Sc60ConnectedThread mConnectedThread;
    private final Handler mHandler;

    private static final String NAME = "myServerSocket";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //这个为蓝牙串口服务的UUID

    public Sc60AcceptThread(BluetoothAdapter mAdapter, Handler handler) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        mBluetoothAdapter = mAdapter;
        mHandler = handler;
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
        } catch (IOException e) { }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        Log.e("liujian", "start AcceptThread");
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)
                Message msg = mHandler.obtainMessage();
                msg.what = 3;
                msg.obj = socket.getRemoteDevice();
                mHandler.sendMessage(msg);

                manageConnectedSocket(socket);
                try {
                    mmServerSocket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void manageConnectedSocket(BluetoothSocket socket){
        //只支持同时处理一个连接
        Log.e("liujian", "start manageConnectedSocket");
        if( mConnectedThread != null) {
            mConnectedThread.cancel();
        }
        // mHandler.sendEmptyMessage(Constant.MSG_GOT_A_CLINET);
        mConnectedThread = new Sc60ConnectedThread(socket, mHandler);
        mConnectedThread.start();
    }

    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
            //mHandler.sendEmptyMessage(Constant.MSG_FINISH_LISTENING);
        } catch (IOException e) { }
    }

    public void sendData(byte[] data) {
        if( mConnectedThread!=null){
            mConnectedThread.write(data);
        }
    }

}

