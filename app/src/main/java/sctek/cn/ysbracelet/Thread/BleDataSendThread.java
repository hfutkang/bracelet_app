package sctek.cn.ysbracelet.Thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import sctek.cn.ysbracelet.ble.BleData;
import sctek.cn.ysbracelet.ble.BlePacket;
import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.ble.BluetoothLeManager;

/**
 * Created by kang on 16-2-18.
 */
public class BleDataSendThread extends Thread {

    private final static String TAG = BleDataSendThread.class.getSimpleName();

    private final static int MAX_REPEAT_COUNT = 3;
    private final static int SEND_INTERVAL_TIME = 40;
    private final static int REPEAT_INTERVAL_TIME = 1000;

    public final static int FAIL_SEND_DATA = 0;
    public final static int SUCCESS_SNED_DATA = 1;

    private BleData data;

    private volatile boolean needRepeat = true;
    private int repeatCount = 0;

    private DataSendStateListener mDataSendStateListener;
    private Handler mHandler;

    public BleDataSendThread(BleData data, DataSendStateListener listener) {
        this.data = data;
        mDataSendStateListener = listener;
    }

    public BleDataSendThread(BleData data, Handler handler) {
        this.data = data;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        super.run();
        if(BleUtils.DEBUG) Log.e(TAG, "run");
            if(!sendData(data)) {
                Message msg = mHandler.obtainMessage(data.getCmd(), FAIL_SEND_DATA, -1);
                msg.sendToTarget();
            } else {
                Message msg = mHandler.obtainMessage(data.getCmd(), SUCCESS_SNED_DATA, -1);
                msg.sendToTarget();
            }
    }

    public interface DataSendStateListener {
        void onComplete();
        void onTimeOut();
        void onError();
    }

    private boolean sendData(BleData data) {

        if(BleUtils.DEBUG) Log.e(TAG, "sendData");

        int offset = 0;
        int seq = 1;

        do {

            boolean isHead = seq == 1?true:false;

            if(!data.hasNext(offset))
                seq = 0;

            byte[] seg = data.getNextSegment(offset);
            byte[] packet = BlePacket.parsePacket((byte) data.getCmd(), seq, seg, isHead);
            if(!BluetoothLeManager.getInstance().sendData(packet))
                return false;

            try {
                Thread.sleep(SEND_INTERVAL_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            seq++;
            offset += BleData.MAX_SEGMENT_SIZE;
        } while (data.hasNext(offset));
        return true;
    }

    public void setNeedRepeat(boolean needRepeat) {
        this.needRepeat = needRepeat;
    }
}
