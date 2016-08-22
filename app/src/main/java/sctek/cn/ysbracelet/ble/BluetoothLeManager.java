package sctek.cn.ysbracelet.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by kang on 16-2-17.
 */
public class BluetoothLeManager {

    private final static String TAG = BluetoothLeManager.class.getSimpleName();

    private final static String SERVICE_UUID = "0000fee9-0000-1000-8000-00805f9b34fb";
    private final static String WRITE_CHARAC_UUID = "d44bc439-abfd-45a2-b575-925416129600";
    private final static String NOTIF_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb";

    static private Map<String, BluetoothGatt> mMacGattMaps = new HashMap<>();

    private static final BluetoothLeManager mInstance = new BluetoothLeManager();

    public static BluetoothLeManager getInstance() {
        return mInstance;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static boolean connect(Context context, String address) {
        Log.e(TAG, "connect " + address);
        BluetoothManager mBluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = mBluetoothManager.getAdapter();
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

        gatt = device.connectGatt(context.getApplicationContext(), false, MyBluetoothGattCallBack.getInstance());
        mMacGattMaps.put(address, gatt);
        Log.e(TAG, "1111");
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void disconnect(String addr) {
        BluetoothGatt gatt = mMacGattMaps.get(addr);
        if(gatt != null)
            gatt.disconnect();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static BluetoothGattCharacteristic getWriteCharacteristic(BluetoothGatt gatt) {
        BluetoothGattService gattService = gatt.getService(UUID.fromString(SERVICE_UUID));
        if(gattService == null)
            return null;
        return gattService.getCharacteristic(UUID.fromString(WRITE_CHARAC_UUID));
    }

    public static boolean sendData(byte[] data, String mac) {
        BluetoothGatt gatt = mMacGattMaps.get(mac);
        BluetoothGattCharacteristic writeCharacteristic = getWriteCharacteristic(gatt);
        return BleUtils.sendData(gatt, writeCharacteristic, data);
    }

}
