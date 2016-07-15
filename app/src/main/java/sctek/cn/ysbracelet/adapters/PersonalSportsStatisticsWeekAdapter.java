package sctek.cn.ysbracelet.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

/**
 * Created by kang on 16-4-11.
 */
public class PersonalSportsStatisticsWeekAdapter extends PersonalStatisticsBaseAdapter {

    private final static String  TAG = PersonalSportsStatisticsWeekAdapter.class.getSimpleName();

    public PersonalSportsStatisticsWeekAdapter(Context context, String device) {
        super(context, device);
        mDateManager = new YsDateManager(YsDateManager.DATE_FORMAT_DAY);
    }
    @Override
    protected int getPageCount() {
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.Sports.CONTENT_URI
                , new String[]{LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME}
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_DEVICE + "=?"
                , new String[]{deviceId}
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME + " asc limit 1");
        String earliestDate = null;
        if(cursor.moveToNext()) {
            earliestDate = cursor.getString(0);
        }

        cursor.close();

        int pages = 0;
        try {
            pages = mDateManager.weeksFromNowTo(earliestDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pages;
    }

    @Override
    protected LineData getDataSet(int position) {
        String dateStart = mDateManager.getFirstDayOfWeekBy(-position);
        String dateEnd = mDateManager.getFisrtDayOfNextWeekBy(-position);//从周一凌晨到下周一凌晨
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.Sports.CONTENT_URI
                , new String[]{LocalDataContract.Sports.COLUMNS_NAME_SPORTS_WALK, LocalDataContract.Sports.COLUMNS_NAME_SPORTS_RUN
                        , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME}
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_DEVICE + "=?"
                        + " AND " + LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME + ">=" + "'" + dateStart + "'"
                        + " AND " + LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME + "<" + "'" + dateEnd + "'"
                , new String[]{deviceId}
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME + " asc");

        ArrayList<Entry> points = new ArrayList<>();
        while(cursor.moveToNext()) {
            int walk = cursor.getInt(0);
            int run = cursor.getInt(1);
            String date = cursor.getString(2);
            int xIndex = 0;
            try {
                xIndex = mDateManager.getDayOfWeekFor(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Entry entry = new Entry(walk + run, xIndex);
            points.add(entry);
        }

        cursor.close();

        LineDataSet dataSet = new LineDataSet(points, "data");
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);

        List<String> xVals = getXValues(position);
        LineData lineData = new LineData(xVals, dataSet);
        return lineData;
    }

    @Override
    protected List<String> getXValues(int position) {
        String[] values = mContext.getResources().getStringArray(R.array.weekdays);
        ArrayList<String> xVal = new ArrayList<>();
        Collections.addAll(xVal, values);
        return xVal;
    }
}
