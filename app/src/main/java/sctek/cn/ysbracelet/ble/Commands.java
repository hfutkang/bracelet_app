package sctek.cn.ysbracelet.ble;

/**
 * Created by kang on 16-2-18.
 */
public class Commands {

    public static final byte CMD_MOTOR_CONTROL = 0x00;

    public static final byte CMD_SYNC_TIME = 0x01;

    public static final byte CMD_GET_BATTERY = 0x02;

    public static final byte CMD_SET_TIMER = 0x03;

    public static final byte CMD_GET_TIMERS = 0x04;

    public static final byte CMD_CONTROL_TIMER = 0x05;

    public static final byte CMD_GET_SPORT_DATA = 0x06;

    public static final byte CMD_GET_SLEEP_DATA = 0x07;

    public static final byte CMD_GET_HEART_RATE = 0x08;

    public static final byte CMD_SWITCH_MODE = 0x09;

    public static final byte CMD_SOS = (byte)0x80;

    public static final byte CMD_MEASURE_HRATE = 0x00;

    public class MotorData {

        public static final byte START = 0x01;

        public static final byte STOP = 0x02;

        public static final byte SHAKE_SHORT  = 0x03;

        public static final byte SHAKE_LONG = 0x04;

    }

    public class ModeData {

        public static final byte MODE_STANDBY = 0x01;

        public static final byte MODE_SPORT = 0x02;

        public static final byte MODE_CHECK  = 0x03;

    }
}
