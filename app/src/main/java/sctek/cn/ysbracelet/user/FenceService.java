package sctek.cn.ysbracelet.user;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import sctek.cn.ysbracelet.ble.BleUtils;

/**
 * Created by kang on 16-4-14.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class FenceService extends Service implements BluetoothAdapter.LeScanCallback{

    private static final String TAG = FenceService.class.getSimpleName();

    private SharedPreferences.Editor editor;

    public final static String SCAN_BLE_COMPLETE_ACTION = "sctek.cn.ysbracelet.ACTION.SCAN_BLE_COMPLETE";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        String mac = device.getAddress();
        if(YsUser.getInstance().getDeviceByMac(mac) != null) {
            editor.putBoolean(mac, true);
            editor.commit();
        }
    }

    public class MyBinder extends Binder {
        public FenceService getService() {
            return FenceService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        editor = getSharedPreferences("sctek.cn.ysbracelet.fence", MODE_PRIVATE).edit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        if(!scanThread.isAlive())
            scanThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        scanThread.interrupt();
        BleUtils.stopBleScan(this, this);
        SharedPreferences sp = getSharedPreferences("sctek.cn.ysbracelet.fence", MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    private Thread scanThread = new Thread() {
        @Override
        public void run() {
            SharedPreferences sp = getSharedPreferences("sctek.cn.ysbracelet.fence", MODE_PRIVATE);
            while (true) {
                Log.e(TAG, "start scan");
                if(isInterrupted()) {
                    Log.e(TAG, "thread interrupted");
                    return;
                }
                BleUtils.stopBleScan(FenceService.this, FenceService.this);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG, "thread interrupted");
                    return;
                }

                sp.edit().clear().commit();
                BleUtils.startBleScanl(FenceService.this, FenceService.this);

                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG, "thread interrupted");
                    return;
                }
                sp = getSharedPreferences("sctek.cn.ysbracelet.fence", MODE_PRIVATE);
                sp.edit().putBoolean("0F:14:BC:8A:4E:CB", true).commit();
                sendBroadcast(new Intent(SCAN_BLE_COMPLETE_ACTION));
            }
        }
    };

}
