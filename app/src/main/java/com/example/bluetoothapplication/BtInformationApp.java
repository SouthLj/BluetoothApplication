package com.example.bluetoothapplication;

import android.app.Application;

public class BtInformationApp extends Application {
    private String btName;
    private String btAddress;
    private Sc60Bluetooth mBluetooth;
    private Sc60AcceptThread receiveThread;

    public void setBtName(String name){
        this.btName = name;
    }

    public void setBtAddress(String address){
        this.btAddress = address;
    }

    public void setmBluetooth(Sc60Bluetooth mBluetooth){
        this.mBluetooth = mBluetooth;
    }

    public void setReceiveThread(Sc60AcceptThread thread){
        this.receiveThread = thread;
    }

    public String getBtName(){
        return btName;
    }

    public String getBtAddress(){
        return btAddress;
    }

    public Sc60Bluetooth getmBluetooth(){
        return mBluetooth;
    }
    public Sc60AcceptThread getReceiveThread(){
        return receiveThread;
    }
}
