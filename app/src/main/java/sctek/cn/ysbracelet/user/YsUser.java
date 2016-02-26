package sctek.cn.ysbracelet.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import sctek.cn.ysbracelet.Device.DeviceInformation;
import sctek.cn.ysbracelet.Device.DevicesManager;
import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.braceletdata.YsData;
import sctek.cn.ysbracelet.http.XmlNodes;

/**
 * Created by kang on 16-2-23.
 */
public class YsUser implements YsData{

    private final static String TAG = YsUser.class.getSimpleName();

    private String name;
    private String password;
    private String sex;
    private int age;
    private int height;
    private int weight;

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
        if(BleUtils.DEBUG) Log.e(TAG, "init");

        SharedPreferences preferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        name = preferences.getString("name", null);
        password = preferences.getString("pw", null);
        sex = preferences.getString("sex", null);
        age = preferences.getInt("age", 0);
        height = preferences.getInt("height", 0);
        weight = preferences.getInt("weight", 0);
    }

    public boolean isLogined() {
        if(BleUtils.DEBUG) Log.e(TAG, "login");

        return name != null;
    }

    public void updateUserInfo(Context context) {
        if(BleUtils.DEBUG) Log.e(TAG, "updateUserInfo");

        SharedPreferences preferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.putString("pw", password);
        editor.putString("sex",sex);
        editor.putInt("age", age);
        editor.putInt("height", height);
        editor.putInt("weight", weight);
        editor.commit();

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

    public DeviceInformation getDevice(int index) {
        return mDevicesManager.getDevice(index);
    }

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
}
