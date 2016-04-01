package sctek.cn.ysbracelet.devicedata;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import java.util.Date;

import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

/**
 * Created by kang on 16-2-19.
 */
public class HeartRateData implements YsData {
    private static final String TAG = HeartRateData.class.getSimpleName();

    public Date time;
    public int rate;
    public String deviceId;
    public String type;

    private String tempTime = "2016-03-29";

    @Override
    public void setField(String field, String value) {

        if(XmlNodes.HeartRateNodes.NODE_RATE.equals(field)) {

        }
        else if(XmlNodes.HeartRateNodes.NODE_TIME.equals(field)) {

        }
        else if(XmlNodes.HeartRateNodes.NODE_TYPE.equals(field)) {

        }
        else if(XmlNodes.HeartRateNodes.NODE_DEVICE_ID.equals(field)) {

        }

    }

    @Override
    public Uri insert(ContentResolver cr) {
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.HeartRate.COLUMNS_NAME_RATE, rate);
        values.put(LocalDataContract.HeartRate.COLUMNS_NAME_DEVICE, deviceId);
        values.put(LocalDataContract.HeartRate.COLUMNS_NAME_TYPE, type);
        values.put(LocalDataContract.HeartRate.COLUMNS_NAME_TIME, tempTime);
        return cr.insert(LocalDataContract.HeartRate.CONTENT_URI, values);
    }

    @Override
    public int update(ContentResolver cr) {
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.HeartRate.COLUMNS_NAME_RATE, rate);
        values.put(LocalDataContract.HeartRate.COLUMNS_NAME_DEVICE, deviceId);
        values.put(LocalDataContract.HeartRate.COLUMNS_NAME_TYPE, type);
        values.put(LocalDataContract.HeartRate.COLUMNS_NAME_TIME, tempTime);
        return cr.update(LocalDataContract.HeartRate.CONTENT_URI, values
                , LocalDataContract.HeartRate.COLUMNS_NAME_TIME + "=" + tempTime
                + " AND " + LocalDataContract.HeartRate.COLUMNS_NAME_DEVICE + "=" + deviceId
                , null);
    }

    @Override
    public int delete(ContentResolver cr) {
        return cr.delete(LocalDataContract.HeartRate.CONTENT_URI
            , LocalDataContract.HeartRate.COLUMNS_NAME_TIME + "=" + tempTime
                + " AND " + LocalDataContract.HeartRate.COLUMNS_NAME_DEVICE + "=" + deviceId
            , null);
    }
}
