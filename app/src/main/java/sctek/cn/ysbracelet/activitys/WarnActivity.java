package sctek.cn.ysbracelet.activitys;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.AlarmsLvAdapter;
import sctek.cn.ysbracelet.ble.BleData;
import sctek.cn.ysbracelet.ble.BleDataParser;
import sctek.cn.ysbracelet.ble.BlePacket;
import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.ble.BluetoothLeManager;
import sctek.cn.ysbracelet.ble.BluetoothLeService;
import sctek.cn.ysbracelet.ble.Commands;
import sctek.cn.ysbracelet.ble.MyBluetoothGattCallBack;
import sctek.cn.ysbracelet.devicedata.YsTimer;
import sctek.cn.ysbracelet.fragments.HomeFragment;
import sctek.cn.ysbracelet.thread.BleDataSendThread;
import sctek.cn.ysbracelet.user.YsUser;
import sctek.cn.ysbracelet.utils.DialogUtils;

public class WarnActivity extends AppCompatActivity implements MyBluetoothGattCallBack.OnCharacteristicChangedListener{

    private final static String TAG = WarnActivity.class.getSimpleName();

    private View clikableV;

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton actionIb;

    private Switch callReminderSw;
    private Switch alarmSw;

    private ListView alarmsLv;
    private ImageButton addIb;

    private List<YsTimer> mYsTimers;
    private AlarmsLvAdapter mAlarmsLvAdapter;

    private int selectedTimerIndex;

    private String deviceId;
    private String mac;

    private BluetoothLeService mBluetoothLeService;

    private ProgressDialog mProgressDialog;

    private final static int GET_WARN_LIST_TIME_OUT = 1;
    private final static int SEND_CONTROL_CMD_TIME_OUT = 2;
    private final static int RECEIVED_BLE_DATA = 3;
    private final static int BLE_INVALID = 4;
    private final static int BLE_ERROR = 5;
    private final static int GET_NEW_TIMER = 6;
    private final static int WARN_CONTROL_FAIL = 7;
    public final static int ON_TIMER_SWITCH_CHANGED = 8;
    public final static int ON_DELETE_BUTTON_CLICKED = 9;
    public final static int ON_RECEIVE_CHECK_PACKET = 10;

    public final static int ADD_TIMER_REQUEST = 2;
    public final static int EDITOR_TIMER_REQUEST = 3;

    public final static int WARN_CONTROL_ID_ADD = 1;
    public final static int WARN_CONTROL_ID_DEL = 2;
    public final static int WARN_CONTRL_ID_MODI = 3;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        setContentView(R.layout.activity_warn);

        initViewElement();

