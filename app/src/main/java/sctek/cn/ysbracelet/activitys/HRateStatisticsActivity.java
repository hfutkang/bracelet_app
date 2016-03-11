package sctek.cn.ysbracelet.activitys;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.Thread.BleDataSendThread;
import sctek.cn.ysbracelet.Thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.UIWidget.HRateListViewAdapter;
import sctek.cn.ysbracelet.UIWidget.MonthPickerDialog;
import sctek.cn.ysbracelet.ble.BleData;
import sctek.cn.ysbracelet.ble.BleDataParser;
import sctek.cn.ysbracelet.ble.BlePacket;
import sctek.cn.ysbracelet.ble.BluetoothLeManager;
import sctek.cn.ysbracelet.ble.BluetoothLeService;
import sctek.cn.ysbracelet.ble.Commands;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.devicedata.HeartRateData;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.http.YsHttpConnection;
import sctek.cn.ysbracelet.utils.UrlUtils;

public class HRateStatisticsActivity extends AppCompatActivity implements BluetoothLeService.OnCharacteristicChangedListener
            , HttpConnectionWorker.ConnectionWorkListener{

    private final static String TAG = HRateStatisticsActivity.class.getSimpleName();

    private final static int TIMEOUT_MEASURE_HRATE = 1001;
    private final static int SHOW_TIMER_COUNTER = 1002;

    private TextView stateTv;
    private TextView resultTv;
    private TextView measureBt;

    private ImageButton preDateIb;
    private ImageButton nextDateIb;
    private TextView dateTv;

    private ListView dataLv;
    private List<HeartRateData> dataList;
    private HRateListViewAdapter adapter;
    private TextView emptyTv;

    private BluetoothLeManager mBluetoothLeManager;

    private YsDateManager dateManager;

    private String currentShowDate;
    private String currentDate;

    private DeviceInformation mDevice;

    private Timer timer = new Timer();
    private int timerCounter = 0;

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(SHOW_TIMER_COUNTER);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrate_statistics);
        dataList = new ArrayList<>();
        mBluetoothLeManager = BluetoothLeManager.getInstance();
        mBluetoothLeManager.setCharacteristicListener(this);
        mDevice = mBluetoothLeManager.getBindedDevice();
        dateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SHOW3);
        currentDate = dateManager.getCurrentDate();

        initViewElement();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getHeartRateRecords();
            }
        });
    }

    private void initViewElement() {

        stateTv = (TextView)findViewById(R.id.state_hint_tv);
        resultTv = (TextView)findViewById(R.id.result_tv);
        measureBt = (TextView)findViewById(R.id.start_measure_bt);

        preDateIb = (ImageButton)findViewById(R.id.date_previous_ib);
        nextDateIb = (ImageButton)findViewById(R.id.date_next_ib);
        dateTv = (TextView)findViewById(R.id.date_tv);
        currentShowDate = dateManager.showCurrentDate(dateTv);

        preDateIb.setOnClickListener(onDateViewClicked);
        nextDateIb.setOnClickListener(onDateViewClicked);
        dateTv.setOnClickListener(onDateViewClicked);

        dataLv = (ListView)findViewById(R.id.history_hrate_lv);
        emptyTv = (TextView)findViewById(R.id.empty_tv);

        adapter = new HRateListViewAdapter(this, dataList);
        dataLv.setAdapter(adapter);

    }

    public void onStartMeasureClicked(View v) {
        if(!mBluetoothLeManager.isConnected()) {
            showState(R.string.ble_disconnected);
            return;
        }
        BleData data = new BleData(Commands.CMD_MEASURE_HRATE, null);
        BleDataSendThread thread = new BleDataSendThread(data, mHandler);
        thread.start();
        measureBt.setEnabled(false);
        showState(R.string.measure_in_progress);
        timerCounter = 0;
        timer.schedule(timerTask, 0, 1000);
    }

    private void showMonthPickerDialog() {

        MonthPickerDialog dialog = new MonthPickerDialog(this, onDateSetListener, dateManager.getYear(), dateManager.getMonth(), 1);
        dialog.setTitle(R.string.select_month);
        DatePicker datePicker = dialog.getDatePicker();
        datePicker.setMaxDate(System.currentTimeMillis());
        dialog.show();

    }

    private void showState(int resId) {
        stateTv.setText(resId);
    }

    @Override
    public void onReceiveData(BlePacket packet) {
        if(packet.cmd == Commands.CMD_MEASURE_HRATE) {
            timer.cancel();
            HeartRateData data = BleDataParser.parseHeartRateDataFrom(packet);
            resultTv.setText(data.rate + "");
            measureBt.setEnabled(true);
            mHandler.removeMessages(TIMEOUT_MEASURE_HRATE);
            showState(R.string.latest_heart_rate_label);
        }
    }

    @Override
    public void onReceiveRssi(int rssi) {

    }

    @Override
    public void onDataValid() {
        showState(R.string.measure_fail);
        mHandler.removeMessages(TIMEOUT_MEASURE_HRATE);
        measureBt.setEnabled(true);
        resultTv.setText("0");
        timer.cancel();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Commands.CMD_MEASURE_HRATE:
                    if(msg.arg1 == BleDataSendThread.FAIL_SEND_DATA) {
                        timer.cancel();
                        showState(R.string.measure_fail);
                        measureBt.setEnabled(true);
                        resultTv.setText("0");
                    }
                    break;
                case TIMEOUT_MEASURE_HRATE:
                    timer.cancel();
                    showState(R.string.measure_fail);
                    resultTv.setText("0");
                    measureBt.setEnabled(true);
                    break;
                case SHOW_TIMER_COUNTER:
                    resultTv.setText(timerCounter + "");
                    timerCounter++;
                    break;
            }
        }
    };

    private View.OnClickListener onDateViewClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.date_previous_ib:
                    currentShowDate = dateManager.showPreviousMonth(dateTv);
                    dataList.clear();
                    adapter.notifyDataSetChanged();
                    getHeartRateRecords();
                    break;
                case R.id.date_next_ib:
                    if(currentDate.equals(currentShowDate))
                        return;
                    dataList.clear();
                    currentShowDate = dateManager.showNextMonth(dateTv);
                    getHeartRateRecords();
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
            getHeartRateRecords();
        }
    };

    private void getHeartRateRecords() {

        if(mDevice.getSerialNumber() == null) {
            dataLv.setVisibility(View.GONE);
            emptyTv.setVisibility(View.VISIBLE);
            return;
        }

        String url = UrlUtils.compositeGetHRateRecordssUrl(mDevice.getSerialNumber(),
                dateManager.getFirstDayOfCurrentMonth(), dateManager.getLastDayOfCurrentMonth());
        YsHttpConnection connection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        HttpConnectionWorker worker = new HttpConnectionWorker(connection, this);
        worker.start();

        preDateIb.setEnabled(false);
        dateTv.setEnabled(false);
        nextDateIb.setEnabled(false);

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
        }

        adapter.notifyDataSetChanged();
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
        HeartRateData data = (HeartRateData)result;
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
}
