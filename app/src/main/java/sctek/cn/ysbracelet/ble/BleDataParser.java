package sctek.cn.ysbracelet.ble;

import sctek.cn.ysbracelet.devicedata.HeartRateData;
import sctek.cn.ysbracelet.devicedata.SleepData;
import sctek.cn.ysbracelet.devicedata.SportsData;

/**
 * Created by kang on 16-3-7.
 */
public class BleDataParser {

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
}
