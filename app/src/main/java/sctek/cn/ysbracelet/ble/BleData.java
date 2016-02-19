package sctek.cn.ysbracelet.ble;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by kang on 16-2-19.
 */
public class BleData {

    private final static String TAG = BleData.class.getSimpleName();

    public final static int MAX_SEGMENT_SIZE = 16;

    private byte[] dataBuffer;
    private int cmd;

    public BleData(int cmd, byte[] data) {
        this.cmd = cmd;
        dataBuffer = data;
    }

    public void append(byte[] data) {
        if(BleUtils.DEBUG) Log.e(TAG, "append");
        if(dataBuffer == null)
            dataBuffer = new byte[0];

        byte[] temp = Arrays.copyOf(dataBuffer, dataBuffer.length + data.length);
        System.arraycopy(data, 0, temp, dataBuffer.length, data.length);
        dataBuffer = temp;
    }

    public int getTotalLength() {
        if(dataBuffer == null)
            return 0;
        return dataBuffer.length;
    }

    public byte[] getData() {
        return dataBuffer;
    }

    public int getCmd() {
        return cmd;
    }

    public byte[] getNextSegment(int offset) {
        if(BleUtils.DEBUG) Log.e(TAG, "getNextSegment");
        int totalLeng = getTotalLength();

        if(offset < 0 || offset >= totalLeng)
            return null;
        if(offset + MAX_SEGMENT_SIZE <= totalLeng)
            return Arrays.copyOfRange(dataBuffer, offset, offset+ MAX_SEGMENT_SIZE);
        else
            return Arrays.copyOfRange(dataBuffer, offset, totalLeng);
    }

    public boolean hasNext(int offset) {
        if(offset < 0)
            return false;
        return offset < getTotalLength();
    }

}
