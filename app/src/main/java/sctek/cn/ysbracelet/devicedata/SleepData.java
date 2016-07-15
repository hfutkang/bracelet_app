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

    public int total;//单位分
    public int deep;
    public int shallow;
    public int wake;

    public String deviceId;

    public Date startTime;
    public Date endTime;

    public String tempStartTime = "2016-03-29 23:01";
    public String tempEndTime = "2016-03-30 08:00";

    @Override
    public void setField(String field, String value) {
        if(XmlNodes.SleepNodes.NODE_QUALITY.equals(field)) {

        }
        else if(XmlNodes.SleepNodes.NODE_TIME.equals(field)) {

        }
        else if(XmlNodes.SleepNodes.NODE_START.equals(field)) {
            tempStartTime = value;
        }
        else if(XmlNodes.SleepNodes.NODE_END.equals(field)) {
            tempEndTime = value;
        }
        else if(XmlNodes.SleepNodes.NODE_DEEP.equals(field)) {
            deep = Integer.parseInt(value);
        }
        else if(XmlNodes.SleepNodes.NODE_SHALLOW.equals(field)) {
            shallow = Integer.parseInt(value);
        }
        else if(XmlNodes.SleepNodes.NODE_WAKE.equals(field)) {
            wake = Integer.parseInt(value);
        }
        else if(XmlNodes.SleepNodes.NODE_DEVICE_ID.equals(field)) {
            deviceId = value;
        }
        else if(XmlNodes.SleepNodes.NODE_TOTAL.equals(field)) {
            total = Integer.parseInt(value);
        }
    }

    @Override
    public Uri insert(ContentResolver cr) {

        ContentValues values = new ContentValues();
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEVICE, deviceId);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_TOTALE, total);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEEP, deep);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_SHALLOW, shallow);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_WAKE, wake);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START, tempStartTime);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_END, tempEndTime);

        return cr.insert(LocalDataContract.Sleep.CONTENT_URI, values);
    }

    @Override
    public int update(ContentResolver cr) {
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEVICE, deviceId);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_TOTALE, total);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEEP, deep);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_SHALLOW, shallow);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_WAKE, wake);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START, tempStartTime);
        values.put(LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_END, tempEndTime);
        return cr.update(LocalDataContract.Sleep.CONTENT_URI
                    , values
                    , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEVICE + "=" + deviceId
                        + " AND " + LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + "=" + "" + startTime + "'"
                        + " AND " + LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_END + "=" + "'" + endTime + "'"
                    , null);
    }

    @Override
    public int delete(ContentResolver cr) {

        return cr.delete(LocalDataContract.Sleep.CONTENT_URI
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEVICE + "=" + deviceId
                        + " AND " + LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + "=" + "'" + startTime + "'"
                        + " AND " + LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_END + "=" + "'" + endTime + "'"
                , null);
    }
}
