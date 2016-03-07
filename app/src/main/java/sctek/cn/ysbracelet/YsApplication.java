package sctek.cn.ysbracelet;

import android.app.Application;

import sctek.cn.ysbracelet.ble.BluetoothLeManager;
import sctek.cn.ysbracelet.user.YsUser;

/**
 * Created by kang on 16-2-25.
 */
public class YsApplication extends Application{

    private static final String TAG = YsApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        YsUser.getInstance().loadUserInfo(this);
        BluetoothLeManager.getInstance().initial(this);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();
            }
        });

    }
}
