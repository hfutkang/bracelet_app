package sctek.cn.ysbracelet.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
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
public class FamilySleepStatisticsMonthLvAdapter extends FamilyStatisticsBaseLvAdapter{

    private static final String TAG = FamilySleepStatisticsMonthLvAdapter.class.getSimpleName();

    public FamilySleepStatisticsMonthLvAdapter(Context context, int offset) {
        super(context, offset);

    }

    @Override
    protected DataHolder getDataSet(int position, String deviceId) {

        int max = 0;
        int min = Integer.MAX_VALUE;
        int average = 0;

        int total1 = 0;
        int rowCount = 0;

        String dateStart = mDateManager.getFirstDayOfMonthBy(-position);
        String dateEnd = mDateManager.getLastDayOfMonthBy(-position);
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.Sleep.CONTENT_URI
                , new String[]{LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_TOTALE, LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START}
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEVICE + "=?"
                        + " AND " + LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + ">" + "'" + dateStart + "'"
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

            if(total > max)
                max = total;
            if(total < min)
                min = total;

            rowCount++;
            total1 += total;
        }

        cursor.close();

        if(rowCount != 0) {
            average = total1 / rowCount;
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
        View view =LayoutInflater.from(mContext).inflate(R.layout.sleep_statistics_item_view, null);
        viewHolder.name = (TextView)view.findViewById(R.id.name_tv);
        viewHolder.longest = (TextView)view.findViewById(R.id.sleep_longest_time_tv);
        viewHolder.shortest = (TextView)view.findViewById(R.id.sleep_shortest_time_tv);
        viewHolder.average = (TextView)view.findViewById(R.id.sleep_average_time_tv);
        viewHolder.lineChart = (LineChart) view.findViewById(R.id.data_lc);
        return view;
    }

}
