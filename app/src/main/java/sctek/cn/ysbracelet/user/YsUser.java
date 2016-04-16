package sctek.cn.ysbracelet.user;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.Date;
import java.util.List;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.device.DevicesManager;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

/**
 * Created by kang on 16-2-23.
 */
public class YsUser implements YsData{

    private final static String TAG = YsUser.class.getSimpleName();

    public static final String ACCOUNT_TYPE = "sctek.cn.ysbracelet";

    private final static String SEX_DEFAULT = "Female";
    private final static int AGE_DEFAULT = 18;
    private final static int HEIGHT_DEFAULT = 175;
    private final static int WEIGHT_DEFAULT = 120;

    private String name;
    private String password;
    private String lastSyncTime;
    private String sex;
    private int age;
    private int height;
    private int weight;
    private String gravatar;

    private Date syncTime;

    private DevicesManager mDevicesManager;

    private static YsUser mIntance;

    public static YsUser getInstance() {
        if(mIntance == null) {
            mIntance = new YsUser();
        }
        return mIntance;
    }

    private YsUser() {
        mDevicesManager = new DevicesManager();
    }

    public void loadUserInfo(Context context) {
        if(BleUtils.DEBUG) Log.e(TAG, "loadUserInfo");

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.UserInfo.CONTENT_URI
                                , null, null, null, null);

