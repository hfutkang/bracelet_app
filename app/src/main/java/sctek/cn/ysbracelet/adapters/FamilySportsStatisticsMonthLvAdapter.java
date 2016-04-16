package sctek.cn.ysbracelet.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
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
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

/**
 * Created by kang on 16-3-24.
 */
public class FamilySportsStatisticsMonthLvAdapter extends FamilyStatisticsBaseLvAdapter{

    private static final String TAG = FamilySportsStatisticsMonthLvAdapter.class.getSimpleName();

    public FamilySportsStatisticsMonthLvAdapter(Context context, int offset) {
        super(context, offset);

    }

    @Override
    protected DataHolder getDataSet(int position, String deviceId) {

        Log.e(TAG, position + " " + deviceId);
        int max = 0;
        int min = Integer.MAX_VALUE;
        int average = 0;

        int total = 0;
        int rowCount = 0;

        String dateStart = mDateManager.getFirstDayOfMonthBy(-position);
        String dateEnd = mDateManager.getLastDayOfMonthBy(-position);
        Log.e(TAG, dateStart + " " + dateEnd);
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.Sports.CONTENT_URI
                , new String[]{LocalDataContract.Sports.COLUMNS_NAME_SPORTS_WALK, LocalDataContract.Sports.COLUMNS_NAME_SPORTS_RUN
                        , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME}
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_DEVICE + "=?"
                        + " AND " + LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME + ">" + "'" + dateStart + "'"
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
                xIndex = mDateManager.getDayOfMonthFor(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Entry entry = new Entry(walk + run, xIndex);
            points.add(entry);

            if(walk + run > max)
                max = walk + run;
            if(walk + run < min)
                min = walk + run;

            rowCount++;
            total += walk + run;
        }

        cursor.close();

        if(rowCount != 0) {
            average = total / rowCount;
        }
        else {
            min = 0;
        }

        LineDataSet dataSet = new LineDataSet(points, "data");
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);

        List<String> xVals = getXValues(position);
        LineData lineData = new LineData(xVals, dataSet);

        DataHolder dataHodler = new DataHolder();
        dataHodler.average = average;
        dataHodler.max = max;
        dataHodler.min= min;
        dataHodler.lineData = lineData;

        return dataHodler;
    }

    @Override
    protected List<String> getXValues(int position) {
        int max = mDateManager.getMaxDayOfMonthBy(-position);
        List<String> xVals = new ArrayList<>();
        for(int i = 1; i <= max; i++)
            xVals.add(i + "");
        return xVals;
    }

    @Override
    protected View inflaterItemView(ViewHolder viewHolder) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sports_statistics_item_view, null);
        viewHolder.name = (TextView)view.findViewById(R.id.name_tv);
        viewHolder.longest = (TextView)view.findViewById(R.id.sports_most_steps_tv);
        viewHolder.shortest = (TextView)view.findViewById(R.id.sports_least_steps_tv);
        viewHolder.average = (TextView)view.findViewById(R.id.sports_average_steps_tv);
        viewHolder.lineChart = (LineChart) view.findViewById(R.id.data_lc);
        return view;
    }

}
