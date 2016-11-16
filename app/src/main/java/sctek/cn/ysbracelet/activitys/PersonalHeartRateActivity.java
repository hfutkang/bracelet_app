package sctek.cn.ysbracelet.activitys;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.ble.BleData;
import sctek.cn.ysbracelet.ble.BlePacket;
import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.ble.BluetoothLeManager;
import sctek.cn.ysbracelet.ble.BluetoothLeService;
import sctek.cn.ysbracelet.ble.Commands;
import sctek.cn.ysbracelet.ble.MyBluetoothGattCallBack;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.fragments.HomeFragment;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.thread.BleDataSendThread;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.user.UserManagerUtils;
import sctek.cn.ysbracelet.user.YsUser;
import sctek.cn.ysbracelet.utils.DialogUtils;

public class PersonalHeartRateActivity extends PersonalLatestDataBaseActivity implements HttpConnectionWorker.ConnectionWorkListener
    , MyBluetoothGattCallBack.OnCharacteristicChangedListener{

    private final static String TAG = PersonalHeartRateActivity.class.getSimpleName();

    private TextView rateTv;
    private Button startIb;

    private String mac;

    private BluetoothLeService mBluetoothLeService;

    private static final int HRATE_FROM_DEVICE = 1;
    private static final int START_MEASURE_HEART_RAT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, BluetoothLeService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        mac = YsUser.getInstance().getDevice(deviceId).mac;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    protected void initViewElement() {
        super.initViewElement();

        actionBarV.setBackgroundColor(Color.RED);
        titleTv.setText(R.string.heart_rate_title);
        actionIb.setOnClickListener(onViewClickedListener);
        backIb.setOnClickListener(onViewClickedListener);

        rateTv = (TextView)findViewById(R.id.rate_tv);
        startIb = (Button) findViewById(R.id.start_measure_ib);
        startIb.setOnClickListener(onViewClickedListener);

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_heart_rate);
    }

    @Override
    protected void loadLatestRecord() {

        if(deviceId == null) {
            return;
        }

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.HeartRate.CONTENT_URI
                        , new String[]{LocalDataContract.HeartRate.COLUMNS_NAME_RATE, LocalDataContract.HeartRate.COLUMNS_NAME_TIME}
                        , LocalDataContract.HeartRate.COLUMNS_NAME_DEVICE + "=" + "'" + deviceId + "'"
                        , null
                        , LocalDataContract.HeartRate.COLUMNS_NAME_TIME + " desc limit 1");

        if(cursor.moveToFirst()) {
            timeTv.setText(cursor.getString(1));
            Log.e(TAG, "" + cursor.getInt(0));
            rateTv.setText("" + cursor.getInt(0));
        }
        else {
            timeTv.setText(R.string.no_data_yet);
        }

        cursor.close();

    }

    @Override
    public void onWorkDone(int resCode) {

        if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS){
            DialogUtils.makeToast(this, R.string.send_measure_command_success);
        }
        else if(resCode == XmlNodes.RESPONSE_CODE_FAIL) {
            DialogUtils.makeToast(this, R.string.send_measure_command_fail);
        }
   }

    @Override
    public void onResult(YsData result) {
    }

    @Override
    public void onError(Exception e) {
        DialogUtils.makeToast(this, R.string.send_measure_command_fail);
    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nav_back_ib:
                    onBackPressed();
                    break;
                case R.id.action_ib:
                    Intent intent = new Intent(PersonalHeartRateActivity.this, PersonalHistoryHRateActivity.class);
                    intent.putExtra(HomeFragment.EXTR_DEVICE_ID, deviceId);
                    startActivity(intent);
                    break;
                case R.id.start_measure_ib:
                    if(BleUtils.isConnected(PersonalHeartRateActivity.this, mac)) {
                        measureHeartRate(mac);
                    }
                    else {
                        UserManagerUtils.startMeasureHeartRate(deviceId, PersonalHeartRateActivity.this);
                    }
                    break;
//                    UserManagerUtils.startMeasureHeartRate(deviceId, PersonalHeartRateActivity.this);
            }
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(BleUtils.DEBUG) Log.e(TAG, "onServiceConnected");
            MyBluetoothGattCallBack.getInstance().setBleListener(PersonalHeartRateActivity.this);
            BluetoothLeManager.connect(PersonalHeartRateActivity.this, mac, false);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if(BleUtils.DEBUG) Log.e(TAG, "onServiceDisconnected");
            mBluetoothLeService = null;
        }
    };

    @Override
    public void onReceiveData(BlePacket packet) {
        if(packet.cmd == Commands.CMD_MEASURE_HRATE) {
            mHandler.obtainMessage(START_MEASURE_HEART_RAT).sendToTarget();
        }
        else if(packet.cmd == Commands.CMD_HRATE_FROM_DEVICE) {
            mHandler.obtainMessage(HRATE_FROM_DEVICE, packet.data[1], 0).sendToTarget();
        }
    }

    @Override
    public void onReceiveRssi(int rssi) {

    }

    @Override
    public void onDataValid() {

    }

    private void measureHeartRate(String mac) {
        BleData data = new BleData(Commands.CMD_MEASURE_HRATE, new byte[]{0x01, 0x01}, mac);
        BleDataSendThread dataSendThread = new BleDataSendThread(data, mHandler, null);
        dataSendThread.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HRATE_FROM_DEVICE:
                    rateTv.setText("" + msg.arg1);
                    break;
                case START_MEASURE_HEART_RAT:
                    DialogUtils.makeToast(PersonalHeartRateActivity.this, R.string.measure_in_progress);
                    break;
            }
        }
    };
}
