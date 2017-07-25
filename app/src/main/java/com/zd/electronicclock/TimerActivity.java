package com.zd.electronicclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Locale;

import zd.com.timer.Constants;
import zd.com.timer.TimerService;

/**
 * 定时器
 */
public class TimerActivity extends AppCompatActivity {
    TextView tv;
    TextView tvOther;
    private MsgReceiver receiver = null;
    private Intent intent = null;
    private long cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        tv = (TextView) findViewById(R.id.tv);
        tvOther = (TextView) findViewById(R.id.tvOther);
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
            cnt++;
            tvOther.setText(getStringTime());
        }
    }


    @Override
    protected void onDestroy() {
        stopService(intent);
        //注销广播
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * 返回显示的时间
     *
     * @return
     */
    private String getStringTime() {
        int hour = (int) cnt / 3600;
        int min = (int) cnt % 3600 / 60;
        int second = (int) cnt % 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, min, second);
    }
}
