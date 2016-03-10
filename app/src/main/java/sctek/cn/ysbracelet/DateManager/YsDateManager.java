package sctek.cn.ysbracelet.DateManager;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kang on 16-3-4.
 */
public class YsDateManager {

    private final static String TAG = YsDateManager.class.getSimpleName();

    private final static String DATE_FORMAT = "yyyy-MM-dd";

    private Calendar mCalendar;
    private SimpleDateFormat mSimpleDateFormat;

    public YsDateManager() {
        mCalendar = Calendar.getInstance();
        mSimpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
    }

    public String showCurrentDate(TextView tv) {
        String date = (mSimpleDateFormat.format(mCalendar.getTime()));
        tv.setText(date);
        return date;
    }

    public String showNextDate(TextView tv) {
        mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        String date = (mSimpleDateFormat.format(mCalendar.getTime()));
        tv.setText(date);
        return date;
    }

    public String showPreviousDate(TextView tv) {
        mCalendar.add(Calendar.DAY_OF_MONTH, -1);
        String date = (mSimpleDateFormat.format(mCalendar.getTime()));
        tv.setText(date);
        return date;
    }

    public String getCurrentDate() {
        Date date = new Date();
       return mSimpleDateFormat.format(date);
    }

    public int getYear() {
        return mCalendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return mCalendar.get(Calendar.MONTH);
    }

    public int getDayOfMonth() {
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setDate(int y, int m, int d) {
        mCalendar.set(y, m, d);
    }
}
