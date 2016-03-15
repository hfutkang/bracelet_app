package sctek.cn.ysbracelet.devicedata;

import java.text.SimpleDateFormat;
import java.util.Date;

import sctek.cn.ysbracelet.http.XmlNodes;

/**
 * Created by kang on 16-2-19.
 */
public class SportsData implements YsData {

    private final static String TAG = SportsData.class.getSimpleName();

    public int runSteps;
    public int walkSteps;

    public int distance;//unit m
    public int type;
    public int calories;

    public Date date;

    public SportsData() {

    }

    public SportsData(int run, int walk, String date) {
        runSteps = run;
        walkSteps = walk;

        SimpleDateFormat format = new SimpleDateFormat("yyyMMdd");
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
}
