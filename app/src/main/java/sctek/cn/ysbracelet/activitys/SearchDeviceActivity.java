package sctek.cn.ysbracelet.activitys;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
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
import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.ble.BluetoothLeManager;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class SearchDeviceActivity extends AppCompatActivity implements BluetoothAdapter.LeScanCallback{

    private final static String TAG = SearchDeviceActivity.class.getSimpleName();

    private TextView titleTv;
    private View actionBarV;
    private ImageButton backIb;
    private ImageButton actionIb;
    private ProgressBar progressBar;

    private ListView devicesLv;
    private TextView emptyTv;

    private Button searchBt;

    private BluetoothLeManager mBluetoothLeManager;

    private BaseAdapter mAdapter;
    private List<BleDevice> mDevices;

    private final static int REQUEST_ENABLE_BLUETOOTH = 1;
    private final static int SEARCH_PERIOD = 5000;
    public final static int DEVICE_BOND_BUTTON_CLICKED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_device);

        mBluetoothLeManager = BluetoothLeManager.getInstance();
        if(!mBluetoothLeManager.isBluetoothEnabled()) {
            showTurnBluetoothOnDialog();
        }

        mDevices = new ArrayList<>();

        initElement();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                startSearchDevice();
            }
        });

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

    }

    public void onSearchButtonClicked(View v) {
        if(BleUtils.DEBUG) Log.e(TAG, "onSearchButtonClicked");
        startSearchDevice();

    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.e(TAG, "onLeScan");
        emptyTv.setVisibility(View.GONE);
        BleDevice device1 = new BleDevice();
        device1.mac = device.getAddress();
        device1.name = device.getName();

        mDevices.add(device1);
        mAdapter.notifyDataSetChanged();
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

        mBluetoothLeManager.startBleScanl(this);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mBluetoothLeManager.stopBleScan(SearchDeviceActivity.this);

                progressBar.setVisibility(View.GONE);
                searchBt.setEnabled(true);
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
                    break;
            }
        }
    };

    public class BleDevice {
        public BleDevice(){}
        public String name;
        public String mac;
        public String id;
    }

}
