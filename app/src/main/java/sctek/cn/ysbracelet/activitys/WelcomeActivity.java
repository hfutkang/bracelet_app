package sctek.cn.ysbracelet.activitys;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.igexin.sdk.PushManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.sync.SyncAdapter;
import sctek.cn.ysbracelet.user.YsUser;

public class WelcomeActivity extends AppCompatActivity {

    private final static String TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(YsUser.getInstance().isLogined()) {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    if(YsUser.getInstance().getDeviceCount() != 0) {
                        Account account = YsUser.getInstance().getAccount();
                        Bundle bundle = new Bundle();
                        ContentResolver.setSyncAutomatically(account, LocalDataContract.AUTHORITY, true);
                        bundle.putInt(SyncAdapter.SYNC_EXTR_MODE, SyncAdapter.SYNC_TYPE_MANUAL_ALL);
                        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                        ContentResolver.requestSync(account, LocalDataContract.AUTHORITY, bundle);
                    }
                    PushManager.getInstance().initialize(getApplicationContext());
                    finish();
                }
                else {
//                    try {
//                        loadTestData();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 2000);
    }

    private void loadTestData() throws IOException {
        File dataCache = getExternalCacheDir();
        Log.e(TAG, dataCache.toString());
        String[] files = getAssets().list("testdata");
        for (String fn : files) {
            Log.e(TAG, fn);
            InputStream inputStream = getAssets().open("testdata/" + fn);
            OutputStream outputStream = new FileOutputStream(new File(dataCache, fn));
            byte[] buffer = new byte[1024];
            int n = 0;
            while ((n = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, n);
            }

        }
    }

}
