package sctek.cn.ysbracelet.devicedata;

import java.util.Date;

import sctek.cn.ysbracelet.http.XmlNodes;

/**
 * Created by kang on 16-2-19.
 */
public class SleepData implements YsData {

    private final static String TAG = SleepData.class.getSimpleName();

    public int quality;

    public Date startTime;
    public Date endTime;

    @Override
    public void setField(String field, String value) {
        if(XmlNodes.SleepNodes.NODE_QUALITY.equals(field)) {

        }
        else if(XmlNodes.SleepNodes.NODE_TIME.equals(field)) {

        }
    }
}
