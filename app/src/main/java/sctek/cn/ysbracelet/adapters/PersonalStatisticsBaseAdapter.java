package sctek.cn.ysbracelet.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

import java.util.List;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.ble.BleUtils;

/**
 * Created by kang on 16-4-11.
 */
public abstract class PersonalStatisticsBaseAdapter extends PagerAdapter {

    private final static String TAG = PersonalStatisticsBaseAdapter.class.getSimpleName();

    protected int goal;
    protected int max;
    protected int min;

    protected Context mContext;
    protected YsDateManager mDateManager;

    protected String deviceId;

    public PersonalStatisticsBaseAdapter(Context context, String device) {
        mContext = context;
        deviceId = device;
    }

    @Override
    public int getCount() {
        return getPageCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.e(TAG, "instantianteItem:" + position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.chart_viewpager_item_view, null);
        LineChart lineChart = (LineChart)view.findViewById(R.id.data_lc);

        new LoadDataAsyncTask(lineChart, mContext).execute(position);
//        addLimitLine(lineChart);
        initLineChart(lineChart);
        container.addView(view);
        return view;
    }

    private void initLineChart(LineChart lineChart) {

        lineChart.setDrawGridBackground(false);

        Legend l = lineChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        XAxis lxAxis = lineChart.getXAxis();
        lxAxis.setDrawGridLines(false);
        lxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lxAxis.setAvoidFirstLastClipping(true);

        YAxis lyAxis = lineChart.getAxisLeft();
        lyAxis.setDrawGridLines(false);
        lyAxis.setDrawLabels(false);

        YAxis ryAxis = lineChart.getAxisRight();
        ryAxis.setDrawGridLines(false);
        ryAxis.setDrawLabels(false);

        lineChart.setDescription("");
        String emptyMsg = mContext.getString(R.string.empty_data);
        lineChart.setNoDataText(emptyMsg);

    }

    private void addLimitLine(LineChart lineChart) {
        YAxis ya = lineChart.getAxisLeft();
        ya.addLimitLine(newLimitLine(max, "Max"));
        ya.addLimitLine(newLimitLine(min, "Min"));
        ya.addLimitLine(newLimitLine(goal, "Goal"));
    }

    private LimitLine newLimitLine(int value, String label) {
        LimitLine ll = new LimitLine(value, label + "/" + value);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll.setTextSize(10.f);
        ll.setLineColor(Color.RED);
        return ll;
    }

    protected abstract int getPageCount();

    protected abstract LineData getDataSet(int position);

    protected abstract List<String> getXValues(int position);

    private class LoadDataAsyncTask extends AsyncTask<Integer, Void, LineData> {

        LineChart lineChart;
        ProgressDialog progressDialog;

        public LoadDataAsyncTask(LineChart lc, Context context) {
            progressDialog = new ProgressDialog(context);
            String msg = context.getString(R.string.loading_data);
            progressDialog.setMessage(msg);
            lineChart = lc;
        }
        @Override
        protected LineData doInBackground(Integer... params) {
            LineData lineData = getDataSet(params[0]);
            return lineData;
        }

        @Override
        protected void onPreExecute() {
//            progressDialog.show();
        }

        @Override
        protected void onPostExecute(LineData lineData) {
            if(BleUtils.DEBUG) Log.e(TAG, "onPostExecute");
            lineChart.setData(lineData);
            lineChart.invalidate();
            lineChart = null;
            progressDialog = null;
        }

    }

}
