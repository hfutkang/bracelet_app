package sctek.cn.ysbracelet.devicedata;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import java.util.Date;

import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

/**
 * Created by kang on 16-2-20.
 */
public class PositionData implements YsData{

    private final static String TAG = PositionData.class.getSimpleName();

    public Date time;

    public String deviceId;

    public double longitude;

    public double latitude;

    public int mcc;

    public int mnc;

    public int lac;

    public int cid;

    public String type;

    public String tempTime = "2016-03-29";

    @Override
    public void setField(String field, String value) {
        if(XmlNodes.PositionNodes.NODE_LATITUDE.equals(field)) {
            latitude = Double.parseDouble(value);
        }
        else if(XmlNodes.PositionNodes.NODE_LONGITUDE.equals(field)) {
            longitude = Double.parseDouble(value);
        }
        else if(XmlNodes.PositionNodes.NODE_TIME.equals(field)) {
            tempTime = value;
        }
        else if(XmlNodes.PositionNodes.NODE_DEVICE_ID.equals(field)) {
            deviceId = value;
        }
    }

    @Override
    public Uri insert(ContentResolver cr) {
        Log.e(TAG, deviceId + " " + longitude + " " + latitude + " " + tempTime);
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_DEVICE, deviceId);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_LATITUDE, latitude);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_LONGITUDE, longitude);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_MCC, mcc);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_MNC, mnc);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_LAC, lac);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_CID, cid);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_TYPE, type);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME, tempTime);
        return cr.insert(LocalDataContract.Location.CONTENT_URI, values);
    }

    @Override
    public int update(ContentResolver cr) {
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_DEVICE, deviceId);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_LATITUDE, longitude);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_LONGITUDE, latitude);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_MCC, mcc);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_MNC, mnc);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_LAC, lac);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_CID, cid);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_TYPE, type);
        values.put(LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME, tempTime);
        return cr.update(LocalDataContract.Location.CONTENT_URI
                , values
                , LocalDataContract.Location.COLUMNS_NAME_LOCATION_DEVICE + "=" + deviceId
                + " AND " + LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME + "=" + tempTime
                , null);
    }

    @Override
    public int delete(ContentResolver cr) {

        return cr.delete(LocalDataContract.Location.CONTENT_URI
                , LocalDataContract.Location.COLUMNS_NAME_LOCATION_DEVICE + "=" + deviceId
                + " AND " + LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME + "=" + tempTime
                , null);
    }
}
