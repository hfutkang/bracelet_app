package sctek.cn.ysbracelet.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

/**
 * Created by kang on 16-4-11.
 */
public class PersonalHRateStatisticsDayAdapter extends PersonalStatisticsBaseAdapter {

    private final static String TAG = PersonalHRateStatisticsDayAdapter.class.getSimpleName();

    private List<String> xVals;
    private Object lockObject = new Object();

    public PersonalHRateStatisticsDayAdapter(Context context, String device) {
        super(context, device);
        mDateManager = new YsDateManager(YsDateManager.DATE_FORMAT_DAY);
        initXValuesTask.execute();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    protected int getPageCount() {
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.HeartRate.CONTENT_URI
                                , new String[]{LocalDataContract.HeartRate.COLUMNS_NAME_TIME}
                                , LocalDataContract.HeartRate.COLUMNS_NAME_DEVICE + "=?"
                                , new String[]{deviceId}
                                , LocalDataContract.HeartRate.COLUMNS_NAME_TIME + " asc limit 1");
        String earliestDate = null;
        if(cursor.moveToNext()) {
            earliestDate = cursor.getString(0);
        }
        cursor.close();

        int pages = 0;
        try {
            pages = mDateManager.daysFromNowTo(earliestDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pages;
    }

    @Override
    protected LineData getDataSet(int position) {
        String dateStart = mDateManager.getDayDateBy(-position);
        String dateEnd = mDateManager.getDayDateBy(-(position - 1));
        Log.e(TAG, dateStart + " " + dateEnd);
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.HeartRate.CONTENT_URI
                                , new String[]{LocalDataContract.HeartRate.COLUMNS_NAME_RATE, LocalDataContract.HeartRate.COLUMNS_NAME_TIME}
                                , LocalDataContract.HeartRate.COLUMNS_NAME_DEVICE + "=?"
                                    + " AND " +LocalDataContract.HeartRate.COLUMNS_NAME_TIME + ">" + "'" + dateStart + "'"
                                    + " AND " + LocalDataContract.HeartRate.COLUMNS_NAME_TIME + "<" + "'" + dateEnd + "'"
                                , new String[]{deviceId}
                                , LocalDataContract.HeartRate.COLUMNS_NAME_TIME + " asc");

        ArrayList<Entry> points = new ArrayList<>();
        while(cursor.moveToNext()) {
            int rate = cursor.getInt(0);
            String date = cursor.getString(1);
            int xIndex = 0;
            try {
                xIndex = mDateManager.getMinutesOfDayFor(date);
                Log.e(TAG, date + " " + xIndex);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Entry entry = new Entry(rate, xIndex);
            points.add(entry);
        }

        cursor.close();

        LineDataSet dataSet = new LineDataSet(points, "data");
        dataSet.setDrawValues(true);
        dataSet.setDrawCircles(true);

        synchronized (lockObject) {
            if(xVals == null) {
                try {
                    lockObject.wait();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        LineData lineData = new LineData(xVals, dataSet);
        return lineData;
    }

    @Override
    protected List<String> getXValues(int position) {
        if(BleUtils.DEBUG) Log.e(TAG, "getXValues");
        int totalMinutes = 24*60;
        List<String> xVals = new ArrayList<>();
        for(int i = 0; i < totalMinutes; i++) {
            String xVal = formatTimeFor(i);
            xVals.add(xVal);
        }
        if(BleUtils.DEBUG) Log.e(TAG, "getXValues");
        return xVals;
    }

    private String formatTimeFor(int minute) {
        int h = minute/60;
        int m = minute%60;
        String hour = h < 10?"0" + h : h + "";
        String min = m < 10?"0" + m : m + "";
        return hour + ":" + min;
    }

    private AsyncTask<Void, Void, Void> initXValuesTask = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
            synchronized (lockObject) {
                xVals = getXValues(0);
                lockObject.notify();
            }
            return null;
        }
    };

}
