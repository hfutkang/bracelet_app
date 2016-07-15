package sctek.cn.ysbracelet.device;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.user.YsUser;
import sctek.cn.ysbracelet.utils.UrlUtils;

/**
 * Created by kang on 16-2-24.
 */
public class DeviceInformation implements YsData{

    private final static String TAG = DeviceInformation.class.getSimpleName();

    public String serialNumber;

    public String name;

    public String mac;

    public String imageName;

    public String sex = "Female";

    public int age = 1;

    public int weight = 75;

    public int height = 10;

    public int power = 100;

    public DeviceInformation() {
        serialNumber = null;
        name = null;
        mac = null;
    }

    public DeviceInformation(String sn, String mac, String name) {
        serialNumber = sn;
        this.mac = mac;
        this.name = name;
        this.imageName = YsUser.getInstance().getName() + "_" + sn + ".png";
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

    public String getImageName() {
        return imageName;
    }

    public String getImagePath() { return UrlUtils.IMAGE_PATH_BASE + imageName; };

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
            imageName = value;
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
        Log.e(TAG, "insert:" + serialNumber);
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_SN, serialNumber);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_MAC, mac);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_NAME, name);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_HEIGHT, height);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_SEX, sex);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_AGE, age);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_WEIGHT, weight);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_POWER, power);
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_IMAGE_PATH, imageName);
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
        values.put(LocalDataContract.DeviceInfo.COLUMNS_NAME_IMAGE_PATH, imageName);
        return cr.update(LocalDataContract.DeviceInfo.CONTENT_URI
                , values
                , LocalDataContract.DeviceInfo.COLUMNS_NAME_SN + "=" + "'" + serialNumber + "'"
                , null);
    }

    @Override
    public int delete(ContentResolver cr) {
        return cr.delete(LocalDataContract.DeviceInfo.CONTENT_URI
                , LocalDataContract.DeviceInfo.COLUMNS_NAME_SN + "=?"
                , new String[]{serialNumber});
    }
}
