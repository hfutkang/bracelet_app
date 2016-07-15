package sctek.cn.ysbracelet.devicedata;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

/**
 * Created by kang on 16-5-4.
 */
public class Message implements YsData{

    public String deviceId;
    public String name;
    public String type;
    public String time;
    public String message;

    @Override
    public void setField(String field, String value) {

        if(XmlNodes.MessageNodes.NODE_DEVICE_ID.equals(field)) {
            deviceId = value;
        }
        else if(XmlNodes.MessageNodes.NODE_TIME.equals(field)) {
            time = value;
        }
        else if(XmlNodes.MessageNodes.NODE_TYPE.equals(field)) {
            type = value;
        }
        else if(XmlNodes.MessageNodes.NODE_MSG.equals(field)) {
            message = value;
        }
    }

    @Override
    public Uri insert(ContentResolver cr) {
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.Message.COLUMNS_NAME_DEVICE, deviceId);
        values.put(LocalDataContract.Message.COLUMNS_NAME_TYPE, type);
        values.put(LocalDataContract.Message.COLUMNS_NAME_TIME, time);
        values.put(LocalDataContract.Message.COLUMNS_NAME_MESSAGE, message);
        return cr.insert(LocalDataContract.Message.CONTENT_URI, values);
    }

    @Override
    public int update(ContentResolver cr) {
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.Message.COLUMNS_NAME_DEVICE, deviceId);
        values.put(LocalDataContract.Message.COLUMNS_NAME_TYPE, type);
        values.put(LocalDataContract.Message.COLUMNS_NAME_TIME, time);
        values.put(LocalDataContract.Message.COLUMNS_NAME_MESSAGE, message);
        return cr.update(LocalDataContract.Message.CONTENT_URI, values
                , LocalDataContract.Message.COLUMNS_NAME_TIME + "=" + "'" + time + "'"
                        + " AND " + LocalDataContract.Message.COLUMNS_NAME_DEVICE + "=" + "'" + deviceId + "'"
                , null);
    }

    @Override
    public int delete(ContentResolver cr) {
        return cr.delete(LocalDataContract.Message.CONTENT_URI
                , LocalDataContract.Message.COLUMNS_NAME_TIME + "=" + "'" + time + "'"
                        + " AND " + LocalDataContract.Message.COLUMNS_NAME_DEVICE + "=" + "'" + deviceId + "'"
                , null);
    }
}