        while (cursor.moveToNext()) {

            int nameIndex = cursor.getColumnIndex(LocalDataContract.UserInfo.COLUMNS_NAME_NAME);
            int passwordIndex = cursor.getColumnIndex(LocalDataContract.UserInfo.COLUMNS_NAME_PASSWORD);
            int syncTimeIndex = cursor.getColumnIndex(LocalDataContract.UserInfo.COLUMNS_NAME_LAST_SYNC_TIME);

            name = cursor.getString(nameIndex);
            password = cursor.getString(passwordIndex);
            lastSyncTime = cursor.getString(syncTimeIndex);
            Log.e(TAG, lastSyncTime);

            loadDevices(context);
        }

    }

    private void loadDevices(Context context) {
        if(name == null)
            return;
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.DeviceInfo.CONTENT_URI,
                            null,null,null,null);
        while (cursor.moveToNext()) {
            DeviceInformation device = new DeviceInformation();
            int serialNumberIndex = cursor.getColumnIndex(LocalDataContract.DeviceInfo.COLUMNS_NAME_SN);
            int macIndex = cursor.getColumnIndex(LocalDataContract.DeviceInfo.COLUMNS_NAME_MAC);
            int nameIndex = cursor.getColumnIndex(LocalDataContract.DeviceInfo.COLUMNS_NAME_NAME);
            int ageIndex = cursor.getColumnIndex(LocalDataContract.DeviceInfo.COLUMNS_NAME_AGE);
            int sexIndex = cursor.getColumnIndex(LocalDataContract.DeviceInfo.COLUMNS_NAME_SEX);
            int heightIndex = cursor.getColumnIndex(LocalDataContract.DeviceInfo.COLUMNS_NAME_HEIGHT);
            int weightIndex = cursor.getColumnIndex(LocalDataContract.DeviceInfo.COLUMNS_NAME_WEIGHT);
            int powerIndex = cursor.getColumnIndex(LocalDataContract.DeviceInfo.COLUMNS_NAME_POWER);
            int imagePathIndex = cursor.getColumnIndex(LocalDataContract.DeviceInfo.COLUMNS_NAME_IMAGE_PATH);

            device.serialNumber = cursor.getString(serialNumberIndex);
            device.mac = cursor.getString(macIndex);
            device.name = cursor.getString(nameIndex);
            device.age = cursor.getInt(ageIndex);
            device.sex = cursor.getString(sexIndex);
            device.height = cursor.getInt(heightIndex);
            device.weight = cursor.getInt(weightIndex);
            device.power = cursor.getInt(powerIndex);
            device.imagePath = cursor.getString(imagePathIndex);

            mDevicesManager.addDevice(device);
        }
    }

    public Account getAccount() {
        return new Account(name, ACCOUNT_TYPE);
    }

    public boolean isLogined() {
        if(BleUtils.DEBUG) Log.e(TAG, "login");

        return name != null;
    }

    public void updateUserInfo(Context context) {
        if(BleUtils.DEBUG) Log.e(TAG, "updateUserInfo");

        insert(context.getContentResolver());

    }

    public void addDevice(DeviceInformation device) {
        mDevicesManager.addDevice(device);
    }

    public void deleteDevice(DeviceInformation device) {
        mDevicesManager.deleteDevice(device);
    }

    public int getDeviceCount() {
        return mDevicesManager.getCount();
    }

    public List<DeviceInformation> getDevices() { return mDevicesManager.getDevices(); }

    public DeviceInformation getDevice(int index) {
        return mDevicesManager.getDevice(index);
    }

    public DeviceInformation getDevice(String id) {
        return mDevicesManager.getDevice(id);
    }

    public DeviceInformation getDeviceByMac(String mac) { return mDevicesManager.getDeviceByMac(mac);}

    public void clear() {
        mDevicesManager.clear();
    }

    public void setName(String name) { this.name = name; }

    public void setPassword(String pw) { this.password = pw; }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() { return name ;}

    public String getPassword() { return  password;}

    public String getSex() { return  sex;}

    public int getAge() { return  age;}

    public int getHeight() { return height;}

    public int getWeight() { return  weight;}

    public String getLastSyncTime() { return lastSyncTime; }

    @Override
    public void setField(String field, String value) {
        if(BleUtils.DEBUG) Log.e(TAG, "setField");

        if(field.equals(XmlNodes.UserNodes.NODE_NAME)) {
            this.name = value;
        }
        else if(field.equals(XmlNodes.UserNodes.NODE_PASSWORD)) {
            this.password = value;
        }
        else if(field.equals(XmlNodes.UserNodes.NODE_SEX)) {
            this.sex = value;
        }
        else if(field.equals(XmlNodes.UserNodes.NODE_AGE)) {
            this.age = Integer.parseInt(value);
        }
        else if(field.equals(XmlNodes.UserNodes.NODE_HEIGTH)) {
            this.height = Integer.parseInt(value);
        }
        else if(field.equals(XmlNodes.UserNodes.NODE_WEIGHT)) {
            this.weight = Integer.parseInt(value);
        }
    }

    @Override
    public Uri insert(ContentResolver cr) {
        Log.e(TAG, "insert");
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.UserInfo.COLUMNS_NAME_NAME, name);
        values.put(LocalDataContract.UserInfo.COLUMNS_NAME_PASSWORD, password);
        lastSyncTime = new YsDateManager(YsDateManager.DATE_FORMAT_SECOND).getCurrentDate();
        values.put(LocalDataContract.UserInfo.COLUMNS_NAME_LAST_SYNC_TIME, lastSyncTime);
        return cr.insert(LocalDataContract.UserInfo.CONTENT_URI, values);
    }

    @Override
    public int update(ContentResolver cr) {
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.UserInfo.COLUMNS_NAME_NAME, name);
        values.put(LocalDataContract.UserInfo.COLUMNS_NAME_PASSWORD, password);
        lastSyncTime = new YsDateManager(YsDateManager.DATE_FORMAT_SECOND).getCurrentDate();
        values.put(LocalDataContract.UserInfo.COLUMNS_NAME_LAST_SYNC_TIME, lastSyncTime);
        return cr.update(LocalDataContract.UserInfo.CONTENT_URI
                , values
                , LocalDataContract.UserInfo.COLUMNS_NAME_NAME + "=" + name
                , null);
    }

    @Override
    public int delete(ContentResolver cr) {
        return cr.delete(LocalDataContract.UserInfo.CONTENT_URI
                , LocalDataContract.UserInfo.COLUMNS_NAME_NAME + "=" + name
                , null);
    }
}
