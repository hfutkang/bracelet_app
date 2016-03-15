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

    public final static String DATE_FORMAT_SHOW1 = "yyyy-MM-dd";
    public final static String DATE_FORMAT_SHOW2 = "yyyy-MM-dd HH:mm";
    public final static String DATE_FORMAT_SHOW3 = "yyyy-MM";
    public final static String DATE_FORMAT_HTTP = "yyyyMMddHHmmss";


    private Calendar mCalendar;
    private SimpleDateFormat mShowDateFormat;
    private SimpleDateFormat mHttpDateFormat;

    public YsDateManager(String format) {
        mCalendar = Calendar.getInstance();
        mShowDateFormat = new SimpleDateFormat(format);
        mHttpDateFormat = new SimpleDateFormat(DATE_FORMAT_HTTP);
    }

    public String showCurrentDate(TextView tv) {
        String date = (mShowDateFormat.format(mCalendar.getTime()));
        tv.setText(date);
        return date;
    }

    public String showNextDate(TextView tv) {
        mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        String date = (mShowDateFormat.format(mCalendar.getTime()));
        tv.setText(date);
        return date;
    }

    public String showPreviousDate(TextView tv) {
        mCalendar.add(Calendar.DAY_OF_MONTH, -1);
        String date = (mShowDateFormat.format(mCalendar.getTime()));
        tv.setText(date);
        return date;
    }

    public String showNextMonth(TextView tv) {
        mCalendar.add(Calendar.MONTH, 1);
        String date = mShowDateFormat.format(mCalendar.getTime());
        tv.setText(date);
        return date;
    }

    public String showPreviousMonth(TextView tv) {
        mCalendar.add(Calendar.MONTH, -1);
        String date = mShowDateFormat.format(mCalendar.getTime());
        tv.setText(date);
        return date;
    }

    public String getCurrentDate() {
        Date date = new Date();
       return mShowDateFormat.format(date);
    }

    public String getHttpDate() {
        return mHttpDateFormat.format(mCalendar.getTime());
    }

    public String getFirstDayOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return mHttpDateFormat.format(calendar.getTime());
    }

    public String getLastDayOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return mHttpDateFormat.format(calendar.getTime());
    }

    public int getActualMaximumOfCurrentMonth() {
        return mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
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

    public int getDayOfWeek() { return  mCalendar.get(Calendar.DAY_OF_WEEK); }

    public void setDate(int y, int m, int d) {
        mCalendar.set(y, m, d);
    }

    public void setDate(Date date) {
        mCalendar.setTime(date);
    }
}
