package sctek.cn.ysbracelet.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by kang on 16-7-6.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MyBluetoothGattCallBack extends BluetoothGattCallback{

    private static final String TAG = MyBluetoothGattCallBack.class.getSimpleName();

    public final static String ACTION_CONNECTED = "sctek.cn.ysbracelet.ble.connected";
    public final static String ACTION_DISCONNECTED = "sctek.cn.ysbracelet.ble.disconnected";

    private final static String SERVICE_UUID = "0000fee9-0000-1000-8000-00805f9b34fb";
    private final static String WRITE_CHARAC_UUID = "d44bc439-abfd-45a2-b575-925416129600";
    private final static String NOTIF_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb";

    private Context mContext;
    private OnCharacteristicChangedListener mOnCharacteristicChangedListener;

    private static MyBluetoothGattCallBack instance;

    int xxx;
    private MyBluetoothGattCallBack(Context context) {
        xxx = new Random(System.currentTimeMillis()).nextInt();
        Log.e(TAG,"xxx:" + xxx);
        mContext = context;
    }

    public static void init(Context context) {
        if(instance == null)
            instance = new MyBluetoothGattCallBack(context);
    }

    public static MyBluetoothGattCallBack getInstance() {
        if(instance == null) {
            Log.e(TAG, "Not initid");
            return null;
        }
        return instance;
    }

    public void setBleListener(OnCharacteristicChangedListener listener) {
        mOnCharacteristicChangedListener = listener;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        BluetoothDevice device = gatt.getDevice();
        String mac = device.getAddress();
        Intent intent = null;
        switch (newState) {
            case BluetoothProfile.STATE_CONNECTED:
                gatt.discoverServices();

                Log.e(TAG, "ble connected mac:" + mac);
                intent = new Intent(ACTION_CONNECTED);
                intent.putExtra("mac", mac);

                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                Log.e(TAG, "ble disconnected mac:" + mac);
                intent = new Intent(ACTION_DISCONNECTED);
                intent.putExtra("mac", mac);

                break;
            case BluetoothProfile.STATE_CONNECTING:
                break;
            case BluetoothProfile.STATE_DISCONNECTING:
                break;
        }

        if(intent != null)
            mContext.sendOrderedBroadcast(intent, null);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        BluetoothGattService gattService = gatt.getService(UUID.fromString(SERVICE_UUID));

        if(gattService != null) {

            List<BluetoothGattCharacteristic> characteristics = gattService.getCharacteristics();

            for(BluetoothGattCharacteristic chara : characteristics) {
                if(chara.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                    BleUtils.setCharacteristicNotification(gatt, chara, true, NOTIF_DESCRIPTOR_UUID);
                }
            }
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        Log.e(TAG,"xxx:" + xxx);
        Log.e(TAG, "onCharacteristicChanged");
        byte[] data = characteristic.getValue();
        Log.e(TAG, "length:" + data.length);
        String result = "";
        for(byte b : data) {
            result += Integer.toHexString(b) + " ";
        }
        Log.e(TAG, result);
        BlePacket pk = new BlePacket();
        pk.init(data);
        if(mOnCharacteristicChangedListener == null) {
            Log.e(TAG, "CharacteristicChangedListener is null");
        }
        mOnCharacteristicChangedListener.onReceiveData(pk);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorRead(gatt, descriptor, status);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        super.onReliableWriteCompleted(gatt, status);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
        mOnCharacteristicChangedListener.onReceiveRssi(rssi);
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        super.onMtuChanged(gatt, mtu, status);
    }

    public interface OnCharacteristicChangedListener {
        void onReceiveData(BlePacket packet);
        void onReceiveRssi(int rssi);
        void onDataValid();
    }

    private static class NotInitException extends Exception {

        @Override
        public String getMessage() {
            return "Not inited exception";
        }

        @Override
        public String toString() {
            return "Not inited exception";
        }
    }

}
