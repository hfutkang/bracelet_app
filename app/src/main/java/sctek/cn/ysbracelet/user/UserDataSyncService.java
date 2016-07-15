package sctek.cn.ysbracelet.user;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.sync.SyncAdapter;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker;

/**
 * Created by kang on 16-3-28.
 */
public class UserDataSyncService extends Service {

    public static final String TAG = UserDataSyncService.class.getSimpleName();

    private String userName;
    private String startTime;

    private ContentResolver mContentResolver;
    private static SyncAdapter mSyncAdapter = null;
    private static final Object mSyncAdapterLock = new Object();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return mSyncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContentResolver = getContentResolver();
        Cursor cursor = mContentResolver.query(LocalDataContract.UserInfo.CONTENT_URI
                , new String[]{LocalDataContract.UserInfo.COLUMNS_NAME_NAME, LocalDataContract.UserInfo.COLUMNS_NAME_LAST_SYNC_TIME}
                , null
                , null
                , null);

        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex(LocalDataContract.UserInfo.COLUMNS_NAME_NAME);
            int timeIndex = cursor.getColumnIndex(LocalDataContract.UserInfo.COLUMNS_NAME_LAST_SYNC_TIME);

            userName = cursor.getString(nameIndex);
            startTime = cursor.getString(timeIndex);
        }

        cursor.close();

        synchronized (mSyncAdapterLock) {
            if(mSyncAdapter == null) {
                mSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mPullThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    private PullThread mPullThread = new PullThread();

    private class PullThread extends Thread implements HttpConnectionWorker.ConnectionWorkListener{

        @Override
        public void run() {
            Looper.prepare();
            if(userName != null)
                UserManagerUtils.syncForUser(userName, startTime, this);
            Looper.loop();
            super.run();
        }

        @Override
        public void onWorkDone(int resCode) {
            Looper.myLooper().quit();
        }

        @Override
        public void onResult(YsData result) {
            result.insert(mContentResolver);
        }

        @Override
        public void onError(Exception e) {
            Looper.myLooper().quit();
        }
    }

}
