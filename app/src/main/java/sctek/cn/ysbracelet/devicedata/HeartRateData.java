package sctek.cn.ysbracelet.devicedata;

import java.util.Date;

import sctek.cn.ysbracelet.http.XmlNodes;

/**
 * Created by kang on 16-2-19.
 */
public class HeartRateData implements YsData {
    private static final String TAG = HeartRateData.class.getSimpleName();

    public Date time;
    public int rate;

    @Override
    public void setField(String field, String value) {
        if(XmlNodes.HeartRateNodes.NODE_RATE.equals(field)) {

        }
        else if(XmlNodes.HeartRateNodes.NODE_TIME.equals(field)) {

        }
    }
}
