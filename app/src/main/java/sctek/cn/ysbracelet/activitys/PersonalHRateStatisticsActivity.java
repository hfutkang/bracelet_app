package sctek.cn.ysbracelet.activitys;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.ChartViewPagerAdapter;
import sctek.cn.ysbracelet.devicedata.SportsData;

public class PersonalHRateStatisticsActivity extends AppCompatActivity {

    private final static String TAG = PersonalHRateStatisticsActivity.class.getSimpleName();

    private BarChart mBarChart;
    private LineChart mLineChart;
    private ViewPager mViewPager;

    private YsDateManager dateManager;
    private List<SportsData> dataList;

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton actionIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_hrate_statistics);

        dateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SHOW3);

        initViewElement();
//        showDataWithCharBar();
//        showDataWithLineChart();
    }

    private void initBarChart() {

        mBarChart = (BarChart) findViewById(R.id.data_bc);
        mBarChart.setDrawGridBackground(false);
        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        Legend l = mBarChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
//        l.setTypeface(tf);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);
        l.setEnabled(false);

        XAxis xl = mBarChart.getXAxis();
        xl.setTypeface(tf);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawGridLines(false);

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        leftAxis.setLabelCount(3, true);
        leftAxis.setDrawTopYLabelEntry(false);

        mBarChart.getAxisRight().setEnabled(false);
        mBarChart.setBackgroundColor(Color.WHITE);
        mBarChart.setScaleEnabled(true);

    }

    private void initLineChart() {

        mLineChart = (LineChart)findViewById(R.id.data_lc);
        mLineChart.setDrawGridBackground(false);

        Legend l = mLineChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);

        XAxis lxAxis = mLineChart.getXAxis();
        lxAxis.setDrawGridLines(false);
        lxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lxAxis.setSpaceBetweenLabels(1);

        YAxis lyAxis = mLineChart.getAxisLeft();
        lyAxis.setDrawGridLines(false);
        lyAxis.setDrawLabels(false);

        LimitLine limitLine = new LimitLine(1500.f, "Goal");
        limitLine.setTextSize(10f);
        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        lyAxis.addLimitLine(limitLine);

        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.setDescription("");
    }

    private void initViewElement() {

//        initLineChart();

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        actionIb = (ImageButton)findViewById(R.id.action_ib);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);

        actionBarV.setBackgroundColor(Color.RED);
        titleTv.setText(R.string.heart_rate_title);
        actionIb.setVisibility(View.GONE);

        mViewPager = (ViewPager)findViewById(R.id.chart_vp);
        ChartViewPagerAdapter adapter = new ChartViewPagerAdapter(this, 160, 1);
        mViewPager.setAdapter(adapter);

    }

    private void showDataWithCharBar() {

        Random random = new Random(1234567);
        random.nextInt(2000);
        ArrayList<SportsData> datas = new ArrayList<>();
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160301"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160302"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160303"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160304"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160305"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160306"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160307"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160308"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160309"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160310"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160311"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160312"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160313"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160314"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160315"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160316"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160317"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160318"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160319"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160320"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160321"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160322"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160323"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160324"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160325"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160326"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160327"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160328"));

        ArrayList<String> xValues = new ArrayList<>();
        int xEntries = dateManager.getActualMaximumOfCurrentMonth();
        for(int i = 1; i <= xEntries; i++) {
            xValues.add(i + "");
        }

        ArrayList<BarEntry> runBars = new ArrayList<>();

        Iterator<SportsData> iterator = datas.iterator();
        while (iterator.hasNext()) {
            SportsData data = iterator.next();
            dateManager.setDate(data.date);
            runBars.add(new BarEntry(data.runSteps, dateManager.getDayOfMonth()));
        }

        BarDataSet runSet = new BarDataSet(runBars, "rate");
        runSet.setDrawValues(false);
        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(runSet);

        BarData barData = new BarData(xValues, dataSets);

        mBarChart.setData(barData);
        mBarChart.invalidate();

    }

    private void showDataWithLineChart() {

        Random random = new Random(1234567);
        random.nextInt(2000);
        ArrayList<SportsData> datas = new ArrayList<>();
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160301002100"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160302013500"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160303013700"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160304015400"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160305024500"));
//        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160306024600"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160307031500"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160308034300"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160309035600"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160310040100"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160311042200"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160312043100"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160313051200"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160314052600"));
//        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160315054300"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160316063300"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160317081000"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160318095100"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160319103200"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160320115800"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160321124300"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160322132900"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160323143600"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160324190200"));
//        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160325201000"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160326210400"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160327223000"));
        datas.add(new SportsData(random.nextInt(2000), random.nextInt(2000), "20160328232200"));

        ArrayList<String> xValues = new ArrayList<>();
        int xEntries = dateManager.getActualMaximumOfCurrentMonth();
        for(int i = 1; i <= 1440; i++) {
            int hour = i/60;
            int minutes = i%60;
            String h = hour > 9?"" + hour:"0" + hour;
            String m = minutes > 9?"" + minutes:"0" + minutes;
            xValues.add(h + ":" + m);
        }

        ArrayList<Entry> runBars = new ArrayList<>();

        Iterator<SportsData> iterator = datas.iterator();
        while (iterator.hasNext()) {
            SportsData data = iterator.next();
            dateManager.setDate(data.date);
            runBars.add(new Entry(data.runSteps, dateManager.getMinuteOfDay()));
        }

        LineDataSet runSet = new LineDataSet(runBars, "Heart rate");
        runSet.setDrawValues(false);
        runSet.setDrawCircles(false);
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(runSet);

        LineData lineData = new LineData(xValues, dataSets);


        mLineChart.setData(lineData);
        mLineChart.invalidate();
    }
}
