package sctek.cn.ysbracelet.devicedata;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

/**
 * Created by kang on 16-2-19.
 */
public class SportsData implements YsData {

    private final static String TAG = SportsData.class.getSimpleName();

    public int runSteps;
    public int walkSteps;

    public int distance;//unit m
    public String type;
    public int calories;

    public String deviceId;

    public Date date;

    public String tempTime = "2016-03-29";

    public SportsData() {

    }

    public SportsData(int run, int walk, String date) {
        runSteps = run;
        walkSteps = walk;

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            this.date = format.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setField(String field, String value) {

        if(XmlNodes.SportNodes.NODE_RUN.equals(field)) {

        }
        else if(XmlNodes.SportNodes.NODE_WALK.equals(field)) {

        }
        else if(XmlNodes.SportNodes.NODE_TIME.equals(field)) {

        }
    }

    @Override
    public Uri insert(ContentResolver cr) {
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.Sports.COLUMNS_NAME_SPORTS_DEVICE, deviceId);
        values.put(LocalDataContract.Sports.COLUMNS_NAME_SPORTS_RUN, runSteps);
        values.put(LocalDataContract.Sports.COLUMNS_NAME_SPORTS_WALK, walkSteps);
        values.put(LocalDataContract.Sports.COLUMNS_NAME_SPORTS_CALORIES, calories);
        values.put(LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME, tempTime);
        values.put(LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TYPE, type);
        return cr.insert(LocalDataContract.Sports.CONTENT_URI, values);
    }

    @Override
    public int update(ContentResolver cr) {
        ContentValues values = new ContentValues();
        values.put(LocalDataContract.Sports.COLUMNS_NAME_SPORTS_DEVICE, deviceId);
        values.put(LocalDataContract.Sports.COLUMNS_NAME_SPORTS_RUN, runSteps);
        values.put(LocalDataContract.Sports.COLUMNS_NAME_SPORTS_WALK, walkSteps);
        values.put(LocalDataContract.Sports.COLUMNS_NAME_SPORTS_CALORIES, calories);
        values.put(LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME, tempTime);
        values.put(LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TYPE, type);
        return cr.update(LocalDataContract.Sports.CONTENT_URI
                , values
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_DEVICE + "=" + deviceId
                    + " AND " + LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME + "=" + tempTime
                , null);
    }

    @Override
    public int delete(ContentResolver cr) {
        return cr.delete(LocalDataContract.Sports.CONTENT_URI
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_DEVICE + "=" + deviceId
                    + " AND " + LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME + "=" + tempTime
                , null);
    }
}
