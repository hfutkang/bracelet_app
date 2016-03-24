package sctek.cn.ysbracelet.activitys;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
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
import sctek.cn.ysbracelet.thread.BleDataSendThread;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.uiwidget.MonthPickerDialog;
import sctek.cn.ysbracelet.utils.UrlUtils;

public class HeartRateActivity extends AppCompatActivity implements BluetoothLeService.OnCharacteristicChangedListener
            , HttpConnectionWorker.ConnectionWorkListener{

    private final static String TAG = HeartRateActivity.class.getSimpleName();

    private final static int TIMEOUT_MEASURE_HRATE = 1001;
    private final static int SHOW_TIMER_COUNTER = 1002;

    private BluetoothLeManager mBluetoothLeManager;

    private YsDateManager dateManager;

    private String currentShowDate;
    private String currentDate;

    private DeviceInformation mDevice;

    private View actionBarV;
    private TextView titleTv;
    private ImageButton historyIb;
    private ImageButton backIb;

    private TextView timeTv;

    private TextView rateTv;
    private Button startIb;

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
        setContentView(R.layout.activity_heart_rate);

        mBluetoothLeManager = BluetoothLeManager.getInstance();
        mBluetoothLeManager.setCharacteristicListener(this);
        mDevice = mBluetoothLeManager.getBindedDevice();
        dateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SHOW2);
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

        actionBarV = findViewById(R.id.action_bar_rl);
        actionBarV.setBackgroundColor(Color.RED);

        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        historyIb = (ImageButton)findViewById(R.id.action_ib);

        titleTv.setText(R.string.heart_rate_title);
        historyIb.setOnClickListener(onViewClickedListener);


        timeTv = (TextView)findViewById(R.id.time_tv);
        dateManager.showCurrentDate(timeTv);
    }

    public void onStartMeasureClicked(View v) {
        if(!mBluetoothLeManager.isConnected()) {
            showState(R.string.ble_disconnected);
            return;
        }
        BleData data = new BleData(Commands.CMD_MEASURE_HRATE, null);
        BleDataSendThread thread = new BleDataSendThread(data, mHandler);
        thread.start();
        startIb.setEnabled(false);
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
        ;
    }

    @Override
    public void onReceiveData(BlePacket packet) {
        if(packet.cmd == Commands.CMD_MEASURE_HRATE) {
            timer.cancel();
            HeartRateData data = BleDataParser.parseHeartRateDataFrom(packet);
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
                    }
                    break;
                case TIMEOUT_MEASURE_HRATE:
                    timer.cancel();
                    showState(R.string.measure_fail);
                    break;
                case SHOW_TIMER_COUNTER:
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
                    getHeartRateRecords();
                    break;
                case R.id.date_next_ib:
                    if(currentDate.equals(currentShowDate))
                        return;
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
            getHeartRateRecords();
        }
    };

    private void getHeartRateRecords() {

        if(mDevice.getSerialNumber() == null) {
            return;
        }

        String url = UrlUtils.compositeGetHRateRecordsUrl(mDevice.getSerialNumber(),
                dateManager.getFirstDayOfCurrentMonth(), dateManager.getLastDayOfCurrentMonth());
        YsHttpConnection connection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        HttpConnectionWorker worker = new HttpConnectionWorker(connection, this);
        worker.start();

    }

    @Override
    public void onWorkDone(int resCode) {

        if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS){

        }
        else if(resCode == XmlNodes.RESPONSE_CODE_FAIL) {

        }

   }

    @Override
    public void onResult(YsData result) {
        HeartRateData data = (HeartRateData)result;
    }

    @Override
    public void onError(Exception e) {
    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nav_back_ib:

                    break;
                case R.id.action_ib:
                    startActivity(new Intent(HeartRateActivity.this, PersonalHistoryHRateActivity.class));
                    break;
                case R.id.start_measure_ib:
                    break;
            }
        }
    };
}
