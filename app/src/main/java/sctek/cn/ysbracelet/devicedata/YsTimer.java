package sctek.cn.ysbracelet.devicedata;

/**
 * Created by kang on 16-2-19.
 */
public class YsTimer {

    private final static String TAG = "YsTimer";

    public int id;
    public int hour;
    public int minutes;
    public int mode;
    public boolean status;
    public String description;

    @Override
    public boolean equals(Object o) {
        if(o instanceof YsTimer) {
            YsTimer timer = (YsTimer)o;
            if(timer.id == id && timer.hour == hour && timer.minutes == minutes
                    && timer.mode == mode && status == status && description.equals(timer.description))
                return true;
        }
        return false;
    }
}
