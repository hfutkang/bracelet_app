package sctek.cn.ysbracelet.activitys;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.SportsListViewAdapter;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.devicedata.SportsData;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.http.YsHttpConnection;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.uiwidget.MonthPickerDialog;
import sctek.cn.ysbracelet.utils.UrlUtils;

public class SportsStatisticsActivity extends AppCompatActivity implements HttpConnectionWorker.ConnectionWorkListener{

    private final static String TAG = SportsStatisticsActivity.class.getSimpleName();

    private BarChart mBarChart;

    private ImageButton preDateIb;
    private ImageButton nextDateIb;
    private TextView dateTv;

    private TextView emptyTv;
    private ListView dataLv;

    private List<SportsData> dataList;

    private YsDateManager dateManager;

    private String currentShowDate;
    private String currentDate;

    private DeviceInformation mDevice;

    private SportsListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_statistics);
        dataList = new ArrayList<>();
        dateManager = new YsDateManager(YsDateManager.DATE_FORMAT_MONTH);
        currentDate = dateManager.getCurrentDate();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getSportRecords();
            }
        });

        initViewElement();
        showDataWithCharBar();
    }

    private void initViewElement() {

        mBarChart = (BarChart) findViewById(R.id.data_bc);

        preDateIb = (ImageButton)findViewById(R.id.date_previous_ib);
        nextDateIb = (ImageButton)findViewById(R.id.date_next_ib);
        dateTv = (TextView)findViewById(R.id.date_tv);
        currentShowDate = dateManager.showCurrentDate(dateTv);

        preDateIb.setOnClickListener(onDateViewClicked);
        nextDateIb.setOnClickListener(onDateViewClicked);
        dateTv.setOnClickListener(onDateViewClicked);

        emptyTv = (TextView)findViewById(R.id.empty_tv);
        dataLv = (ListView)findViewById(R.id.history_sports_lv);

        adapter = new SportsListViewAdapter(this, dataList);
        dataLv.setAdapter(adapter);

    }

    private void getSportRecords() {

        if(mDevice.getSerialNumber() == null) {
            dataLv.setVisibility(View.GONE);
            emptyTv.setVisibility(View.VISIBLE);
            return;
        }

        String url = UrlUtils.compositeGetSportsRecordsUrl(mDevice.getSerialNumber(),
                dateManager.getFirstDayOfCurrentMonth(), dateManager.getLastDayOfCurrentMonth());
        YsHttpConnection connection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        HttpConnectionWorker worker = new HttpConnectionWorker(connection, this);
        worker.start();

        preDateIb.setEnabled(false);
        dateTv.setEnabled(false);
        nextDateIb.setEnabled(false);

    }

    private void showMonthPickerDialog() {

        MonthPickerDialog dialog = new MonthPickerDialog(this, onDateSetListener, dateManager.getYear(), dateManager.getMonth(), 1);
        dialog.setTitle(R.string.select_month);
        DatePicker datePicker = dialog.getDatePicker();
        datePicker.setMaxDate(System.currentTimeMillis());
        dialog.show();

    }

    @Override
    public void onWorkDone(int resCode) {

        if(dataList.size() == 0) {
            emptyTv.setVisibility(View.VISIBLE);
            dataLv.setVisibility(View.GONE);
        }
        else {
            emptyTv.setVisibility(View.GONE);
            dataLv.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            showDataWithCharBar();
        }

        preDateIb.setEnabled(true);
        dateTv.setEnabled(true);
        nextDateIb.setEnabled(true);

        if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS){

        }
        else if(resCode == XmlNodes.RESPONSE_CODE_FAIL) {

        }

    }

    @Override
    public void onResult(YsData result) {
        SportsData data = (SportsData) result;
        dataList.add(data);
    }

    @Override
    public void onError(Exception e) {
        preDateIb.setEnabled(true);
        dateTv.setEnabled(true);
        nextDateIb.setEnabled(true);
        if(dataList.size() == 0) {
            emptyTv.setVisibility(View.VISIBLE);
            dataLv.setVisibility(View.GONE);
        }
        else {
            emptyTv.setVisibility(View.GONE);
            dataLv.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    private View.OnClickListener onDateViewClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.date_previous_ib:
                    currentShowDate = dateManager.showPreviousMonth(dateTv);
                    dataList.clear();
                    adapter.notifyDataSetChanged();
                    getSportRecords();
                    break;
                case R.id.date_next_ib:
                    if(currentDate.equals(currentShowDate))
                        return;
                    dataList.clear();
                    currentShowDate = dateManager.showNextMonth(dateTv);
                    getSportRecords();
                    break;
                case R.id.date_tv:
                    showMonthPickerDialog();
                    break;
            }
        }
    };

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if(year == dateManager.getYear() && monthOfYear == dateManager.getMonth())
                return;
            dateManager.setDate(year, monthOfYear, dayOfMonth);
            currentShowDate = dateManager.showCurrentDate(dateTv);
            dataList.clear();
            getSportRecords();
        }
    };

    private void showDataWithCharBar() {

        ArrayList<SportsData> datas = new ArrayList<>();
        datas.add(new SportsData(1000, 544, "20160301"));
        datas.add(new SportsData(2123, 324, "20160302"));
        datas.add(new SportsData(43423, 3242, "20160303"));
        datas.add(new SportsData(2454, 3422, "20160304"));
        datas.add(new SportsData(12, 3453, "20160305"));
        datas.add(new SportsData(231, 456, "20160306"));
        datas.add(new SportsData(8977, 3445, "20160307"));
        datas.add(new SportsData(23324, 76, "20160308"));
        datas.add(new SportsData(44, 4, "20160309"));
        datas.add(new SportsData(445, 544, "20160310"));
        datas.add(new SportsData(1897, 564, "20160311"));
        datas.add(new SportsData(4566, 976, "20160312"));
        datas.add(new SportsData(3423, 5644, "20160313"));
        datas.add(new SportsData(54545, 656, "20160314"));
        datas.add(new SportsData(1111, 222, "20160315"));
        datas.add(new SportsData(332, 533, "20160316"));
        datas.add(new SportsData(1757, 3454, "20160317"));
        datas.add(new SportsData(1223, 6545, "20160318"));
        datas.add(new SportsData(8678, 5667, "20160319"));
        datas.add(new SportsData(767, 456, "20160320"));
        datas.add(new SportsData(8788, 345, "20160321"));
        datas.add(new SportsData(6745, 2323, "20160322"));
        datas.add(new SportsData(4454, 4334, "20160323"));
        datas.add(new SportsData(6784, 1975, "20160324"));
        datas.add(new SportsData(1233, 4367, "20160325"));
        datas.add(new SportsData(455, 544, "20160326"));
        datas.add(new SportsData(112, 445, "20160327"));
        datas.add(new SportsData(6777, 666, "20160328"));

        ArrayList<String> xValues = new ArrayList<>();
        int xEntries = dateManager.getActualMaximumOfCurrentMonth();
        for(int i = 1; i <= xEntries; i++) {
            xValues.add(i + "");
        }

        ArrayList<BarEntry> runBars = new ArrayList<>();
        ArrayList<BarEntry> walkBars = new ArrayList<>();

        Iterator<SportsData> iterator = datas.iterator();
        while (iterator.hasNext()) {
            SportsData data = iterator.next();
            dateManager.setDate(data.date);
            runBars.add(new BarEntry(data.runSteps, dateManager.getDayOfMonth()));
            walkBars.add(new BarEntry(data.walkSteps, dateManager.getDayOfMonth()));
        }

        BarDataSet runSet = new BarDataSet(runBars, "run");
        BarDataSet walkSet = new BarDataSet(walkBars, "walk");

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(runSet);
        dataSets.add(walkSet);

        BarData barData = new BarData(xValues, dataSets);

        mBarChart.setData(barData);
        mBarChart.invalidate();



    }
}
