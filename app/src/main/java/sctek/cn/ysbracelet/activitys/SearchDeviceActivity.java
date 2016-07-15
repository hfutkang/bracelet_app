package sctek.cn.ysbracelet.activitys;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.DeviceListAdapter;
import sctek.cn.ysbracelet.ble.BleData;
import sctek.cn.ysbracelet.ble.BleDataParser;
import sctek.cn.ysbracelet.ble.BlePacket;
import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.ble.BluetoothLeManager;
import sctek.cn.ysbracelet.ble.BluetoothLeService;
import sctek.cn.ysbracelet.ble.Commands;
import sctek.cn.ysbracelet.ble.MyBluetoothGattCallBack;
import sctek.cn.ysbracelet.fragments.HomeFragment;
import sctek.cn.ysbracelet.thread.BleDataSendThread;
import sctek.cn.ysbracelet.utils.DialogUtils;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class SearchDeviceActivity extends AppCompatActivity implements BluetoothAdapter.LeScanCallback,
        MyBluetoothGattCallBack.OnCharacteristicChangedListener{

    private final static String TAG = SearchDeviceActivity.class.getSimpleName();

    private TextView titleTv;
    private View actionBarV;
    private ImageButton backIb;
    private ImageButton actionIb;
    private ProgressBar progressBar;

    private ListView devicesLv;
    private TextView emptyTv;

    private Button searchBt;

    private BaseAdapter mAdapter;
    private List<BleDevice> mDevices;

    private ProgressDialog mProgressDialog;

    private BleDevice mBindDevice;

    public final static int REQUEST_ENABLE_BLUETOOTH = 1;
    private final static int SEARCH_PERIOD = 5000;
    public final static int DEVICE_BOND_BUTTON_CLICKED = 1;
    public final static int GET_DEVICE_ID_TIME_OUT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_device);

        if(!BleUtils.isBluetoothEnabled(this)) {
            showTurnBluetoothOnDialog();
        }

        mDevices = new ArrayList<>();

//        loadDevice();

        initElement();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                startSearchDevice();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyBluetoothGattCallBack.getInstance().setBleListener(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == HomeFragment.REQUEST_CODE_ADD) {
            if(resultCode == HomeFragment.RESULT_CODE_ADD_OK) {
                setResult(HomeFragment.RESULT_CODE_ADD_OK);
                onBackPressed();
            }
        }
    }

    private void initElement() {

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        titleTv.setText(R.string.search_device_title);
        actionIb.setVisibility(View.GONE);

        devicesLv = (ListView)findViewById(R.id.devices_lv);
        emptyTv = (TextView)findViewById(R.id.empty_tv);

        searchBt = (Button)findViewById(R.id.search_bt);

        mAdapter = new DeviceListAdapter(this, mDevices, mHandler);
        devicesLv.setAdapter(mAdapter);

        mProgressDialog = new ProgressDialog(this);

        String msg = getString(R.string.waiting);
        mProgressDialog.setMessage(msg);

        IntentFilter filter = new IntentFilter(BluetoothLeService.ACTION_CONNECTED);
        registerReceiver(mBroadcastReceiver, filter);

        MyBluetoothGattCallBack.getInstance().setBleListener(SearchDeviceActivity.this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcastReceiver);
    }

    public void onSearchButtonClicked(View v) {
        if(BleUtils.DEBUG) Log.e(TAG, "onSearchButtonClicked");
        startSearchDevice();
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.e(TAG, "onLeScan");

        BleDevice device1 = new BleDevice();
        device1.mac = device.getAddress();
        device1.name = device.getName();

        for(BleDevice bd : mDevices) {
            if(bd.mac.equals(device.getAddress()))
                return;
        }
        emptyTv.setVisibility(View.GONE);
        mDevices.add(device1);
    }

    private void showTurnBluetoothOnDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.turn_on_bluetooth_title);
        builder.setMessage(R.string.turn_on_bluetooth_message);
        builder.setPositiveButton(R.string.turn_on_now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
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

    private void startSearchDevice() {

        mDevices.clear();
        mAdapter.notifyDataSetChanged();

        progressBar.setVisibility(View.VISIBLE);
        emptyTv.setVisibility(View.GONE);
        searchBt.setEnabled(false);

        BleUtils.startBleScanl(this, this);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                BleUtils.stopBleScan(SearchDeviceActivity.this, SearchDeviceActivity.this);

                progressBar.setVisibility(View.GONE);
                searchBt.setEnabled(true);
                mAdapter.notifyDataSetChanged();
                if(mDevices.size() == 0) {
                    emptyTv.setVisibility(View.VISIBLE);
                }

            }
        }, SEARCH_PERIOD);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DEVICE_BOND_BUTTON_CLICKED:
                    mBindDevice = mDevices.get(msg.arg1);

                    if(BleUtils.isConnected(SearchDeviceActivity.this, mBindDevice.mac))
                        sendData(mBindDevice.mac);
                    else
                        BluetoothLeManager.getInstance().connect(SearchDeviceActivity.this, mBindDevice.mac);
                    mProgressDialog.show();
                    sendEmptyMessageDelayed(GET_DEVICE_ID_TIME_OUT, 5000);

                    break;
                case GET_DEVICE_ID_TIME_OUT:
                    mProgressDialog.cancel();
            }
        }
    };

    @Override
    public void onReceiveData(BlePacket packet) {
        String id = BleDataParser.parseDeviceIdFrom(packet);
        mProgressDialog.cancel();
        mHandler.removeMessages(GET_DEVICE_ID_TIME_OUT);

        Bundle bundle = new Bundle();
        bundle.putString(HomeFragment.EXTR_DEVICE_ID, id);
        bundle.putString(HomeFragment.EXTR_DEVICE_MAC, mBindDevice.mac);
        Intent intent = new Intent(SearchDeviceActivity.this, SetDeviceInfoActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, HomeFragment.REQUEST_CODE_ADD);

    }

    @Override
    public void onReceiveRssi(int rssi) {

    }

    @Override
    public void onDataValid() {
        mProgressDialog.cancel();
        mHandler.removeMessages(GET_DEVICE_ID_TIME_OUT);
        DialogUtils.makeToast(this, R.string.get_device_id_fail);
    }

    public class BleDevice {
        public BleDevice(){}
        public String name;
        public String mac;
        public String id;
    }

    private void loadDevice() {
        BleDevice d = new BleDevice();
        d.mac = "AA:CC:BB:3E:53:4F";
        d.id = "YS0000000001";
        mDevices.add(d);
        d = new BleDevice();
        d.mac = "AA:CC:BB:3E:21:4F";
        d.id = "YS0000000002";
        mDevices.add(d);
        d = new BleDevice();
        d.mac = "AA:CC:BB:3E:DF:4F";
        d.id = "YS0000000003";
        mDevices.add(d);
        d = new BleDevice();
        d.mac = "AA:CC:BB:3E:CC:4F";
        d.id = "YS0000000004";
        mDevices.add(d);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothLeService.ACTION_CONNECTED.equals(intent.getAction())) {
                String mac = intent.getStringExtra("mac");
                if(mBindDevice == null)
                    return;
                if(mac.equals(mBindDevice.mac)) {
                    sendData(mac);
                }
            }
        }
    };

    private void sendData(String mac) {
        BleData data = new BleData(Commands.CMD_GET_DEVICE_ID, new byte[]{0}, mac);
        BleDataSendThread dataSendThread = new BleDataSendThread(data, mHandler, null);
        dataSendThread.start();
    }

}
