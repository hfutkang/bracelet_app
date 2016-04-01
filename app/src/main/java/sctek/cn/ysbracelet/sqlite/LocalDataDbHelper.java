package sctek.cn.ysbracelet.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kang on 16-3-25.
 */
public class LocalDataDbHelper extends SQLiteOpenHelper {

    public final static String TAG = LocalDataDbHelper.class.getSimpleName();

    public LocalDataDbHelper(Context context) {
        super(context, LocalDataContract.DB_NAME, null, LocalDataContract.DB_VERSION);
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.e(TAG, "onCreate");

        db.execSQL(LocalDataContract.UserInfo.CREATE_TABLE_USERINFO);

        db.execSQL(LocalDataContract.DeviceInfo.CREATE_TABLE_DEVICEINFO);

        db.execSQL(LocalDataContract.HeartRate.CREATE_TABLE_HEARTRATE);

        db.execSQL(LocalDataContract.Sports.CREATE_TABLE_SPORTS);

        db.execSQL(LocalDataContract.Sleep.CREATE_TABLE_SPORTS);

        db.execSQL(LocalDataContract.Location.CREATE_TABLE_LOCATION);

        db.execSQL(LocalDataContract.Message.CREATE_TABLE_MESSAGE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.e(TAG, "upgrade database from version "
        + oldVersion + " to " + newVersion);

        Log.e(TAG, "dropping table user info");
        db.execSQL("DROP TABLE IF EXISTS "
        + LocalDataContract.UserInfo.TABLE_NAME);

        Log.e(TAG, "dropping table device info");
        db.execSQL("DROP TABLE IF EXISTS "
                + LocalDataContract.DeviceInfo.TABLE_NAME);

        Log.e(TAG, "dropping table heart rate");
        db.execSQL("DROP TABLE IF EXISTS "
                + LocalDataContract.HeartRate.TABLE_NAME);

        Log.e(TAG, "dropping table sports");
        db.execSQL("DROP TABLE IF EXISTS "
                + LocalDataContract.Sports.TABLE_NAME);

        Log.e(TAG, "dropping table location");
        db.execSQL("DROP TABLE IF EXISTS "
                + LocalDataContract.Location.TABLE_NAME);

        Log.e(TAG, "dropping table sleep");
        db.execSQL("DROP TABLE IF EXISTS "
                + LocalDataContract.Sleep.TABLE_NAME);

        Log.e(TAG, "dropping table message");
        db.execSQL("DROP TABLE IF EXISTS "
                + LocalDataContract.Message.TABLE_NAME);

        onCreate(db);

    }
}
