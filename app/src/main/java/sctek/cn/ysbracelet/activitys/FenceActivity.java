package sctek.cn.ysbracelet.activitys;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;
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
import java.util.Random;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.FamiliesListViewAdapter;
import sctek.cn.ysbracelet.ble.BleData;
import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.ble.BluetoothLeManager;
import sctek.cn.ysbracelet.ble.BluetoothLeService;
import sctek.cn.ysbracelet.ble.Commands;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.thread.BleDataSendThread;
import sctek.cn.ysbracelet.uiwidget.HorizontalListView;
import sctek.cn.ysbracelet.uiwidget.RadarScanView;
import sctek.cn.ysbracelet.uiwidget.RandomTextView;
import sctek.cn.ysbracelet.user.FenceService;
import sctek.cn.ysbracelet.user.YsUser;
import sctek.cn.ysbracelet.utils.DialogUtils;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class FenceActivity extends AppCompatActivity {

    private final static String TAG = FenceActivity.class.getSimpleName();

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

    public final static int NOTIFICATION_ID = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fence);

        if(!BleUtils.isBluetoothEnabled(this)) {
            showTurnBluetoothOnDialog();
        }

        devicesSelected = new ArrayList<>();

        initViewElement();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                connectAllDevice();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!fenceSw.isChecked())
            stopScanService();
        try {
            unregisterReceiver(bleScanBroadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

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

        SharedPreferences sp = getSharedPreferences("sctek.cn.ysbracelet.fence", MODE_PRIVATE);
        boolean isOn = sp.getBoolean("on", false);
        fenceSw = (Switch)findViewById(R.id.fence_sw);
        fenceSw.setChecked(isOn);
        fenceSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    fenceRsv.startScan();
                    IntentFilter filter = new IntentFilter(FenceService.SCAN_BLE_COMPLETE_ACTION);
                    filter.addAction(BluetoothLeService.ACTION_CONNECTED);
                    filter.addAction(BluetoothLeService.ACTION_DISCONNECTED);
                    filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
                    registerReceiver(bleScanBroadcastReceiver, filter);

                    SharedPreferences sp = getSharedPreferences("sctek.cn.ysbracelet.fence", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("on", true);
                    editor.commit();
                    showNotification();
                }
                else {
                    fenceRsv.stopScan();
                    unregisterReceiver(bleScanBroadcastReceiver);
                    for(DeviceInformation device : devicesSelected) {
                        memberRtv.removeKeyWord(device.name);
                    }
                    devicesSelected.clear();
                    memberRtv.removeAllViews();

                    SharedPreferences sp = getSharedPreferences("sctek.cn.ysbracelet.fence", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("on", false);
                    editor.commit();

                    ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).cancel(NOTIFICATION_ID);

                    stopScanService();
                }
            }
        });

        fenceRsv = (RadarScanView)findViewById(R.id.fence_rsv);
        memberRtv = (RandomTextView)findViewById(R.id.member_rtv);

        if(!fenceSw.isChecked())
            fenceRsv.stopScan();
        else
            loadViews();

        familiesLv = (HorizontalListView)findViewById(R.id.families_hlv);
        FamiliesListViewAdapter adapter = new FamiliesListViewAdapter(this, false);
        familiesLv.setAdapter(adapter);
        familiesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!fenceSw.isChecked())
                    return;
                DeviceInformation device = YsUser.getInstance().getDevice(position);
                if(devicesSelected.contains(device)) {
                    devicesSelected.remove(device);
                    memberRtv.removeKeyWord(device.name);
                    memberRtv.show();

                    SharedPreferences sp = getSharedPreferences("sctek.cn.ysbracelet.fence", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean(device.mac, true);
                    editor.commit();
                }
                else {
                    if(!fenceSw.isChecked())
                        return;

                    SharedPreferences sp = getSharedPreferences("sctek.cn.ysbracelet.fence", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean(device.mac, false);
                    editor.commit();

                    devicesSelected.add(device);
                    if(BleUtils.isConnected(FenceActivity.this, device.mac)) {
                        memberRtv.addKeyWord(device.name);
                        memberRtv.show();
                    }
                    else {
                        DialogUtils.makeToast(FenceActivity.this, R.string.device_not_found);
                    }
                }
            }
        });

    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nav_back_ib:
                    onBackPressed();
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
            else if(action.equals(BluetoothLeService.ACTION_CONNECTED)) {
                String mac = intent.getStringExtra("mac");
                if(fenceSw.isChecked()) {
                    for (DeviceInformation di : devicesSelected) {
                        if (mac.equals(di.getMac())) {
                            memberRtv.addKeyWord(di.name);
                            memberRtv.show();
                        }
                    }
                }
            }
            else if(action.equals(BluetoothLeService.ACTION_DISCONNECTED)) {
                String mac = intent.getStringExtra("mac");
                if(fenceSw.isChecked()) {
                    for (DeviceInformation di : devicesSelected) {
                        if (mac.equals(di.getMac())) {
                            memberRtv.removeKeyWord(di.name);
                            memberRtv.show();
                        }
                    }
                }
            }
            abortBroadcast();
        }
    };

    private void startScanService() {
        Intent intent = new Intent(this, BluetoothLeService.class);
        startService(intent);
    }

    private void stopScanService() {
        Intent intent = new Intent(this, BluetoothLeService.class);
        stopService(intent);
    }

    private void connectAllDevice() {
        List<DeviceInformation> devices = YsUser.getInstance().getDevices();
        for(DeviceInformation di : devices) {
            BluetoothLeManager.getInstance().connect(this, di.getMac());
        }
    }

    private void sendMotorControlCmd(byte mode) {
        for (DeviceInformation di : devicesSelected) {
            BleData data = new BleData(Commands.CMD_MOTOR_CONTROL
                    , new byte[]{mode, (byte)0x00, (byte)0x04, (byte)0x00, (byte)0x04}
                    , di.getMac());
            new BleDataSendThread(data, new BleDataSendThread.DataSendStateListener() {
                @Override
                public void onComplete() {

                }

                @Override
                public void onTimeOut() {

                }

                @Override
                public void onError() {

                }
            }, null).start();
        }
    }

    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentText(getText(R.string.fence_on));

        Intent clickedIntent = new Intent(this, FenceActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(FenceActivity.class);
        stackBuilder.addNextIntent(clickedIntent);
        Random random = new Random(System.currentTimeMillis());
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(random.nextInt(), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void loadViews() {
        SharedPreferences sp = getSharedPreferences("sctek.cn.ysbracelet.fence", MODE_PRIVATE);
        List<DeviceInformation> devices = YsUser.getInstance().getDevices();
        for(DeviceInformation di : devices) {
            if(sp.getBoolean(di.mac, false)) {
                devicesSelected.add(di);
                memberRtv.addKeyWord(di.name);
            }
        }
        memberRtv.show();

        IntentFilter filter = new IntentFilter(FenceService.SCAN_BLE_COMPLETE_ACTION);
        filter.addAction(BluetoothLeService.ACTION_CONNECTED);
        filter.addAction(BluetoothLeService.ACTION_DISCONNECTED);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(bleScanBroadcastReceiver, filter);

//        startScanService();
    }
}
