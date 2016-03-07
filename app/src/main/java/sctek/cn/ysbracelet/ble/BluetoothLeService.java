package sctek.cn.ysbracelet.ble;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import java.util.List;
import java.util.UUID;

/**
 * Created by kang on 16-2-17.
 */
public class BluetoothLeService extends Service {

    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private final IBinder mBinder = new MyBinder();

    public final static String ACTION_CONNECTED = "sctek.cn.ysbracelet.ble.connected";
    public final static String ACTION_DISCONNECTED = "sctek.cn.ysbracelet.ble.disconnected";

    private final static String SERVICE_UUID = "0000fee9-0000-1000-8000-00805f9b34fb";
    private final static String WRITE_CHARAC_UUID = "d44bc439-abfd-45a2-b575-925416129600";
    private final static String NOTIF_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb";

    private BluetoothGatt mBluetoothGatt;
    private String mBleDeviceAddress;

    private BluetoothGattCharacteristic mWriteCharacteristic;

    private OnCharacteristicChangedListener mOnCharacteristicChangedListener;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean connect(Context context, String address, BluetoothAdapter adapter, BluetoothManager manager) {
        if(address == null || adapter == null || manager == null)
            return false;

        if(mBleDeviceAddress != null && address.equals(mBleDeviceAddress)
                && mBluetoothGatt != null) {
            if(mBluetoothGatt.connect()) {
                return true;
            } else {
                return false;
            }
        }

        BluetoothDevice device = adapter.getRemoteDevice(address);
        if(device == null)
            return false;

        mBluetoothGatt = device.connectGatt(context, true, mBluetoothGattCallback);
        mBleDeviceAddress = address;
        return true;
    }

    private final BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    gatt.discoverServices();
                    sendBroadcast(new Intent(ACTION_CONNECTED));
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    sendBroadcast(new Intent(ACTION_DISCONNECTED));
                    break;
                case BluetoothProfile.STATE_CONNECTING:
                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            BluetoothGattService gattService = gatt.getService(UUID.fromString(SERVICE_UUID));

            if(gattService != null) {
                mWriteCharacteristic = gattService.getCharacteristic(UUID.fromString(WRITE_CHARAC_UUID));

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

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] data = characteristic.getValue();
            BlePacket pk = new BlePacket();
            pk.init(data);
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
    };

    public boolean sendData(byte[] data) {
        return BleUtils.sendData(mBluetoothGatt, mWriteCharacteristic, data);
    }

    public void setOnCharacteristicChangedListener(OnCharacteristicChangedListener listener) {
        mOnCharacteristicChangedListener = listener;
    }

    public interface OnCharacteristicChangedListener {
        void onReceiveData(BlePacket packet);
        void onReceiveRssi(int rssi);
    }
}
