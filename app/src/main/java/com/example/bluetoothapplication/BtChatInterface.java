package com.example.bluetoothapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class BtChatInterface extends AppCompatActivity {
    private final static String TAG = "BtChatInterface";
    private ListView msgListView;
    private EditText inputText;
    private Button send;
    public static Sc60MessageAdapter adapter;
    private String btName;
    private String btAddress;
    private static final int MENU_STATE = 17;
    private Sc60AcceptThread thread;
    private Sc60Bluetooth mBluetooth;

    public static List<Sc60ChatMessage> msgList = new ArrayList<Sc60ChatMessage>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.chat_interface);

        BtInformationApp app = (BtInformationApp)getApplication();

        btName = app.getBtName();
        btAddress = app.getBtAddress();
        thread = app.getReceiveThread();
        mBluetooth = app.getmBluetooth();
        Log.e("liujian","device:"+btName + "   address:"+btAddress);
            /* 设置标题为蓝牙名称 */
        setTitle(btName);

        Log.d(TAG, "远端设备"+btName+"已连接");

        adapter = new Sc60MessageAdapter(this, R.layout.msg_item, msgList);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    if(Sc60BroadcastReceiver.BT_CONNECTED_STATE != 1){
                        Sc60ChatMessage msg = new Sc60ChatMessage(content, Sc60ChatMessage.TYPE_SEND);
                        msgList.add(msg);
                        thread.sendData(msg.getContent().getBytes());
                        adapter.notifyDataSetChanged();
                        msgListView.setSelection(msgList.size());
                        inputText.setText("");
                   }else{
                        //Toast.makeText(getApplicationContext(),"设备已丢失",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "设备已丢失");
                    }

                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setIcon(null)
                .setTitle("断开")
                .setMessage("您确定断开“"+btName+"”吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_item, menu);

        // 添加按钮
        MenuItem item = menu.add(0,MENU_STATE, 0, "断开");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case MENU_STATE:
                        onBackPressed();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.enable_visible:
                mBluetooth.enableVisiblity();
                break;
            case R.id.clear_list:
                msgList.clear();
                adapter.notifyDataSetChanged();
                break;
            case R.id.device_bt_info:

                final MyDialog myDialog = new MyDialog(this);
                myDialog.setTitle("设备信息");
                myDialog.setMessage(btName,btAddress,0,0 );
                myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesOnClick() {
                        myDialog.dismiss();
                    }
                });

                myDialog.show();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy(){
        Sc60ConnectedThread.cancel();
        super.onDestroy();
    }


}
