package zd.com.timer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by apple on 2017/7/19.
 */

public class TimerService extends Service {
    private Timer timer;
    /**
     * 间隔，默认5s一次
     */
    private int space;

    private long milliSecond;

    private Intent intentReturn = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        space = intent.getIntExtra(Constants.SPACE, 5);
        milliSecond = space * 1000;
        intentReturn = new Intent("zd.com.timer");
        beginTimer();
        return super.onStartCommand(intent, flags, startId);
    }

    private void beginTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        }, 0, milliSecond);
    }

    private void updateTime() {
        if (intentReturn == null) {
            intentReturn = new Intent("zd.com.timer");
        }
        Log.d("TimerService", "----------->updateTime");
        sendBroadcast(intentReturn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
