package sctek.cn.ysbracelet.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

import java.util.List;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.user.YsUser;

/**
 * Created by kang on 16-4-13.
 */
public abstract class FamilyStatisticsBaseLvAdapter extends BaseAdapter{

    private final static String TAG = FamilyStatisticsBaseLvAdapter.class.getSimpleName();

    protected Context mContext;
    protected List<DeviceInformation> mDevices;

    protected int pageOffset;

    protected YsDateManager mDateManager;

    public FamilyStatisticsBaseLvAdapter(Context context, int offset) {
        mContext = context;
        mDevices = YsUser.getInstance().getDevices();
        mDateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SECOND);
        pageOffset = offset;
    }

    protected abstract DataHolder getDataSet(int position, String deviceId);
    protected abstract List<String> getXValues(int position);
    protected abstract View inflaterItemView(ViewHolder viewHolder);

    @Override
    public int getCount() {
        return mDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {

            viewHolder = new ViewHolder();
            convertView = inflaterItemView(viewHolder);

            convertView.setTag(viewHolder);
        } {
            viewHolder = (ViewHolder)convertView.getTag();
        }

       new LoadDataAsyncTask(viewHolder, mContext).execute(pageOffset + "", mDevices.get(position).serialNumber);

        viewHolder.lineChart.clear();

        viewHolder.name.setText(mDevices.get(position).name);
        initLineChart(viewHolder.lineChart);

        return convertView;
    }

    public class ViewHolder {
        public TextView name;
        public TextView longest;
        public TextView shortest;
        public TextView average;

        public LineChart lineChart;
    }

    public class DataHolder {
        int max;
        int min;
        int average;
        LineData lineData;
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

    private class LoadDataAsyncTask extends AsyncTask<String, Void, DataHolder> {

        ViewHolder viewHolder;
        ProgressDialog progressDialog;

        public LoadDataAsyncTask(ViewHolder holder, Context context) {
            progressDialog = new ProgressDialog(context);
            String msg = context.getString(R.string.loading_data);
            progressDialog.setMessage(msg);
            viewHolder = holder;
        }
        @Override
        protected DataHolder doInBackground(String... params) {
            int postion = Integer.parseInt(params[0]);
            DataHolder holder = getDataSet(postion, params[1]);
            return holder;
        }

        @Override
        protected void onPreExecute() {
//            progressDialog.show();
        }

        @Override
        protected void onPostExecute(DataHolder dataHolder) {
            if(BleUtils.DEBUG) Log.e(TAG, "onPostExecute");
            viewHolder.lineChart.clear();
            viewHolder.lineChart.setData(dataHolder.lineData);
            viewHolder.lineChart.invalidate();

            viewHolder.average.setText(dataHolder.average + "");
            viewHolder.shortest.setText(dataHolder.min + "");
            viewHolder.longest.setText(dataHolder.max + "");
            progressDialog = null;
        }

    }

}
