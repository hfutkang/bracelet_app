package sctek.cn.ysbracelet.ble;

import android.util.Log;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by kang on 16-2-19.
 */
public class BlePacket {

    private final static String TAG = BlePacket.class.getSimpleName();

    public final static int PACKET_SIZE = 20;

    private final static byte[] BLANK_BYTE_ARRAY = new byte[]{
            0x0,0x0,0x0,0x0,0x0,
            0x0,0x0,0x0,0x0,0x0,
            0x0,0x0,0x0,0x0,0x0,
            0x0,0x0,0x0,0x0,0x0};

    public byte[] data;
    public int cmd;
    public int seq;
    public boolean isHead;
    public boolean hasNext;
    public int length;
    public int type;

    public BlePacket() {
        data = null;
        cmd = -1;
        seq = -1;
        isHead = false;
        hasNext = false;
        length = 0;
    }

    public void init(byte[] packet) {
        cmd = packet[0];
        length = (packet[1] & 0x0F) + 1;
        type = (packet[1] >> 4) &0x0F;
        seq = packet[2];
        isHead = seq == 1;
        hasNext = seq > 1;
        data = Arrays.copyOfRange(packet, 3, 3+length);
    }

    public static boolean isValid(byte[] pakcet) {
        if(BleUtils.DEBUG) Log.e(TAG, "isValid");

        byte temp = pakcet[0];

        for(int i = 1; i < pakcet.length - 1; i++) {
            temp ^= pakcet[i];
        }

        if(temp != pakcet[PACKET_SIZE -1])
            return false;
        return true;
    }

    public static byte[] parsePacket(byte cmd, int seq, byte[] data) {
        if(BleUtils.DEBUG) Log.e(TAG, "parsePacket");

        ByteBuffer packetBuffer = ByteBuffer.allocate(20);

        packetBuffer.put(BLANK_BYTE_ARRAY);
        packetBuffer.position(0);

        packetBuffer.put(cmd);

        if(data != null) {
            byte leng = (byte)data.length;

            byte l = (byte)(leng -1);
            packetBuffer.put(l);
            packetBuffer.put((byte)seq);
            packetBuffer.put(data);
        } else {
            byte leng = 0x00;
            packetBuffer.put(leng);
            packetBuffer.put((byte)seq);
            byte d = 0x00;
            packetBuffer.put(d);
        }

        byte checkByte = packetBuffer.get(0);
        for(int i = 1; i < packetBuffer.capacity(); i++) {
            checkByte ^= packetBuffer.get(i);
        }

        packetBuffer.put(PACKET_SIZE - 1, checkByte);

        return packetBuffer.array();
    }
}
