package sctek.cn.ysbracelet.user;

import android.util.Log;

import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.http.HttpConnectionWorker;
import sctek.cn.ysbracelet.http.HttpConnectionWorker.ConnectionWorkeListener;
import sctek.cn.ysbracelet.http.YsHttpConnection;
import sctek.cn.ysbracelet.utils.UrlUtils;

/**
 * Created by kang on 16-2-24.
 */
public class UserManagerUtils {

    private final static String TAG = UserManagerUtils.class.getSimpleName();

    public static void login(String name, String pw, ConnectionWorkeListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "login");

        String url = UrlUtils.compositeLoginUrl(name, pw);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
    }

    public static void logout() {}

    public static void register(String name, String pw, ConnectionWorkeListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "register");

        String url = UrlUtils.compositeRegisterUrl(name, pw);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();

    }

    public static void updateUserInfo(String name, String sex, int age, int height, int weight
            , ConnectionWorkeListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "updateUserInfo");

        String url = UrlUtils.compositeUpdateUserInfoUrl(name, sex, age, height, weight);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
    }

    public static void addDevice(String uName, String sn, String name, ConnectionWorkeListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "addDevice");

        String url = UrlUtils.compositeAddDeviceUrl(uName, sn, name);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
    }

    public static void delDevice(String uName, String sn, ConnectionWorkeListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "delDevice");

        String url = UrlUtils.compositeDeleteDeviceUrl(uName, sn);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
    }

    public static void getDeviceList(String uName, ConnectionWorkeListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "getDeviceList");

        String url = UrlUtils.compositeGetDevicesUrl(uName);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
    }


}
