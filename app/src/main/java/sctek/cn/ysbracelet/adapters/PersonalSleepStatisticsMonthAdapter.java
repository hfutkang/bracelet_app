package sctek.cn.ysbracelet.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

/**
 * Created by kang on 16-4-11.
 */
public class PersonalSleepStatisticsMonthAdapter extends PersonalStatisticsBaseAdapter {

    public PersonalSleepStatisticsMonthAdapter(Context context, String device) {
        super(context, device);
        mDateManager = new YsDateManager(YsDateManager.DATE_FORMAT_DAY);
    }
    @Override
    protected int getPageCount() {
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.Sleep.CONTENT_URI
                , new String[]{LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START}
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEVICE + "=?"
                , new String[]{deviceId}
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + " asc limit 1");
        String earliestDate = null;
        if(cursor.moveToNext()) {
            earliestDate = cursor.getString(0);
        }

        cursor.close();

        int pages = 0;
        try {
            pages = mDateManager.monthsFromNowTo(earliestDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pages;
    }

    @Override
    protected LineData getDataSet(int position) {
        String dateStart = mDateManager.getFirstDayOfMonthBy(-position);
        String dateEnd = mDateManager.getLastDayOfMonthBy(-position);
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.Sleep.CONTENT_URI
                , new String[]{LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_TOTALE, LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START}
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEVICE + "=?"
                        + " AND " + LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + ">=" + "'" + dateStart + "'"
                        + " AND " + LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + "<" + "'" + dateEnd + "'"
                , new String[]{deviceId}
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + " asc");

        ArrayList<Entry> points = new ArrayList<>();
        while(cursor.moveToNext()) {
            int total = cursor.getInt(0);
            String date = cursor.getString(1);
            int xIndex = 0;
            try {
                xIndex = mDateManager.getDayOfMonthFor(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Entry entry = new Entry(total, xIndex);
            points.add(entry);
        }

        cursor.close();

        LineDataSet dataSet = new LineDataSet(points, "data");
        dataSet.setDrawValues(true);
        dataSet.setDrawCircles(true);

        List<String> xVals = getXValues(position);
        LineData lineData = new LineData(xVals, dataSet);
        return lineData;
    }

    @Override
    protected List<String> getXValues(int position) {
        int max = mDateManager.getMaxDayOfMonthBy(-position);
        List<String> xVals = new ArrayList<>();
        for(int i = 1; i <= max; i++)
            xVals.add(i + "");
        return xVals;
    }
}
