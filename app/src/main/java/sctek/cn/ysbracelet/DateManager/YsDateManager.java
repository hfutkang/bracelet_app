package sctek.cn.ysbracelet.DateManager;

import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kang on 16-3-4.
 */
public class YsDateManager {

    private final static String TAG = YsDateManager.class.getSimpleName();

    public final static String DATE_FORMAT_DAY = "yyyy-MM-dd";
    public final static String DATE_FORMAT_SECOND = "yyyy-MM-dd HH:mm";
    public final static String DATE_FORMAT_MONTH = "yyyy-MM";
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

    public String getDayDateBy(int daysFromNow) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_DAY);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, daysFromNow);
        String date = format.format(calendar.getTime());
        return date;
    }

    public String getNextDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mCalendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String date = mShowDateFormat.format(calendar.getTime());
        return date;
    }

    public String showPreviousDate(TextView tv) {
        mCalendar.add(Calendar.DAY_OF_MONTH, -1);
        String date = (mShowDateFormat.format(mCalendar.getTime()));
        tv.setText(date);
        return date;
    }

    public String getPreviousDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mCalendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String date = mShowDateFormat.format(calendar.getTime());
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

    public String getMonthBy(int monthFromNow) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthFromNow);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_MONTH);
        return format.format(calendar.getTime());//月份从0开始，需要加1
    }

    public int getMinuteOfDay() {
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        return hour*60 + mCalendar.get(Calendar.MINUTE);
    }

    public int getMinutesOfDayFor(String date) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_SECOND);
        Date time = format.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour*60 + calendar.get(Calendar.MINUTE);
    }

    public int getDayOfWeekFor(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_SECOND);
        Date time = format.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);

        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        if(weekDay == Calendar.SUNDAY)
            return 6;

        return calendar.get(Calendar.DAY_OF_WEEK) -2;//x轴下标从0开始，Calendar.Monday = 2。
    }

    public int getDayOfMonthFor(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_SECOND);
        Date time = format.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);

        return calendar.get(Calendar.DAY_OF_MONTH) - 1;//日下标从0开始，需减1
    }

    public int daysFromNowTo(String date) throws ParseException {
        if(date == null)
            return 0;

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_SECOND);
        Date timeEnd = format.parse(date);

        long millisEnd = timeEnd.getTime();
        long millisStart = mCalendar.getTimeInMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeEnd);
        int days = (int)(millisStart/(1000*60*60*24)) - (int)(millisEnd/(1000*60*60*24)) + 1;

        return days;
    }

    public int monthsFromNowTo(String date) throws ParseException {
        if (date == null)
            return 0;

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_SECOND);
        Date timeEnd = format.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeEnd);
        int yearEnd = calendar.get(Calendar.YEAR);
        int yearStart = mCalendar.get(Calendar.YEAR);

        int monthEnd = calendar.get(Calendar.MONTH);
        int monthStart = calendar.get(Calendar.MONTH);

        int years = yearStart - yearEnd;
        int months = monthStart - monthEnd + 1;

        return years*12 + months;
    }

    public int weeksFromNowTo(String date) throws ParseException {
        if (date == null)
            return 0;

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_SECOND);
        int days = daysFromNowTo(date);
        Date time = format.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int weekDayEnd = calendar.get(Calendar.DAY_OF_WEEK);
        int weekDayStart = mCalendar.get(Calendar.DAY_OF_WEEK);
        if(days <= 7
                && calendar.get(Calendar.WEEK_OF_YEAR) == mCalendar.get(Calendar.WEEK_OF_YEAR))//判断是否在同一周
            return 1;
        int days1 = 7 - weekDayEnd + weekDayStart;//计算首尾两周的天数
        int daysleft = days - days1;
        if(daysleft < 0)
            return 2;
        return daysleft/7 + 2;
    }

    public String getFirstDayOfWeekBy(int weeksFromNow) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_DAY);

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.WEEK_OF_YEAR, weeksFromNow);

        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_MONTH, -weekDay + 2);
        return format.format(calendar.getTime());
    }

    public String getLastDayOfWeekBy(int weeksFromNow) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_DAY);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, weeksFromNow);

        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_MONTH, 8 - weekDay);
        return format.format(calendar.getTime());
    }

    public String getFisrtDayOfNextWeekBy(int weeksFromNow) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_DAY);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, weeksFromNow);

        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_MONTH, 9 - weekDay);
        return format.format(calendar.getTime());
    }

    public String getFirstDayOfMonthBy(int monthFromNow) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_DAY);

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, -monthFromNow);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return format.format(calendar.getTime());
    }

    public String getLastDayOfMonthBy(int monthFromNow) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_DAY);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -monthFromNow);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format.format(calendar.getTime());
    }

    public int getMaxDayOfMonthBy(int monthFromNow) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -monthFromNow);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
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
