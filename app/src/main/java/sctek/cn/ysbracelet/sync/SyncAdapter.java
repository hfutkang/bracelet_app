package sctek.cn.ysbracelet.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    public static final int SYNC_TYPE_MANUAL_ALL = 2;
    public static final int SYNC_TYPE_MANUAL_SINGLE = 3;

    private int syncMode;

    public static final String SYNC_EXTR_MODE = "cn.sctek.sync.extra";
    public static final String SYNC_EXTR_DEVICE = "cn.sctek.sync.id";

    private Executor mExecutor;


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mExecutor = Executors.newFixedThreadPool(5);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSync) {
        super(context, autoInitialize, allowParallelSync);
        mExecutor = Executors.newFixedThreadPool(5);
    }
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
         syncMode = extras.getInt(SYNC_EXTR_MODE);//同步的类型，手动，自动。
        String id = extras.getString(SYNC_EXTR_DEVICE);
        Log.e(TAG, "onPerformSync:" + syncMode + " " + authority);
        Log.e(TAG, "thread id===========" + Thread.currentThread().getId());
        Looper.prepare();
        switch (syncMode) {
            case SYNC_TYPE_AUTO:
                UserManagerUtils.syncForUser(account.name, YsUser.getInstance().getLastSyncTime(), this);
                Looper.loop();
                break;
            case SYNC_TYPE_MANUAL_ALL:
                UserManagerUtils.syncForUser(account.name, YsUser.getInstance().getLastSyncTime(), this);
                Looper.loop();
                break;
            case SYNC_TYPE_MANUAL_SINGLE:
                UserManagerUtils.syncForDeivce(id, this);
                Looper.loop();
                break;
        }
    }

    @Override
    public void onWorkDone(int resCode) {
        Looper.myLooper().quit();
        Log.e(TAG, "resCode:" + resCode);
        if(syncMode == SYNC_TYPE_AUTO || syncMode == SYNC_TYPE_MANUAL_ALL) {
            YsUser.getInstance().updateSyncTime(getContext().getContentResolver());
        }
    }

    @Override
    public void onResult(final YsData result) {
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        result.insert(getContext().getContentResolver());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    @Override
    public void onError(Exception e) {
        Looper.myLooper().quit();
        e.printStackTrace();
    }
}