        initState();

    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        MyBluetoothGattCallBack.getInstance().setBleListener(null);
        mYsTimers.clear();
        try {
            unregisterReceiver(mBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBroadcastReceiver = null;
        BluetoothLeManager.disconnect(mac);
        System.gc();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult " + requestCode + " " + resultCode);
        if(requestCode == SearchDeviceActivity.REQUEST_ENABLE_BLUETOOTH) {
            if(resultCode == RESULT_OK) {
                IntentFilter filter = new IntentFilter(BluetoothLeService.ACTION_CONNECTED);
                registerReceiver(mBroadcastReceiver, filter);

                showProgressDialog();
                mHandler.sendEmptyMessageDelayed(GET_WARN_LIST_TIME_OUT, 10000);
                BluetoothLeManager.connect(WarnActivity.this, mac);
            }
        }
        else if(requestCode == ADD_TIMER_REQUEST && resultCode == RESULT_OK) {
            int hour = data.getIntExtra("hour", 0);
            int minutes = data.getIntExtra("min", 0);
            int mode = data.getIntExtra("mode", 0);
            String description = data.getStringExtra("des");
            addModifyWarn(WARN_CONTROL_ID_ADD, selectedTimerIndex, true, hour, minutes, mode, description);
        }
        else if(requestCode == EDITOR_TIMER_REQUEST && resultCode == RESULT_OK) {
            YsTimer timer = mYsTimers.get(selectedTimerIndex);
            YsTimer newTimer = new YsTimer();
            newTimer.hour = data.getIntExtra("hour", 0);
            newTimer.minutes = data.getIntExtra("min", 0);
            newTimer.mode = data.getIntExtra("mode", 0);
            String description = data.getStringExtra("des");
            newTimer.id = timer.id;
            newTimer.status = timer.status;
            newTimer.description = description;
            Log.e(TAG, timer.hour + " " + timer.minutes + " " + timer.mode + " " + timer.id + " " + timer.status + " " + timer.description);
            Log.e(TAG, newTimer.hour + " " + newTimer.minutes + " " + newTimer.mode + " " + newTimer.id + " " + newTimer.status + newTimer.description);
            if(newTimer.equals(timer))
                return;
            addModifyWarn(WARN_CONTRL_ID_MODI, selectedTimerIndex, timer.status, newTimer.hour, newTimer.minutes, newTimer.mode, description);
        }
    }

    private void initState() {

        if(!BleUtils.isBluetoothEnabled(this)) {
            showTurnBluetoothOnDialog();
        }
        else {
            IntentFilter filter = new IntentFilter(BluetoothLeService.ACTION_CONNECTED);
            registerReceiver(mBroadcastReceiver, filter);

            showProgressDialog();
            mHandler.sendEmptyMessageDelayed(GET_WARN_LIST_TIME_OUT, 10000);

//            Intent intent = new Intent(this, BluetoothLeService.class);
//            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            MyBluetoothGattCallBack.getInstance().setBleListener(WarnActivity.this);

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(BleUtils.isConnected(WarnActivity.this, mac)) {
                        getWarnList();
                    }
                    else {
                        BluetoothLeManager.connect(WarnActivity.this, mac);
                    }
                }
            });
        }
    }

    private void initViewElement() {

        mHandler = new MyHandler(this);
        Log.e(TAG, mHandler + "");

        deviceId = getIntent().getStringExtra(HomeFragment.EXTR_DEVICE_ID);
        mac = YsUser.getInstance().getDevice(deviceId).getMac();

        mYsTimers = new ArrayList<>();

        clikableV = findViewById(R.id.clickable_v);

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        actionBarV.setBackgroundColor(getResources().getColor(R.color.slateblue));
        titleTv.setText(R.string.warning_title);
        backIb.setOnClickListener(onViewClickedListener);
        actionIb.setVisibility(View.GONE);

        callReminderSw = (Switch)findViewById(R.id.call_reminder_sw);
        alarmSw = (Switch)findViewById(R.id.alarm_sw);

        callReminderSw.setOnCheckedChangeListener(onCheckedChangeListener);
        alarmSw.setOnCheckedChangeListener(onCheckedChangeListener);

        alarmsLv = (ListView)findViewById(R.id.alarms_lv);
        addIb = (ImageButton)findViewById(R.id.add_alarm_ib);

        addIb.setOnClickListener(onViewClickedListener);
        mAlarmsLvAdapter = new AlarmsLvAdapter(this, mYsTimers, mHandler);
        alarmsLv.setAdapter(mAlarmsLvAdapter);
        alarmsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                YsTimer timer = mYsTimers.get(position);
                Intent intent = new Intent(WarnActivity.this, AlarmEditorActivity.class);
                intent.putExtra("hour", timer.hour);
                intent.putExtra("min", timer.minutes);
                intent.putExtra("des", timer.description);
                intent.putExtra("mode", timer.mode);
                intent.putExtra("req", EDITOR_TIMER_REQUEST);
                Log.e(TAG, timer.hour + " " + timer.minutes + " " + timer.description + " " + timer.mode);
                selectedTimerIndex = position;
                startActivityForResult(intent, EDITOR_TIMER_REQUEST);
            }
        });

