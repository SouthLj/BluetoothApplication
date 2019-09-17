package com.example.bluetoothapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private ExpandableListView ex;
    //声明一个ExpandableListView 用的数据源
    private List<ExpandInfo> list=new ArrayList<ExpandInfo>();
    private List<ChildInfo> clist=new ArrayList<ChildInfo>();
    private MyAdapter adapter;
    private Sc60BroadcastReceiver receiver;
    private Sc60Bluetooth mBluetooth;
    private Sc60AcceptThread connectBtThread;
    private BluetoothAdapter mBluetoothAdapter;
    private String btName;
    private String btAddress;
    private LoadingDialog.Builder loadBuilder;
    private LoadingDialog dialog;
    //final ProgressDialog progressDialog = new ProgressDialog(this);
   // final ProgressBar progressBar = new ProgressBar(this);

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            // Log.e(TAG, "liujian:"+ msg.obj);
            switch (msg.what){
                case 1: Log.e(TAG, "liujian:"+ msg.obj);
                        Sc60ChatMessage msg1 = new Sc60ChatMessage((String) msg.obj, Sc60ChatMessage.TYPE_RECEIVED);
                        BtChatInterface.msgList.add(msg1);
                        BtChatInterface.adapter.notifyDataSetChanged();
                break;
                case 2: BluetoothDevice bt = (BluetoothDevice)msg.obj;
                        //ExpandInfo info=new ExpandInfo();
                        ChildInfo childList = new ChildInfo();

                        childList.bluetoothName = (bt.getName() == null ? "未命名": bt.getName());
                        childList.bluetoothAddress = bt.getAddress();
                        //Log.e(TAG,"device:"+childList.bluetoothName + "   address:" +childList.bluetoothAddress);
                        clist.add(childList);
                       // info.childList = clist;
                        //list.get(1).childList.add(childList);
                        list.get(1).childList = clist;
                        //list.add(info);
                        adapter.notifyDataSetChanged();
                        break;
                case 4:
                       // progressDialog.dismiss();
                    //loadBuilder.
                    dialog.dismiss();
                    break;
                case 3: BluetoothDevice remoteBt = (BluetoothDevice)msg.obj;
                /*
                        Intent intent = new Intent(MainActivity.this, BtChatInterface.class);
                        Bundle bundle = new Bundle();
                        btName = remoteBt.getName();
                        btAddress = remoteBt.getAddress();
                       // BluetoothInfoToActivity btInfo = new BluetoothInfoToActivity(mBluetooth, btName, btAddress);
                       // bundle.putSerializable("btInfo", btInfo);
                        //bundle.putString("btName",remoteBt.getName() );
                        //bundle.putString("btAddress", remoteBt.getAddress());
                        //bundle.put
                        intent.putExtras(bundle);
                       // Toast.makeText(this, "远端设备"+remoteBt.getName()+"已连接",Toast.LENGTH_SHORT).show();
                */
                        btName = (remoteBt.getName() ==null ? "未命名":remoteBt.getName());
                        btAddress = remoteBt.getAddress();
                        BtInformationApp app = (BtInformationApp)getApplication();
                        app.setBtName(btName);
                        app.setBtAddress(btAddress);
                        app.setmBluetooth(mBluetooth);
                        app.setReceiveThread(connectBtThread);
                        Intent intent = new Intent(MainActivity.this, BtChatInterface.class);
                        startActivity(intent);
                        break;
                default:break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("未连接");
        ex=(ExpandableListView) findViewById(R.id.listView1);
        //初始化数据源
        initList();

        mBluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
        mBluetooth = new Sc60Bluetooth(this, mHandler, mBluetoothAdapter);

        adapter=new MyAdapter(MainActivity.this, list);
        ex.setAdapter(adapter);
        //ExpandableListView子条目点击事件
        ex.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String str=((ChildInfo)adapter.getChild(groupPosition, childPosition)).bluetoothName;
                //Toast.makeText(MainActivity.this, "客户端功能尚未开通", 0).show();
                Log.d(TAG, "客户端功能尚未开通");
                //Log.e(TAG,"start new acyivity named BtChatInterface" + str);
                /*
                Intent intent = new Intent(MainActivity.this, BtChatInterface.class);
                Bundle bundle = new Bundle();
                bundle.putString("btName",((ChildInfo)adapter.getChild(groupPosition, childPosition)).bluetoothName );
                bundle.putString("btAddress", ((ChildInfo)adapter.getChild(groupPosition, childPosition)).bluetoothAddress);
               // ArrayList<String> msgList = new ArrayList<String>();
               // msgList.add(((ChildInfo)adapter.getChild(groupPosition, childPosition)).bluetoothName);
               // msgList.add(((ChildInfo)adapter.getChild(groupPosition, childPosition)).bluetoothAddress);
               // bundle.putString("bluetoothInfo", ((ChildInfo)adapter.getChild(groupPosition, childPosition)).bluetoothAddress);
                intent.putExtra("info",bundle);
                startActivity(intent);*/
                return false;
            }
        });

        checkPermissions();

    }
    /**
     * @brief registerBroadcastAction
     * @author liujian
     * @date 20190704
     * @version 1.0
     */
    private void registerBroadcastAction(){
        receiver = new Sc60BroadcastReceiver(mHandler);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
       // filter.addAction(BluetoothDevice.);
        registerReceiver(receiver, filter);
        //Log.e(TAG, "register success");
    }

    @Override
    protected void onDestroy(){
        unregisterReceiver(receiver);
        super.onDestroy();
    }


    //初始化数据源
    private void initList() {
        ExpandInfo pairedInfo = new ExpandInfo();
        pairedInfo.title = "已配对";
        list.add(pairedInfo);
        ExpandInfo connectedInfo = new ExpandInfo();
        connectedInfo.title = "其他设备";
        list.add(connectedInfo);
        registerBroadcastAction();
     /*
        for(int i=0;i<6;i++){
            //创建组对象
            ExpandInfo info=new ExpandInfo();
            //循环添加组名
            info.title="ExpandGroup"+i;
            //创建子条目数据源
            List<ChildInfo> clist=new ArrayList<ChildInfo>();
            for(int j=0;j<10;j++){
                //创建子对象
                ChildInfo childinfo=new ChildInfo();
                //循环添加用户头像和昵称
                childinfo.bluetoothName="str1" + j;
                childinfo.bluetoothAddress ="ExpandChild"+j;
                //将子对象添加到子数据源
                clist.add(childinfo);
            }
            //将子数据源赋值给组对象
            info.childList=clist;
            //将组对象添加到总数据源
            list.add(info);
        }*/
    }

    public void reseachBt(View view) {
        if (!mBluetooth.checkEnable())
           // Toast.makeText(this, "蓝牙未打开", Toast.LENGTH_SHORT)
             //       .show();
        Log.d(TAG, "蓝牙未打开");
       // final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
       // lv.setAdapter(adapter);
        clist.clear();
        list.get(1).childList = clist;
        adapter.notifyDataSetChanged();
        showProgressDialog();

        mBluetooth.enableDiscovery();
    }

    public void openBt(View view){
        /*
        mBluetooth.enableVisiblity();
        Toast.makeText(this, "蓝牙300s可见已开启", Toast.LENGTH_SHORT)
                .show();
                */
       // Toast.makeText(this, "开始监听客户端", Toast.LENGTH_SHORT)
       //         .show();
        Log.d(TAG,"开始监听客户端" );
        connectBtThread = new Sc60AcceptThread(mBluetoothAdapter, mHandler);
        connectBtThread.start();

    }

    private void checkPermissions() {
        RxPermissions rxPermissions = new RxPermissions((Activity) this);
        rxPermissions.request(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.BLUETOOTH)
                .subscribe(new io.reactivex.functions.Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            // 用户已经同意该权限
                           // Toast.makeText(MainActivity.this, "已获取相关权限", Toast.LENGTH_SHORT)
                           //         .show();
                            ArrayList<ChildInfo> childList = mBluetooth.queryPairedDevides();
                            list.get(0).childList = childList;
                            adapter.notifyDataSetChanged();
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                           // Toast.makeText(MainActivity.this, "无法获取权限", Toast.LENGTH_LONG)
                                 //   .show();
                            Log.d(TAG,"无法获取权限" );
                        }
                    }
                });
    }

    private void showProgressDialog(){
        loadBuilder = new LoadingDialog.Builder(this)
                .setMessage("搜索设备中...")
                .setCancelOutside(false)
                .setCancelable(true);
        dialog = loadBuilder.create();
        dialog.show();

    }

}
