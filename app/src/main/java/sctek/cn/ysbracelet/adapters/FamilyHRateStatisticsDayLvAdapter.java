package sctek.cn.ysbracelet.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

/**
 * Created by kang on 16-3-24.
 */
public class FamilyHRateStatisticsDayLvAdapter extends FamilyStatisticsBaseLvAdapter{

    private static final String TAG = FamilyHRateStatisticsDayLvAdapter.class.getSimpleName();

    private List<String> xVals;
    private Object lockObject = new Object();


    public FamilyHRateStatisticsDayLvAdapter(Context context, int offset) {
        super(context, offset);
        initXValuesTask.execute();

    }

    @Override
    protected DataHolder getDataSet(int position, String deviceId) {

        int max = 0;
        int min = Integer.MAX_VALUE;
        int average = 0;

        int total = 0;
        int rowCount = 0;

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
                Log.e(TAG, "" + xIndex);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Entry entry = new Entry(rate, xIndex);
            points.add(entry);
            if(rate > max)
                max = rate;
            if(rate < min)
                min = rate;

            rowCount++;
            total += rate;
        }

        if(rowCount != 0) {
            average = total / rowCount;
        }
        else {
            min = 0;
        }

        cursor.close();

        LineDataSet dataSet = new LineDataSet(points, "data");
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);

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

        DataHolder dataHolder = new DataHolder();
        dataHolder.average = average;
        dataHolder.max = max;
        dataHolder.min= min;
        dataHolder.lineData = lineData;

        return dataHolder;
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

    @Override
    protected View inflaterItemView(ViewHolder viewHolder) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.hrate_statistics_item_view, null);

        viewHolder.name = (TextView)view.findViewById(R.id.name_tv);
        viewHolder.longest = (TextView)view.findViewById(R.id.hrate_highest_rate_tv);
        viewHolder.average = (TextView)view.findViewById(R.id.hrate_average_rate_tv);
        viewHolder.shortest = (TextView)view.findViewById(R.id.hrate_lowest_rate_tv);
        viewHolder.lineChart = (LineChart) view.findViewById(R.id.data_lc);
        return view;
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
