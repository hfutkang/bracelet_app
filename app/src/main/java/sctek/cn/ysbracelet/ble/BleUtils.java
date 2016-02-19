package sctek.cn.ysbracelet.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;

import java.util.UUID;

/**
 * Created by kang on 16-2-17.
 */
public class BleUtils {

    public final static String TAG = BleUtils.class.getSimpleName();

    public final static int PACKET_SIZE = 20;

    public final static boolean DEBUG = true;

    public static boolean setCharacteristicNotification(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic
            , boolean enabled, String descriptorUuid) {
        if(BleUtils.DEBUG) Log.e(TAG, "setCharacteristicNotification");
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }

        bluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        try {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(descriptorUuid ));
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                return (bluetoothGatt.writeDescriptor(descriptor));
            }else{
                Log.e(TAG, "descriptor is null");
                return false;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean sendData(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic writeCharacteristic, byte[] qppData){

        if(BleUtils.DEBUG) Log.e(TAG, "sendData");

        if(bluetoothGatt == null){
            Log.e(TAG,"BluetoothAdapter not initialized !");
            return false;
        }

        if(qppData == null){
            Log.e(TAG,"qppData = null !");
            return false;
        }
        return writeValue(bluetoothGatt, writeCharacteristic, qppData);
    }

    private static boolean writeValue(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] bytes){
        if(BleUtils.DEBUG) Log.e(TAG, "writeValue");
        if(gatt == null){
            Log.e(TAG,"BluetoothAdapter not initialized");
            return false;
        }
        //PrintBytes(bytes);
        characteristic.setValue(bytes);
        return gatt.writeCharacteristic(characteristic);
    }

}
