package sctek.cn.ysbracelet.utils;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;

import java.util.List;

import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.devicedata.Message;

/**
 * Created by kang on 16-5-27.
 */
public class GeTuiUtils {

    public static final String TAG = GeTuiUtils.class.getSimpleName();

    public static boolean setTags(Context context, List<DeviceInformation> devices) {
        PushManager pushManager = PushManager.getInstance();
        if(devices.size() == 0) {
            Tag t = new Tag();
            t.setName(" ");
            return pushManager.setTag(context, new Tag[]{t}) == PushConsts.SETTAG_SUCCESS;
        }

        Tag[] tags = new Tag[devices.size()];
        for(int i = 0; i < devices.size(); i++) {
            Tag tag = new Tag();
            tag.setName(devices.get(i).serialNumber);
            tags[i] = tag;
            Log.e(TAG, tag.getName());
        }
        return pushManager.setTag(context, tags) == PushConsts.SETTAG_SUCCESS;
    }

    public static boolean emptyTag(Context context) {
        Tag tag = new Tag();
        tag.setName(" ");
        return PushManager.getInstance().setTag(context, new Tag[]{tag}) == PushConsts.SETTAG_SUCCESS;
    }

    public static Message paserMessage(String msgStr) {
        if(msgStr == null || msgStr.length() == 0)
            return null;

        Message msg = new Message();
        String[] elements = msgStr.split("&");
        for(int i = 0; i < elements.length; i++) {
            String[] element = elements[i].split("=");
            if(element.length < 2)
                msg.setField(element[0], "");
            else
                msg.setField(element[0], element[1]);
        }
        return msg;
    }
}
