package zd.com.clock;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class ClockService extends Service {
    private long time;

    private Timer mTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        beginClock();
        return super.onStartCommand(intent, flags, startId);
    }

    private void beginClock() {
        initTime();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        }, 0, 1000);
    }

    private void updateTime() {
        sendEvent();
        //revise time
        initTime();
    }

    private void sendEvent() {
        Log.d("ClockService", "------------>sendEvent");
        EventBus.getDefault().post(new TimeUpdate(time));
    }

    /**
     * To get the exact time now and to assign the values .
     */
    @SuppressWarnings("deprecation")
    private void initTime() {
        //Time time = new Time(System.currentTimeMillis());
        time = new Date().getTime();
    }

    // Monitoring the screen on to reset time
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initTime();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
        unregisterReceiver(mReceiver);
    }
}
