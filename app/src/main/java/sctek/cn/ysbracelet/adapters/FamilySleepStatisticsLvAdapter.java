package sctek.cn.ysbracelet.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.devicedata.SportsData;

/**
 * Created by kang on 16-3-24.
 */
public class FamilySleepStatisticsLvAdapter extends BaseAdapter{

    private static final String TAG = FamilySleepStatisticsLvAdapter.class.getSimpleName();

    private Context mContext;
    private List<Object> members;

    private YsDateManager dateManager;

    private List<LimitLine> limitLines;

    int lineNumber;

    int max = 0;
    int min = 2000;
    int seed = 0;

    int randomdigital = 0;

    private int[] colors = new int[]{Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN};



    public FamilySleepStatisticsLvAdapter(Context context, List<Object> m, int r, int l) {
        mContext = context;
        members = new ArrayList<>();
        members.add(new Object());
        members.add(new Object());
        members.add(new Object());
        members.add(new Object());
        members.add(new Object());

        dateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SHOW3);
        randomdigital = r;
        lineNumber = l;
        limitLines = new ArrayList<>();

    }
    @Override
    public int getCount() {
        return members.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sleep_statistics_item_view, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)convertView.findViewById(R.id.name_tv);
            viewHolder.longest = (TextView)convertView.findViewById(R.id.sleep_longest_time_tv);
            viewHolder.shortest = (TextView)convertView.findViewById(R.id.sleep_shortest_time_tv);
            viewHolder.average = (TextView)convertView.findViewById(R.id.sleep_average_time_tv);
            viewHolder.lineChart = (LineChart) convertView.findViewById(R.id.data_lc);

            convertView.setTag(viewHolder);
        } {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.lineChart.clear();
        viewHolder.lineChart.setData(getData());
        addLimitLine(viewHolder.lineChart);
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

    private void addLimitLine(LineChart lineChart) {
        YAxis ya = lineChart.getAxisLeft();
        ya.removeAllLimitLines();
        for(LimitLine ll : limitLines) {
            ya.addLimitLine(ll);
        }
    }

    private LineData getData() {

        limitLines.clear();

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        for(int i = 0; i < lineNumber; i++) {
            LineDataSet runSet = getDataSet(i);
            runSet.setColor(colors[i]);
            dataSets.add(runSet);
        }

        ArrayList<String> xValues = new ArrayList<>();
        int xEntries = dateManager.getActualMaximumOfCurrentMonth();
        for(int i = 1; i <= xEntries; i++) {
            xValues.add(i + "");
        }

        LineData lineData = new LineData(xValues, dataSets);

        return lineData;
    }

    private LineDataSet getDataSet(int i) {

        max = 0;
        min = randomdigital;

        Random random = new Random();

        ArrayList<SportsData> datas = new ArrayList<>();

        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160301002100"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160302013500"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160303013700"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160304015400"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160305024500"));
//        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160306024600"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160307031500"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160308034300"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160309035600"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160310040100"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160311042200"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160312043100"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160313051200"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160314052600"));
//        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160315054300"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160316063300"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160317081000"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160318095100"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160319103200"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160320115800"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160321124300"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160322132900"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160323143600"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160324190200"));
//        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160325201000"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160326210400"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160327223000"));
        datas.add(new SportsData(getInt(random), random.nextInt(2000), "20160328232200"));

        ArrayList<Entry> runBars = new ArrayList<>();

        Iterator<SportsData> iterator = datas.iterator();
        while (iterator.hasNext()) {
            SportsData data = iterator.next();
            dateManager.setDate(data.date);
            runBars.add(new Entry(data.runSteps, dateManager.getDayOfMonth()));
        }

        LineDataSet runSet = new LineDataSet(runBars, "data");
        runSet.setDrawValues(false);
        runSet.setDrawCircles(false);

        LimitLine maxl = new LimitLine(max, "Max/" + max);
        maxl.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        maxl.setTextSize(10.f);
        maxl.setLineColor(colors[i]);

        LimitLine minl = new LimitLine(min, "min/" + min);
        minl.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        minl.setTextSize(10.f);
        minl.setLineColor(colors[i]);

        limitLines.add(maxl);
        limitLines.add(minl);

        return runSet;

    }

    private void initLineChart(LineChart lineChart) {

        lineChart.setDrawGridBackground(false);

        Legend l = lineChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        XAxis lxAxis = lineChart.getXAxis();
        lxAxis.setDrawGridLines(false);
        lxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lxAxis.setSpaceBetweenLabels(1);

        YAxis lyAxis = lineChart.getAxisLeft();
        lyAxis.setDrawGridLines(false);
        lyAxis.setDrawLabels(false);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDescription("");

    }

    private int getInt(Random random) {
        int temp = 0;
        random.setSeed(seed++);
        temp = random.nextInt(randomdigital);
        if (temp > max)
            max =temp;
        if(min > temp)
            min = temp;
        return temp;
    }
}
