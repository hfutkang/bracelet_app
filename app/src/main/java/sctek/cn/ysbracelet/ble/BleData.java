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
    private String mac;

    public BleData(int cmd, byte[] data, String mac1) {
        this.cmd = cmd;
        dataBuffer = data;
        mac = mac1;
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

    public String getMac() {
        return mac;
    }

    public int getSequenceNumber(int offset) {
        int leftLenght = dataBuffer.length - offset;

        if(leftLenght < MAX_SEGMENT_SIZE)
            return 1;

        int seq = leftLenght/MAX_SEGMENT_SIZE;

        if(leftLenght%MAX_SEGMENT_SIZE == 0)
            return seq;
        else
            return seq + 1;
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
