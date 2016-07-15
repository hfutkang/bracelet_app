package sctek.cn.ysbracelet.ble;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.List;

import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.thread.BleDataSendThread;
import sctek.cn.ysbracelet.user.YsUser;

/**
 * Created by kang on 16-6-28.
 */
public class FenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = FenceBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(BluetoothLeService.ACTION_DISCONNECTED)) {
            sendMotorControlCmd(context, (byte)0xFF);
        }
        else if(action.equals(BluetoothLeService.ACTION_CONNECTED)) {

        }
    }

    private void sendMotorControlCmd(Context context, byte mode) {
        List<DeviceInformation> devices = YsUser.getInstance().getDevices();
        SharedPreferences sp = context.getSharedPreferences("sctek.cn.ysbracelet.fence", Context.MODE_PRIVATE);
        for (DeviceInformation di : devices) {
            if (sp.getBoolean(di.mac, false)) {
                BleData data = new BleData(Commands.CMD_MOTOR_CONTROL
                        , new byte[]{mode, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x04}
                        , di.getMac());
                new BleDataSendThread(data, new BleDataSendThread.DataSendStateListener() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onTimeOut() {

                    }

                    @Override
                    public void onError() {

                    }
                }, null).start();
            }
        }
    }
}
