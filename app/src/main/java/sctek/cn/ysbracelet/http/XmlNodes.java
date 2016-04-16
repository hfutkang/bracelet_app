package sctek.cn.ysbracelet.http;

/**
 * Created by kang on 16-2-20.
 */
public class XmlNodes {

    public final static int RESPONSE_CODE_SUCCESS = 100;
    public final static int RESPONSE_CODE_FAIL = 300;
    public final static int RESPONSE_CODE_OTHER = 400;

    public final static String NODE_POSITIONS = "positions";

    public final static String NODE_POSITION = "position";

    public final static String NODE_HEARTRATES = "heartrates";

    public final static String NODE_HEARTRATE = "heartrate";

    public final static String NODE_SPORTS = "sports";

    public final static String NODE_SPORT = "sport";

    public final static String NODE_SLEEP = "sleep";

    public final static String NODE_RES = "res";

    public final static String NODE_TIMESTAMP = "ts";

    public final static String NODE_LOGIN = "login";

    public final static String NODE_REGISTER = "register";

    public final static String NODE_USERINFO = "userinfo";

    public final static String NODE_DEVICE = "device";

    public final static String NODE_DEVICES = "devices";

    public final static String NODE_DELETE_DEVICE = "delDevice";

    public final static String NODE_ADD_DEVICE = "addDevice";

    public final static String NODE_UPDATE_DEVICEINFO = "updateDInfo";

    public class PositionNodes {

        public final static String NODE_TIME = "time";

        public final static String NODE_LONGITUDE = "lon";

        public final static String NODE_LATITUDE = "lat";

        public final static String NODE_DEVICE_ID = "deviceId";

    }

    public class HeartRateNodes {

        public final static String NODE_TIME = "time";

        public final static String NODE_RATE = "HR";

        public final static String NODE_DEVICE_ID = "deviceId";

        public final static String NODE_TYPE = "type";
    }

    public class SportNodes {

        public final static String NODE_TIME = "time";

        public final static String NODE_WALK = "walk";

        public final static String NODE_RUN = "run";

        public final static String NODE_DEVICE_ID = "deviceId";

    }

    public class SleepNodes {

        public final static String NODE_TIME = "time";

        public final static String NODE_QUALITY = "quality";

        public final static String NODE_DEVICE_ID = "deviceId";

        public final static String NODE_TOTAL = "total";

        public final static String NODE_DEEP = "deep";

        public final static String NODE_SHALLOW = "shallow";

        public final static String NODE_WAKE = "wake";

        public final static String NODE_START = "start";

        public final static String NODE_END = "end";

    }

    public class UserNodes {

        public final static String NODE_NAME = "name";

        public final static String NODE_PASSWORD = "password";

        public final static String NODE_SEX = "sex";

        public final static String NODE_AGE = "age";

        public final static String NODE_HEIGTH = "height";

        public final static String NODE_WEIGHT = "weight";

    }

    public class DeviceNodes {

        public final static String NODE_ID = "id";

        public final static String NODE_USER = "user";

        public final static String NODE_NAME = "name";

        public final static String NODE_SEX = "sex";

        public final static String NODE_AGE = "age";

        public final static String NODE_MAC = "mac";

        public final static String NODE_POWER = "power";

        public final static String NODE_WEIGHT = "weight";

        public final static String NODE_HEIGHT = "height";

        public final static String NODE_IMAGE = "image";

    }

    public class Session {

        public final static int SESSION_POSITION = 0;

        public final static int SESSION_SPORT = 1;

        public final static int SESSION_SLEEP = 2;

        public final static int SESSION_HEARTRATE = 3;

        public final static int SESSEION_LOGIN = 4;

        public final static int SESSION_REGISTER = 5;
    }
}
