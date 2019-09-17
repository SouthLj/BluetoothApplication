package com.example.bluetoothapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import static android.os.SystemClock.sleep;

public class MyDialog extends Dialog {
    private TextView nameInfo;
    private TextView addressInfo;
    private TextView sendBytesSize;
    private TextView receiveBytesSize;
    private TextView deviceTitle;
    private Button confirm;
    //private onNoOnclickListener noOnclickListener;
    private onYesOnclickListener yesOnclickListener;
    private String yesStr;
    private String dTitle;
    private String btName;
    private String btAddress;
    private int sendSize;
    private int receiveSize;

    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener){
        if(str != null){
            this.yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

    public MyDialog( Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog);
        //空白处不能取消动画

        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();

        //初始化界面数据
        setDialogInfo();

        //初始化界面控件的事件
        initEvent();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        confirm = findViewById(R.id.yes);
        nameInfo = (TextView) findViewById(R.id.dialog_bt_name);
        addressInfo = (TextView) findViewById(R.id.dialog_bt_address);
        sendBytesSize = (TextView)findViewById(R.id.dialog_bt_send_bytes);
        receiveBytesSize = (TextView)findViewById(R.id.dialog_bt_receive_bytes);
        deviceTitle = (TextView)findViewById(R.id.device_title);
    }

    private void initEvent(){
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(yesOnclickListener != null){
                    yesOnclickListener.onYesOnClick();
                }
            }
        });
    }


    public void setMessage(String name, String address, int send, int receive){
        this.btName = name;
        this.btAddress = address;
        this.sendSize = send;
        this.receiveSize = receive;
    }

    public void setTitle(String title){
        dTitle = title;
    }

    public void setDialogInfo(){
        if(dTitle != null){
            deviceTitle.setText(dTitle);
        }
        if(btName != null){
            nameInfo.setText(btName);
        }
        if(btAddress != null){
            addressInfo.setText(btAddress);
        }
        if(yesStr != null){
            confirm.setText(yesStr);
        }
        sendBytesSize.setText(String.valueOf(sendSize));
        receiveBytesSize.setText(String.valueOf(receiveSize));
    }

    public interface onYesOnclickListener{
        public void onYesOnClick();
    }
}
