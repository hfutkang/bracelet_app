package sctek.cn.ysbracelet.braceletdata;

import java.util.Date;

import sctek.cn.ysbracelet.http.XmlNodes;

/**
 * Created by kang on 16-2-20.
 */
public class PositionData implements YsData{

    public Date time;

    public long longitude;

    public long latitude;

    @Override
    public void setField(String field, String value) {
        if(XmlNodes.PositionNodes.NODE_LATITUDE.equals(field)) {

        }
        else if(XmlNodes.PositionNodes.NODE_LONGITUDE.equals(field)) {

        }
        else if(XmlNodes.PositionNodes.NODE_TIME.equals(field)) {

        }
    }
}
