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
    }

    public static void sync(String userName, String startTime, ConnectionWorkListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "sync");

        String url = UrlUtils.compositeSyncUrl(userName, startTime);
        YsHttpConnection mConnection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        new HttpConnectionWorker(mConnection, listener).start();
    }

    public static void logout() {}

    public static void register(String name, String pw, ConnectionWorkListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "register");

        String url = UrlUtils.compositeRegisterUrl(name, pw);
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

    public static void addDevice(String uName, String sn, String name, ConnectionWorkListener listener) {
        if(BleUtils.DEBUG) Log.e(TAG, "addDevice");

        String url = UrlUtils.compositeAddDeviceUrl(uName, sn, name);
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

    public static void createSyncAccount(Context context) {

        Account account = YsUser.getInstance().getAccount();
        AccountManager accountManager = AccountManager.get(context);

        if(accountManager.addAccountExplicitly(account, null, null)) {
            Bundle bundle = new Bundle();
            bundle.putInt(SyncAdapter.SYNC_EXTR, SyncAdapter.SYNC_TYPE_AUTO);
            ContentResolver.addPeriodicSync(account, LocalDataContract.AUTHORITY, bundle, SyncAdapter.SYNC_INTERVAL);

            bundle.putInt(SyncAdapter.SYNC_EXTR, SyncAdapter.SYNC_TYPE_MANNUAL);
            ContentResolver.requestSync(account, LocalDataContract.AUTHORITY, bundle);
        }
    }


}
