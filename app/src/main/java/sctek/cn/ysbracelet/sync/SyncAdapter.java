package sctek.cn.ysbracelet.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by kang on 16-3-30.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final int SYNC_INTERVAL = 5*60;

    public static final int SYNC_TYPE_AUTO = 1;
    public static final int SYNC_TYPE_MANNUAL = 2;

    public static final String SYNC_EXTR_AUTO = "auto";
    public static final String SYNC_EXTR_MANNUAL = "mannual";

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

    }
}
