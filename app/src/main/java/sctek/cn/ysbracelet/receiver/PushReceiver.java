package sctek.cn.ysbracelet.receiver;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.devicedata.Message;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.utils.GeTuiUtils;

/**
 * Created by kang on 16-5-25.
 */
public class PushReceiver extends BroadcastReceiver{
    public static final String TAG = PushReceiver.class.getSimpleName();

    public static final String SOS_MSG = "sos";
    public static final String LOW_POWER_MSG = "low_power";

    public static final int NOTIFCATION_ID = 10001;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, intent.getAction());
        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:

                String cid = bundle.getString("clientid");
                // TODO:处理cid返回
                break;
            case PushConsts.GET_MSG_DATA:

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                Log.e(TAG, taskid + " " + messageid);
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String data = new String(payload);
                    Log.e(TAG, data);
                    Message msg = GeTuiUtils.paserMessage(data);
                    try {
                        showNotification(context, msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(Context context, Message msg) throws SQLException {

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.DeviceInfo.CONTENT_URI
                , new String[]{LocalDataContract.DeviceInfo.COLUMNS_NAME_NAME}
                , LocalDataContract.DeviceInfo.COLUMNS_NAME_SN + "=?"
                , new String[]{msg.deviceId}
                , null);
        String name = null;
        int i = cursor.getCount();
        if(!cursor.moveToNext()) {
            Log.e(TAG, "none");
            return;
        }
        name = cursor.getString(0);
        cursor.close();

        msg.insert(context.getContentResolver());

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        builder.setLargeIcon(bitmap);
        builder.setSmallIcon(R.drawable.logo);

        String msgBody = null;
        if(SOS_MSG.equals(msg.type)) {
            msgBody = String.format(context.getString(R.string.sos_msg_notify), name, msg.time);

            builder.setContentTitle(context.getString(R.string.sos_notify_title));
            builder.setContentText(msgBody);
        }
        else if(LOW_POWER_MSG.equals(msg.type)){
            msgBody = String.format(context.getString(R.string.low_power_msg), name);
            builder.setContentTitle(context.getString(R.string.low_power_notify_title));
            builder.setContentText(msgBody);
        }

        builder.setDefaults(Notification.DEFAULT_VIBRATE);

        notificationManager.notify(NOTIFCATION_ID, builder.build());

    }

}
