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
            latitude = parseLat(value);
            Log.e(TAG, value + " " + latitude);
        }
        else if(XmlNodes.PositionNodes.NODE_LONGITUDE.equals(field)) {
            longitude = parseLon(value);
            Log.e(TAG, value + " " + longitude);
        }
        else if(XmlNodes.PositionNodes.NODE_TIME.equals(field)) {
            tempTime = value;
        }
        else if(XmlNodes.PositionNodes.NODE_DEVICE_ID.equals(field)) {
            deviceId = value;
        }
        else if(XmlNodes.PositionNodes.NODE_TYPE.equals(field)) {
            type = value;
        }
        else if(XmlNodes.PositionNodes.NODE_MCC.equals(field)) {
            mcc = Integer.parseInt(value);
        }
        else if(XmlNodes.PositionNodes.NODE_MNC.equals(field)) {
            mnc = Integer.parseInt(value);
        }
        else if(XmlNodes.PositionNodes.NODE_LAC.equals(field)) {
            lac = Integer.parseInt(value);
        }
        else if(XmlNodes.PositionNodes.NODE_CID.equals(field)) {
            cid = Integer.parseInt(value);
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
                + " AND " + LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME + "=" + "'" + tempTime + "'"
                , null);
    }

    @Override
    public int delete(ContentResolver cr) {

        return cr.delete(LocalDataContract.Location.CONTENT_URI
                , LocalDataContract.Location.COLUMNS_NAME_LOCATION_DEVICE + "=" + deviceId
                + " AND " + LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME + "=" + tempTime
                , null);
    }

    public double parseLat(String lat) {
        Log.e(TAG, "----------" + lat);
        if(lat.equals("0"))
            return 0;

        String seg1, seg2, seg3;
        double latitude;
        if(lat.substring(0,1) != "-") {
            seg1 = lat.substring(0, 2);
            seg2 = lat.substring(2, 4);
            seg3 = lat.substring(4, 8);
            latitude = Double.parseDouble(seg1)
                    + Double.parseDouble(seg2)/60
                    + Double.parseDouble(seg3)/600000;
        }
        else {
            seg1 = lat.substring(1, 4);
            seg2 = lat.substring(4, 6);
            seg3 = lat.substring(6, 10);
            latitude = -1*(Double.parseDouble(seg1)
                    + Double.parseDouble(seg2)/60
                    + Double.parseDouble(seg3)/600000);
        }
        return latitude;
    }

    public double parseLon(String lon) {
        Log.e(TAG, "----------" + lon);
        if(lon.equals("0"))
            return 0;

        String seg1, seg2, seg3;
        double longitude;
        if(lon.substring(0,1) != "-") {
            seg1 = lon.substring(0, 3);
            seg2 = lon.substring(3, 5);
            seg3 = lon.substring(5, 9);
            longitude = Double.parseDouble(seg1)
                    + Double.parseDouble(seg2)/60
                    + Double.parseDouble(seg3)/600000;
        }
        else {
            seg1 = lon.substring(1, 4);
            seg2 = lon.substring(4, 6);
            seg3 = lon.substring(6, 10);
            longitude = -1*(Double.parseDouble(seg1)
                    + Double.parseDouble(seg2)/60
                    + Double.parseDouble(seg3)/600000);
        }
        return longitude;
    }

    @Override
    public void clearField() {
        time = null;

        deviceId = null;

        longitude = 0;

        latitude = 0;

        mcc = 0;

        mnc = 0;

        lac = 0;

        cid = 0;

        type = null;
    }

    public YsData clone() {
        PositionData temp = null;
        try {
            temp = (PositionData)super.clone();
        }catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