//        alarmsLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                selectedTimerIndex = position;
//                showDeleteTimerDialog();
//                return true;
//            }
//        });
    }

    private void showTurnBluetoothOnDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.turn_on_bluetooth_title);
        builder.setMessage(R.string.turn_on_bluetooth_message);
        builder.setPositiveButton(R.string.turn_on_now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, SearchDeviceActivity.REQUEST_ENABLE_BLUETOOTH);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothLeService.ACTION_CONNECTED.equals(intent.getAction())) {
                String m = intent.getStringExtra("mac");
                if(m.equals(mac)) {
                    getWarnList();
                }
            }
        }
    };

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nav_back_ib:
                    onBackPressed();
                    break;
                case R.id.alarms_rl:
                    break;
                case R.id.add_alarm_ib:
                    Intent intent = new Intent(WarnActivity.this, AlarmEditorActivity.class);
                    intent.putExtra("req", ADD_TIMER_REQUEST);
                    startActivityForResult(new Intent(WarnActivity.this, AlarmEditorActivity.class), ADD_TIMER_REQUEST);
                    break;
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.call_reminder_sw:
                    break;
                case R.id.alarm_sw:
                    switchWarn(isChecked);
                    break;
            }
        }
    };

    private void switchWarn(boolean on) {
        if(!BleUtils.isConnected(this, mac)) {
            DialogUtils.makeToast(WarnActivity.this, R.string.ble_disconnected);
            return;
        }
        BleData data;
        if(on)
            data = new BleData(Commands.CMD_SWITHC_WARN, new byte[]{0x01}, mac);
        else
            data = new BleData(Commands.CMD_SWITHC_WARN, new byte[]{0x00}, mac);
        BleDataSendThread dataSendThread = new BleDataSendThread(data, mHandler, null);
        dataSendThread.start();
    }

    private void getWarnList() {
        if(!BleUtils.isConnected(this, mac)) {
            DialogUtils.makeToast(WarnActivity.this, R.string.ble_disconnected);
            return;
        }
        BleData data = new BleData(Commands.CMD_GET_WARN_LIST, new byte[]{0x00}, mac);
        BleDataSendThread dataSendThread = new BleDataSendThread(data, mHandler, null);
        dataSendThread.start();
    }

    private void addModifyWarn(int cid, int Idx, boolean on, int hour, int min, int mode, String des) {
        if(!BleUtils.isConnected(this, mac)) {
            DialogUtils.makeToast(WarnActivity.this, R.string.ble_disconnected);
            return;
        }

        showProgressDialog();

        Message msg = mHandler.obtainMessage(SEND_CONTROL_CMD_TIME_OUT, Idx, 0);
        mHandler.sendMessageDelayed(msg, 5000);
        BleData data = BleDataParser.compsiteTimerRequest(cid, Idx, on, mode, hour, min, des, mac);
        BleDataSendThread dataSendThread = new BleDataSendThread(data, mHandler, null);
        dataSendThread.start();
    }

    private void showDeleteTimerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete);
        builder.setMessage(R.string.delete_timer);
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addModifyWarn(WARN_CONTROL_ID_DEL, selectedTimerIndex, false, 0, 0, 0, "");
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void onReceiveBleData(BlePacket packet) {
        Log.e(TAG, "onReceiveBleData:" + packet.cmd);
        if(packet.cmd == Commands.CMD_GET_WARN_LIST) {
            mHandler.removeMessages(GET_WARN_LIST_TIME_OUT);
            Log.e(TAG, "1");
            if(packet.length == 5) {//收到校验帧，结束。
                Log.e(TAG, "2");
                try {
                    mProgressDialog.cancel();
                    mAlarmsLvAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            YsTimer timer = BleDataParser.parseTimerFrom(packet);
            Log.e(TAG, "timer:" + timer.description + " " + timer.hour + ":" + timer.minutes + " " + timer.mode);
            if(!mYsTimers.contains(timer)) {
                mYsTimers.add(timer);
//                mHandler.sendEmptyMessage(GET_NEW_TIMER);
            }
        }
        else if(packet.cmd == Commands.CMD_WARN_CONTROL) {
            if(packet.data[0] == 0x00) {//
                mHandler.removeMessages(SEND_CONTROL_CMD_TIME_OUT);
                mYsTimers.clear();
                getWarnList();
            }
            else {
                mHandler.sendEmptyMessage(WARN_CONTROL_FAIL);
            }
        }
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        String msg = getString(R.string.waiting);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    @Override
    public void onReceiveData(BlePacket packet) {
        Log.e(TAG, "onReceiveData");
        mHandler.obtainMessage(RECEIVED_BLE_DATA, packet).sendToTarget();
    }

    @Override
    public void onReceiveRssi(int rssi) {

    }

    @Override
    public void onDataValid() {
        mHandler.obtainMessage(BLE_INVALID).sendToTarget();
    }

    private static class MyHandler extends Handler {
        WeakReference<WarnActivity> mWarnActivity;
        public MyHandler(WarnActivity activity) {
            mWarnActivity = new WeakReference<WarnActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            WarnActivity activity = mWarnActivity.get();
            switch(msg.what) {
                case GET_WARN_LIST_TIME_OUT:
                    activity.mProgressDialog.cancel();
                    DialogUtils.makeToast(activity, R.string.get_warn_list_timeout);
                    break;
                case SEND_CONTROL_CMD_TIME_OUT:
                    activity.mProgressDialog.cancel();
                    if (msg.arg1 == WARN_CONTRL_ID_MODI) {
                        activity.mAlarmsLvAdapter.notifyDataSetChanged();
                    }

                    DialogUtils.makeToast(activity, R.string.timeout_send_ble_command);
                    break;
                case RECEIVED_BLE_DATA:
                    activity.onReceiveBleData((BlePacket)msg.obj);
                    break;
                case BLE_INVALID:
                    activity.mProgressDialog.cancel();
                    removeMessages(GET_WARN_LIST_TIME_OUT);
                    removeMessages(SEND_CONTROL_CMD_TIME_OUT);
                    break;
                case GET_NEW_TIMER:
//                    mAlarmsLvAdapter.notifyDataSetChanged();
                    break;
                case WARN_CONTROL_FAIL:
                    DialogUtils.makeToast(activity, R.string.warn_control_fail);
                    break;
                case ON_TIMER_SWITCH_CHANGED:
                    YsTimer timer = activity.mYsTimers.get(msg.arg1);
                    activity.selectedTimerIndex = msg.arg1;
                    activity.addModifyWarn(WARN_CONTRL_ID_MODI, timer.id, !timer.status, timer.hour, timer.minutes, timer.mode, timer.description);
                    break;
                case ON_DELETE_BUTTON_CLICKED:
                    activity.selectedTimerIndex = msg.arg1;
                    activity.showDeleteTimerDialog();
                    break;
                case ON_RECEIVE_CHECK_PACKET:
                    activity.mProgressDialog.cancel();
                    break;
            }
        }
    }
}
