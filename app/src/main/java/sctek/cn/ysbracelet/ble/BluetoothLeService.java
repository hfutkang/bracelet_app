package sctek.cn.ysbracelet.ble;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.user.YsUser;

/**
 * Created by kang on 16-2-17.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothLeService extends Service {

    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private final IBinder mBinder = new MyBinder();

    public final static String ACTION_CONNECTED = "sctek.cn.ysbracelet.ble.connected";
    public final static String ACTION_DISCONNECTED = "sctek.cn.ysbracelet.ble.disconnected";

    private final static String SERVICE_UUID = "0000fee9-0000-1000-8000-00805f9b34fb";
    private final static String WRITE_CHARAC_UUID = "d44bc439-abfd-45a2-b575-925416129600";
    private final static String NOTIF_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb";

    static private Map<String, BluetoothGatt> mMacGattMaps = new HashMap<>();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    public BluetoothLeService() {
        Log.e(TAG, "BluetoothLeService");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean connect(String address) {
        Log.e(TAG, "connect " + address);
        if(address == null || mBluetoothAdapter == null || mBluetoothManager == null)
            return false;

        BluetoothGatt gatt = mMacGattMaps.get(address);
        if(gatt != null) {
            if(gatt.connect()) {
                return true;
            } else {
                return false;
            }
        }

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        if(device == null)
            return false;

        gatt = device.connectGatt(getApplicationContext(), true, MyBluetoothGattCallBack.getInstance());
        mMacGattMaps.put(address, gatt);
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");

        mBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");

        SharedPreferences sp = getSharedPreferences("sctek.cn.ysbracelet.fence", Context.MODE_PRIVATE);

        if(sp.getBoolean("on", false)) {
//            if(!mThread.isAlive())
//                mThread.start();
            List<DeviceInformation> devices = YsUser.getInstance().getDevices();
            for(DeviceInformation di : devices) {
                connect(di.mac);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public BluetoothGattCharacteristic getWriteCharacteristic(BluetoothGatt gatt) {
        BluetoothGattService gattService = gatt.getService(UUID.fromString(SERVICE_UUID));
        return gattService.getCharacteristic(UUID.fromString(WRITE_CHARAC_UUID));
    }

    public boolean sendData(byte[] data, String mac) {
        BluetoothGatt gatt = mMacGattMaps.get(mac);
        BluetoothGattCharacteristic writeCharacteristic = getWriteCharacteristic(gatt);
        return BleUtils.sendData(gatt, writeCharacteristic, data);
    }

}
