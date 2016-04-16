package sctek.cn.ysbracelet.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.user.UserManagerUtils;
import sctek.cn.ysbracelet.user.YsUser;

/**
 * Created by kang on 16-3-30.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter implements HttpConnectionWorker.ConnectionWorkListener{

    private final static String TAG = SyncAdapter.class.getSimpleName();

    public static final int SYNC_INTERVAL = 5*60;

    public static final int SYNC_TYPE_AUTO = 1;
    public static final int SYNC_TYPE_MANNUAL = 2;

    public static final String SYNC_EXTR = "cn.sctek.sync.extr";

    private ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSync) {
        super(context, autoInitialize, allowParallelSync);
        mContentResolver = context.getContentResolver();
    }
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        int type = extras.getInt(SYNC_EXTR);//同步的类型，手动，自动。
        Log.e(TAG, "onPerformSync:" + type + " " + authority);
        switch (type) {
            case SYNC_TYPE_AUTO:
                break;
            case SYNC_TYPE_MANNUAL:
                Looper.prepare();
                UserManagerUtils.sync(account.name, YsUser.getInstance().getLastSyncTime(), this);
                Looper.loop();
                break;
        }
    }

    @Override
    public void onWorkDone(int resCode) {
        Looper.myLooper().quit();
        Log.e(TAG, "resCode:" + resCode);
    }

    @Override
    public void onResult(YsData result) {
        result.insert(getContext().getContentResolver());
    }

    @Override
    public void onError(Exception e) {
        Looper.myLooper().quit();
        e.printStackTrace();
    }
}
