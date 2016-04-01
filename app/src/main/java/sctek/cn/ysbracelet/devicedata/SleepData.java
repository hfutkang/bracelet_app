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
public class SleepData implements YsData {

    private final static String TAG = SleepData.class.getSimpleName();

    public int quality;

    public int total;
    public int deep;
    public int shallow;
    public int awake;

    public String deviceId;

    public Date startTime;
    public Date endTime;

    public String tempTime = "2016-03-29";

    @Override
    public void setField(String field, String value) {
        if(XmlNodes.SleepNodes.NODE_QUALITY.equals(field)) {

        }
        else if(XmlNodes.SleepNodes.NODE_TIME.equals(field)) {

        }
    }

    @Override
    public Uri insert(ContentResolver cr) {

        ContentValues values = new ContentValues();
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEVICE, deviceId);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_TOTALE, total);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEEP, deep);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_SHALLOW, shallow);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_AWAKE, awake);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START, tempTime);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_END, tempTime);

        return cr.insert(LocalDataContract.Sleep.CONTENT_URI, values);
    }

    @Override
    public int update(ContentResolver cr) {
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEVICE, deviceId);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_TOTALE, total);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEEP, deep);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_SHALLOW, shallow);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_AWAKE, awake);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START, tempTime);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_END, tempTime);
        return cr.update(LocalDataContract.Sleep.CONTENT_URI
                    , values
                    , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEVICE + "=" + deviceId
                        + " AND " + LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + "=" + startTime
                        + " AND " + LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_END + "=" + endTime
                    , null);
    }

    @Override
    public int delete(ContentResolver cr) {

        return cr.delete(LocalDataContract.Sleep.CONTENT_URI
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEVICE + "=" + deviceId
                        + " AND " + LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + "=" + startTime
                        + " AND " + LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_END + "=" + endTime
                , null);
    }
}
