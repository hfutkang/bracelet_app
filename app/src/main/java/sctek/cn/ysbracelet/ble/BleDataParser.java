package sctek.cn.ysbracelet.ble;

import android.util.Log;

import java.nio.ByteBuffer;
import java.util.Arrays;

import sctek.cn.ysbracelet.devicedata.HeartRateData;
import sctek.cn.ysbracelet.devicedata.SleepData;
import sctek.cn.ysbracelet.devicedata.SportsData;
import sctek.cn.ysbracelet.devicedata.YsTimer;

/**
 * Created by kang on 16-3-7.
 */
public class BleDataParser {

    private final static String TAG = BleDataParser.class.getSimpleName();

    public static SportsData parseSporsDataFrom(BlePacket bpk) {
        byte[] data = bpk.data;
        SportsData sd = new SportsData();

        int temp1 = data[0];
        int temp2 = data[1];
        int temp3 = data[2];
        temp1 = temp1<<16;
        temp2 = temp2<<8;
        sd.walkSteps = temp1 + temp2 + temp3;

        temp1 = data[3];
        temp2 = data[4];
        temp3 = data[5];
        temp1 = temp1<<16;
        temp2 = temp2<<8;
        sd.runSteps = temp1 + temp2 + temp3;

        return sd;
    }

    public static HeartRateData parseHeartRateDataFrom(BlePacket bpk) {
        HeartRateData hd = new HeartRateData();
        return hd;
    }

    public static SleepData parseSleepDataFrom(BlePacket bpk) {
        SleepData sd = new SleepData();
        return sd;
    }

    public static String parseDeviceIdFrom(BlePacket bpk) {

        String deviceId = new String(bpk.data);
        Log.e(TAG, "deviceId:" + deviceId);
        return deviceId;
    }

    public static YsTimer parseTimerFrom(BlePacket bkp) {
        byte[] data = bkp.data;

        YsTimer timer = new YsTimer();
        timer.id = data[1];
        timer.status = data[2] == 0x01?true:false;
        timer.mode = data[3];
        timer.hour = data[4];
        timer.minutes = data[5];

        try {
            int end = Arrays.binarySearch(data,(byte) 0x00);
            timer.description = new String(Arrays.copyOfRange(data, 6, end), "gbk");
        } catch (Exception e) {
            e.printStackTrace();
            timer.description = "闹钟";
        }
        return timer;
    }

    public static BleData compsiteTimerRequest(int cid, int idx, boolean on, int mode, int hour, int min, String des, String mac) {
        ByteBuffer bf = ByteBuffer.allocate(16);
        bf.put((byte)cid);
        bf.put((byte)idx);
        int isOn = on?0x01:0x00;
        bf.put((byte)isOn);
        bf.put((byte)mode);
        bf.put((byte)hour);
        bf.put((byte)min);

        try {
            byte[] desBytes = des.getBytes("gbk");
            Log.e(TAG, "des length:" + desBytes.length);
//            bf.put((byte) desBytes.length);
            bf.put(desBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
            BleData data = new BleData(Commands.CMD_WARN_CONTROL, bf.array(), mac);

        return data;
    }
}
