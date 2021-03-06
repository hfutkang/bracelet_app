package sctek.cn.ysbracelet.user;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.http.YsHttpConnection;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.sync.SyncAdapter;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker.ConnectionWorkListener;
import sctek.cn.ysbracelet.utils.UrlUtils;

/**
 * Created by kang on 16-2-24.
 */
public class UserManagerUtils {

    private final static String TAG = UserManagerUtils.class.getSimpleName();

    public static void login(String name, String pw, ConnectionWorkListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "login");

        String url = UrlUtils.compositeLoginUrl(name, pw);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
//                .setTestXmlFile("/storage/emulated/0/Android/data/sctek.cn.ysbracelet/cache/login.xml").start();
    }

    public static void syncForUser(String userName, String startTime, ConnectionWorkListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "syncForUser");

        String url = UrlUtils.compositeUserSyncUrl(userName, startTime);
        Log.e(TAG, url);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
//                .setTestXmlFile("/storage/emulated/0/Android/data/sctek.cn.ysbracelet/cache/syncdata.xml").start();
    }

    public static void syncForDeivce(String deviceId, ConnectionWorkListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "syncForDeivce");

        String url = UrlUtils.compositeUserSyncUrl(deviceId);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
    }

    public static void logout() {}

    public static void register(String name, String pw, String email, String vCode, ConnectionWorkListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "register:" + name + " " + pw);

        String url = UrlUtils.compositeRegisterUrl(name, pw, email, vCode);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();

    }

    public static void getVerifyCode(String name, ConnectionWorkListener listener) {
        String url = UrlUtils.compositeGetVerifyCodeUrl(name);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
    }

    public static void changePassword(String name, String newPw, String vCode, ConnectionWorkListener listener) {
        String url = UrlUtils.compositeChangePasswordUrl(name, newPw, vCode);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
    }

    public static void updateUserInfo(String name, String sex, int age, int height, int weight
            , ConnectionWorkListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "updateUserInfo");

        String url = UrlUtils.compositeUpdateUserInfoUrl(name, sex, age, height, weight);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
    }

    public static void addDevice(String uName, String sn, String mac, String name, String sex, int age
                                 , int height, int weight, ConnectionWorkListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "addDevice");

        String url = UrlUtils.compositeAddDeviceUrl(uName, sn, mac, name, sex, age, height, weight);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
    }

    public static void updateDeviceInfo(String uName, String sn, String name, String sex, int age
            , int height, int weight, ConnectionWorkListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "addDevice");

        String url = UrlUtils.compositeUpdateDeviceUrl(uName, sn, name, sex, age, height, weight);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
    }

    public static void delDevice(String uName, String sn, ConnectionWorkListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "delDevice");

        String url = UrlUtils.compositeDeleteDeviceUrl(uName, sn);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
    }

    public static void getDeviceList(String uName, ConnectionWorkListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "getDeviceList");

        String url = UrlUtils.compositeGetDevicesUrl(uName);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
    }

    public static void startMeasureHeartRate(String sn, ConnectionWorkListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "startMeasureHeartRate");
        String url = UrlUtils.compositeMeasureHRateUrl(sn);
        YsHttpConnection connection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(connection, listener).start();
    }

    public static void createSyncAccount(Context context) {
        if(BleUtils.DEBUG) Log.e(TAG, "createSyncAccount");
        Account account = YsUser.getInstance().getAccount();
        AccountManager accountManager = AccountManager.get(context);

        if(accountManager.addAccountExplicitly(account, null, null)) {
            if(BleUtils.DEBUG) Log.e(TAG, "createSyncAccount add account ok");
            Bundle bundle = new Bundle();
            bundle.putInt(SyncAdapter.SYNC_EXTR_MODE, SyncAdapter.SYNC_TYPE_AUTO);
            ContentResolver.addPeriodicSync(account, LocalDataContract.AUTHORITY, bundle, SyncAdapter.SYNC_INTERVAL);
            ContentResolver.setIsSyncable(account, LocalDataContract.AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(account, LocalDataContract.AUTHORITY, true);
            bundle.putInt(SyncAdapter.SYNC_EXTR_MODE, SyncAdapter.SYNC_TYPE_MANUAL_ALL);
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            ContentResolver.requestSync(account, LocalDataContract.AUTHORITY, bundle);
        }
    }

    public static void getLatestPositionForUser(String name, ConnectionWorkListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "getLatestPosition");

        String url = UrlUtils.compositeGetLatestLocationForUserUrl(name);
        YsHttpConnection connection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(connection, listener).start();
    }


}
