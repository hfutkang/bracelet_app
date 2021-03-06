package sctek.cn.ysbracelet.sqlite;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kang on 16-3-25.
 */
public final class LocalDataContract {

    public LocalDataContract() {};

    public static final String DB_NAME = "local_data.db";
    public static final int DB_VERSION = 1;
    public static final String AUTHORITY = "sctek.cn.ysbracelet.sqlite.LocalDataProvider";

    public static final String CONTENT_TYPE =
            "vnd.android.cursor.dir/vnd.sctek.bracelet";
    public static final String CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/vnd.sctek.bracelet";

    public static abstract class UserInfo implements BaseColumns {

        public static final Uri CONTENT_URI =
                Uri.parse("content://" + AUTHORITY + "/users");

        public static final String TABLE_NAME = "user_info";
        public static final String COLUMNS_NAME_NAME = "name";
        public static final String COLUMNS_NAME_PASSWORD = "password";
        public static final String COLUMNS_NAME_LAST_SYNC_TIME = "last_sync";

        public static final String CREATE_TABLE_USERINFO =
                "create table " + TABLE_NAME + " ("
                        + _ID + " integer auto_increment unique,"
                        + COLUMNS_NAME_NAME + " not null primary key,"
                        + COLUMNS_NAME_PASSWORD + " char(20) not null,"
                        + COLUMNS_NAME_LAST_SYNC_TIME + " datetime default '1970-01-01 00:00'"
                        + ");";
    }

    public static abstract class DeviceInfo implements BaseColumns {

        public static final Uri CONTENT_URI =
                Uri.parse("content://" + AUTHORITY + "/devices");

        public static final String TABLE_NAME = "devices";
        public static final String COLUMNS_NAME_SN = "sn";
        public static final String COLUMNS_NAME_MAC = "mac";
        public static final String COLUMNS_NAME_NAME = "name";
        public static final String COLUMNS_NAME_SEX = "sex";
        public static final String COLUMNS_NAME_AGE = "age";
        public static final String COLUMNS_NAME_WEIGHT = "weight";
        public static final String COLUMNS_NAME_HEIGHT = "height";
        public static final String COLUMNS_NAME_POWER = "power";
        public static final String COLUMNS_NAME_IMAGE_PATH = "image";

        public static final String CREATE_TABLE_DEVICEINFO =
                "create table " + TABLE_NAME + " ("
                        + _ID + " integer auto_increment unique,"
                        + COLUMNS_NAME_SN + " primary key,"
                        + COLUMNS_NAME_MAC + " varchar(20) not null,"
                        + COLUMNS_NAME_NAME + " varchar(20) default 'yoyo',"
                        + COLUMNS_NAME_SEX + " varchar(10) default 'male',"
                        + COLUMNS_NAME_AGE + " int(3) default '18',"
                        + COLUMNS_NAME_WEIGHT + " integer default '50',"
                        + COLUMNS_NAME_HEIGHT + " integer default '170',"
                        + COLUMNS_NAME_POWER + " integer default '0',"
                        + COLUMNS_NAME_IMAGE_PATH + " varchar(256)"
                        + ");";
    }

    public static abstract class Message implements BaseColumns {

        public static final Uri CONTENT_URI =
                Uri.parse("content://" + AUTHORITY + "/messages");

        public static final String TABLE_NAME = "message";
        public static final String COLUMNS_NAME_MESSAGE = "message";
        public static final String COLUMNS_NAME_TIME = "time";
        public static final String COLUMNS_NAME_TYPE = "type";
        public static final String COLUMNS_NAME_DEVICE = "device";

        public static final String CREATE_TABLE_MESSAGE =
                "create table " + TABLE_NAME + " ("
                        + _ID + " integer auto_increment unique,"
                        + COLUMNS_NAME_DEVICE + " varchar(20) not null,"
                        + COLUMNS_NAME_TIME + " varchar(20) not null default '1970-01-01 00:00',"
                        + COLUMNS_NAME_TYPE + " varchar(10),"
                        + COLUMNS_NAME_MESSAGE + " varchar(500),"
                        + "primary key(" + COLUMNS_NAME_DEVICE + "," + COLUMNS_NAME_TIME + ")"
                        + ");";
    }

    public static abstract class HeartRate implements BaseColumns {

        public static final Uri CONTENT_URI =
                Uri.parse("content://" + AUTHORITY + "/hrates");

        public static final String TABLE_NAME = "heart_rate";
        public static final String COLUMNS_NAME_RATE = "rate";
        public static final String COLUMNS_NAME_TIME = "time";
        public static final String COLUMNS_NAME_TYPE = "type";
        public static final String COLUMNS_NAME_DEVICE = "device";

        public static final String CREATE_TABLE_HEARTRATE =
                "create table " + TABLE_NAME + " ("
                        + _ID + " integer auto_increment unique,"
                        + COLUMNS_NAME_DEVICE + " varchar(20) not null,"
                        + COLUMNS_NAME_RATE + " int(3),"
                        + COLUMNS_NAME_TIME + " varchar(20) not null default '1970-01-01 00:00',"
                        + COLUMNS_NAME_TYPE + " varchar(10) default 'Manual',"
                        + "primary key(" + COLUMNS_NAME_DEVICE + "," + COLUMNS_NAME_TIME + ")"
                        + ");";

    }

