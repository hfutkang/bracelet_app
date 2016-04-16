package sctek.cn.ysbracelet.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import sctek.cn.ysbracelet.device.DeviceInformation;

/**
 * Created by kang on 16-2-17.
 */
public class BluetoothLeManager {

    private final static String TAG = BluetoothLeManager.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    private String mBlAddress;
    private DeviceInformation mDevice;

    private BluetoothLeService mBluetoothLeService;

    private static final BluetoothLeManager mInstance = new BluetoothLeManager();

    public static BluetoothLeManager getInstance() {
        return mInstance;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(BleUtils.DEBUG) Log.e(TAG, "onServiceConnected");
            mBluetoothLeService = ((BluetoothLeService.MyBinder)service).getService();
            connect();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if(BleUtils.DEBUG) Log.e(TAG, "onServiceDisconnected");
            mBluetoothLeService = null;
        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void initial(Context context) {
        if(BleUtils.DEBUG) Log.e(TAG, "initial");
        mContext = context;
        mBluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if(mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }

//        Intent intent = new Intent(mContext, BluetoothLeService.class);
//        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public boolean isBluetoothEnabled() {
        if(mBluetoothAdapter == null)
            return false;
        return mBluetoothAdapter.isEnabled();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void startBleScanl(BluetoothAdapter.LeScanCallback callback) {
        if(mBluetoothAdapter != null&&mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.startLeScan(callback);
        }
    }

    public void stopBleScan(BluetoothAdapter.LeScanCallback callback) {
        if(mBluetoothAdapter != null)
            mBluetoothAdapter.stopLeScan(callback);
    }

    private void loadBindedDevice() {

        mDevice = new DeviceInformation();
        mDevice.setMac(null);
        mDevice.setName(null);
        mDevice.setSerialNumber(null);

    }

    public DeviceInformation getBindedDevice() { return mDevice; }

    public boolean connect() {
        if(BleUtils.DEBUG) Log.e(TAG, "connect");
        return mBluetoothLeService.connect(mContext, mBlAddress, mBluetoothAdapter, mBluetoothManager);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean isConnected() {
        if(BleUtils.DEBUG) Log.e(TAG, "isConnected");
        if(mBlAddress == null)
            return false;

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mBlAddress);
        return mBluetoothManager.getConnectionState(device, BluetoothProfile.GATT) == BluetoothProfile.STATE_CONNECTED;
    }

    public boolean sendData(byte[] data) {
        if(BleUtils.DEBUG) Log.e(TAG, "sendData");
        if (mBluetoothLeService != null)
            return mBluetoothLeService.sendData(data);
        return false;
    }

    public void setCharacteristicListener(BluetoothLeService.OnCharacteristicChangedListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "setCharacteristicListener");
            mBluetoothLeService.setOnCharacteristicChangedListener(listener);
    }

}
