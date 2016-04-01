package sctek.cn.ysbracelet.device;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

/**
 * Created by kang on 16-2-24.
 */
public class DeviceInformation implements YsData{

    private final static String TAG = DeviceInformation.class.getSimpleName();

    private String serialNumber;

    private String name;

    private String mac;

    private String imagePath;

    private String sex;

    private int age;

    private int weight;

    private int height;

    private int power;

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

    public int getPower() { return power; }

    public int getWeight() { return weight; }

    public int getHeight() { return height; }

    public String getImagePath() { return imagePath; }

    public String getSex() { return sex; }

    public int getAge() { return  age; }

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
        else if(field.equals(XmlNodes.DeviceNodes.NODE_HEIGHT)) {
            height = Integer.parseInt(value);
        }
        else if(field.equals(XmlNodes.DeviceNodes.NODE_WEIGHT)) {
            weight = Integer.parseInt(value);
        }
        else if(field.equals(XmlNodes.DeviceNodes.NODE_POWER)) {
            power = Integer.parseInt(value);
        }
        else if(field.equals(XmlNodes.DeviceNodes.NODE_IMAGE)) {
            imagePath = value;
        }
        else if(field.equals(XmlNodes.DeviceNodes.NODE_SEX)) {
            sex = value;
        }
        else if(field.equals(XmlNodes.DeviceNodes.NODE_AGE)) {
            age = Integer.parseInt(value);
        }
    }

    @Override
    public Uri insert(ContentResolver cr) {
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_SN, serialNumber);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_MAC, mac);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_NAME, name);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_HEIGHT, height);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_SEX, sex);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_AGE, age);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_WEIGHT, weight);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_POWER, power);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_IMAGE_PATH, imagePath);
        return cr.insert(LocalDataContract.DeviceInfo.CONTENT_URI, values);
    }

    @Override
    public int update(ContentResolver cr) {
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_SN, serialNumber);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_MAC, mac);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_NAME, name);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_HEIGHT, height);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_SEX, sex);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_AGE, age);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_WEIGHT, weight);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_POWER, power);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_IMAGE_PATH, imagePath);
        return cr.update(LocalDataContract.DeviceInfo.CONTENT_URI
                , values
                , LocalDataContract.DeviceInfo.COLUMNS_NAME_SN + "=" + serialNumber
                , null);
    }

    @Override
    public int delete(ContentResolver cr) {
        return cr.delete(LocalDataContract.DeviceInfo.CONTENT_URI
                , LocalDataContract.DeviceInfo.COLUMNS_NAME_SN + "=" + serialNumber
                , null);
    }
}
