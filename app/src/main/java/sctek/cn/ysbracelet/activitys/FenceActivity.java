package sctek.cn.ysbracelet.activitys;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.FamiliesListViewAdapter;
import sctek.cn.ysbracelet.ble.BluetoothLeManager;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.uiwidget.HorizontalListView;
import sctek.cn.ysbracelet.uiwidget.RadarScanView;
import sctek.cn.ysbracelet.uiwidget.RandomTextView;
import sctek.cn.ysbracelet.user.FenceService;
import sctek.cn.ysbracelet.user.YsUser;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class FenceActivity extends AppCompatActivity {

    private final static String TAG = AlarmEditorActivity.class.getSimpleName();

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton actionIb;

    private RadarScanView fenceRsv;
    private RandomTextView memberRtv;

    private HorizontalListView familiesLv;

    private Switch fenceSw;

    private List<DeviceInformation> devicesSelected;

    private final static int REQUEST_ENABLE_BLUETOOTH = 1;

    private BluetoothLeManager mBluetoothLeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fence);

        mBluetoothLeManager = BluetoothLeManager.getInstance();

        if(!mBluetoothLeManager.isBluetoothEnabled()) {
            showTurnBluetoothOnDialog();
        }

        devicesSelected = new ArrayList<>();

        initViewElement();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startScanService();

        unregisterReceiver(bleScanBroadcastReceiver);
        devicesSelected.clear();
    }

    private void initViewElement() {

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        actionBarV.setBackgroundColor(getResources().getColor(R.color.chartreuse));
        titleTv.setText(R.string.fence_title);
        backIb.setOnClickListener(onViewClickedListener);
        actionIb.setVisibility(View.GONE);

        fenceSw = (Switch)findViewById(R.id.fence_sw);
        fenceSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    fenceRsv.startScan();
                    IntentFilter filter = new IntentFilter(FenceService.SCAN_BLE_COMPLETE_ACTION);
                    registerReceiver(bleScanBroadcastReceiver, filter);
                    startScanService();
                }
                else {
                    fenceRsv.stopScan();
                    unregisterReceiver(bleScanBroadcastReceiver);
                    for(DeviceInformation device : devicesSelected) {
                        memberRtv.removeKeyWord(device.name);
                    }
                    devicesSelected.clear();
                    memberRtv.removeAllViews();
                    stopScanService();
                }
            }
        });

        fenceRsv = (RadarScanView)findViewById(R.id.fence_rsv);
        memberRtv = (RandomTextView)findViewById(R.id.member_rtv);
        fenceRsv.stopScan();

        familiesLv = (HorizontalListView)findViewById(R.id.families_hlv);
        FamiliesListViewAdapter adapter = new FamiliesListViewAdapter(this, false);
        familiesLv.setAdapter(adapter);
        familiesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeviceInformation device = YsUser.getInstance().getDevice(position);
                if(devicesSelected.contains(device)) {
                    devicesSelected.remove(device);
                    memberRtv.removeKeyWord(device.name);
                    memberRtv.show();
                }
                else {
                    devicesSelected.add(device);
                }
            }
        });

    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nav_back_ib:
                    break;
            }
        }
    };

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

    private BroadcastReceiver bleScanBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, "onReceive " + action);
            if(action.equals(FenceService.SCAN_BLE_COMPLETE_ACTION)) {
                SharedPreferences sp = getSharedPreferences("sctek.cn.ysbracelet.fence", MODE_PRIVATE);
                for(DeviceInformation device : devicesSelected) {
                    if(sp.getBoolean(device.mac, false)) {
                        memberRtv.addKeyWord(device.name);
                    }
                    else {
                        memberRtv.removeKeyWord(device.name);
                    }
                }
                memberRtv.show();
            }
        }
    };

    private void startScanService() {
        Intent intent = new Intent(this, FenceService.class);
        startService(intent);
    }

    private void stopScanService() {
        Intent intent = new Intent(this, FenceService.class);
        stopService(intent);
    }
}