    public static abstract class Location implements BaseColumns {

        public static final Uri CONTENT_URI =
                Uri.parse("content://" + AUTHORITY + "/locations");

        public static final String TABLE_NAME = "location";
        public static final String COLUMNS_NAME_LOCATION_TIME = "time";
        public static final String COLUMNS_NAME_LOCATION_TYPE = "type";
        public static final String COLUMNS_NAME_LOCATION_DEVICE = "device";
        public static final String COLUMNS_NAME_LOCATION_LATITUDE = "latitude";
        public static final String COLUMNS_NAME_LOCATION_LONGITUDE = "longitude";
        public static final String COLUMNS_NAME_LOCATION_MCC = "mcc";
        public static final String COLUMNS_NAME_LOCATION_MNC = "mnc";
        public static final String COLUMNS_NAME_LOCATION_LAC = "lac";
        public static final String COLUMNS_NAME_LOCATION_CID = "cid";

        public static final String CREATE_TABLE_LOCATION =
                "create table " + TABLE_NAME + " ("
                        + _ID + " integer auto_increment unique,"
                        + COLUMNS_NAME_LOCATION_DEVICE + " varchar(20) not null,"
                        + COLUMNS_NAME_LOCATION_LATITUDE + " double,"
                        + COLUMNS_NAME_LOCATION_LONGITUDE + " double,"
                        + COLUMNS_NAME_LOCATION_MCC + " int(3),"
                        + COLUMNS_NAME_LOCATION_MNC + " int(3),"
                        + COLUMNS_NAME_LOCATION_LAC + " int(3),"
                        + COLUMNS_NAME_LOCATION_CID + " int(3),"
                        + COLUMNS_NAME_LOCATION_TIME + " varchar(20) not null default '1970-01-01 00:00',"
                        + COLUMNS_NAME_LOCATION_TYPE + " varchar(10) default 'gps',"
                        + "primary key(" + COLUMNS_NAME_LOCATION_DEVICE + "," + COLUMNS_NAME_LOCATION_TIME + ")"
                        + ");";
    }

    public static abstract class Sports implements BaseColumns {

        public static final Uri CONTENT_URI =
                Uri.parse("content://" + AUTHORITY + "/sports");

        public static final String TABLE_NAME = "sports";
        public static final String COLUMNS_NAME_SPORTS_RUN = "run";
        public static final String COLUMNS_NAME_SPORTS_WALK = "walk";
        public static final String COLUMNS_NAME_SPORTS_CALORIES = "calories";
        public static final String COLUMNS_NAME_SPORTS_TYPE = "type";
        public static final String COLUMNS_NAME_SPORTS_DEVICE = "device";
        public static final String COLUMNS_NAME_SPORTS_TIME = "time";

        public static final String CREATE_TABLE_SPORTS =
                "create table " + TABLE_NAME + " ("
                        + _ID + " integer auto_increment unique,"
                        + COLUMNS_NAME_SPORTS_DEVICE + " varchar(20) not null,"
                        + COLUMNS_NAME_SPORTS_RUN + " int(6),"
                        + COLUMNS_NAME_SPORTS_WALK + " int(6),"
                        + COLUMNS_NAME_SPORTS_CALORIES + " calories,"
                        + COLUMNS_NAME_SPORTS_TIME + " varchar(20) not null default '1970-01-01',"
                        + COLUMNS_NAME_SPORTS_TYPE + " varchar(10),"
                        + "primary key(" + COLUMNS_NAME_SPORTS_DEVICE + "," + COLUMNS_NAME_SPORTS_TIME + ")"
                        + ");";
    }

    public static abstract class Sleep implements BaseColumns {

        public static final Uri CONTENT_URI =
                Uri.parse("content://" + AUTHORITY + "/sleeps");

        public static final String TABLE_NAME = "sleep";
        public static final String COLUMNS_NAME_SLEEP_TOTALE = "total";
        public static final String COLUMNS_NAME_SLEEP_DEEP = "deep";
        public static final String COLUMNS_NAME_SLEEP_SHALLOW = "shallow";
        public static final String COLUMNS_NAME_SLEEP_DEVICE = "device";
        public static final String COLUMNS_NAME_SLEEP_START = "start";
        public static final String COLUMNS_NAME_SLEEP_END = "end";
        public static final String COLUMNS_NAME_SLEEP_WAKE = "wake";

        public static final String CREATE_TABLE_SLEEP =
                "create table " + TABLE_NAME + " ("
                        + _ID + " integer auto_increment unique,"
                        + COLUMNS_NAME_SLEEP_DEVICE + " varchar(20) not null,"
                        + COLUMNS_NAME_SLEEP_TOTALE + " int(5),"
                        + COLUMNS_NAME_SLEEP_DEEP + " int(5),"
                        + COLUMNS_NAME_SLEEP_SHALLOW + " int(5),"
                        + COLUMNS_NAME_SLEEP_START + " varchar(20) not null default '1970-01-01 00:00',"
                        + COLUMNS_NAME_SLEEP_END + " varchar(20) not null default '1970-01-01 00:00',"
                        + COLUMNS_NAME_SLEEP_WAKE + " int(5),"
                        + "primary key(" + COLUMNS_NAME_SLEEP_DEVICE + "," + COLUMNS_NAME_SLEEP_START + ")"
                        + ");";
    }

}
