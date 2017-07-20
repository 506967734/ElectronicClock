package com.zd.electronicclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import zd.com.timer.Constants;
import zd.com.timer.TimerService;

/**
 * 定时器
 */
public class TimerActivity extends AppCompatActivity {
    TextView tv;
    private MsgReceiver receiver = null;
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        tv = (TextView) findViewById(R.id.tv);
        initService();
        initReceiver();
    }

    private void initService() {
        if (intent == null) {
            intent = new Intent(this, TimerService.class);
            intent.putExtra(Constants.SPACE, 3);
        }
        startService(intent);
    }

    private void initReceiver() {
        if (receiver == null)
            receiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("zd.com.timer");
        registerReceiver(receiver, intentFilter);
    }


    /**
     * 广播接收器
     *
     * @author len
     */
    String str = "s";

    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            str = str + "s";
            tv.setText(str);
        }
    }


    @Override
    protected void onDestroy() {
        stopService(intent);
        //注销广播
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
