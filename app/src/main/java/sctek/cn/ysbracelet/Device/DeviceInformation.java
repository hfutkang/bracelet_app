package sctek.cn.ysbracelet.Device;

import android.util.Log;

import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.braceletdata.YsData;
import sctek.cn.ysbracelet.http.XmlNodes;

/**
 * Created by kang on 16-2-24.
 */
public class DeviceInformation implements YsData{

    private final static String TAG = DeviceInformation.class.getSimpleName();

    private String serialNumber;

    private String name;

    private String mac;

    public DeviceInformation() {
        serialNumber = null;
        name = null;
        mac = null;
    }

    public DeviceInformation(String sn, String mac, String name) {
        serialNumber = sn;
        this.mac = mac;
        this.name = name;
    }

    public void setName(String name) {
        this.name =name;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setSerialNumber(String sn) {
        serialNumber = sn;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getName() {
        return name;
    }

    public String getMac() {
        return mac;
    }


    @Override
    public void setField(String field, String value) {
        if(BleUtils.DEBUG) Log.e(TAG, "setFeild");

        if(field.equals(XmlNodes.DeviceNodes.NODE_ID)) {
            serialNumber = value;
        }
        else if(field.equals(XmlNodes.DeviceNodes.NODE_NAME)) {
            name = value;
        }
        else if(field.equals(XmlNodes.DeviceNodes.NODE_MAC)) {
            mac = value;
        }
    }
}
