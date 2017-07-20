package zd.com.clock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;

/**
 * 电子时钟
 * Created by zhudi on 2017/7/18.
 */

@SuppressLint("AppCompatCustomView")
public class ElectronicClock extends TextView {
    private Context context;

    /**
     * 显示的格式
     */
    private String formatStr = "HH:mm:ss";
    private SimpleDateFormat formatter;

    public ElectronicClock(Context context) {
        super(context, null);
        init(context, null, 0);
    }

    public ElectronicClock(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs, 0);
    }

    public ElectronicClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TextViewStyle);
        if (ta.hasValue(R.styleable.TextViewStyle_textFormatStr)) {
            formatStr = ta.getString(R.styleable.TextViewStyle_textFormatStr);
        }
        try {
            formatter = new SimpleDateFormat(formatStr);
        } catch (Exception e) {
            formatter = new SimpleDateFormat("HH:mm:ss");
        }
        intent = new Intent(context, ClockService.class);
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TimeUpdate time) {
        if (time == null) {
            return;
        }
        updateClock(time.getTime());
    }

    private void updateClock(long event) {
        setText(formatter.format(event));
    }

    private Intent intent = null;
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (intent == null) {
            intent = new Intent(context, ClockService.class);
        }
        if (visibility == VISIBLE) {
            context.stopService(intent);
            context.startService(intent);
        } else {
            context.stopService(intent);
        }
    }
}
