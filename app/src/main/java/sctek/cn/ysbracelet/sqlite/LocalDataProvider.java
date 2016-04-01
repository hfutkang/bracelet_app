package sctek.cn.ysbracelet.sqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by kang on 16-3-25.
 */
public class LocalDataProvider extends ContentProvider {

    private static final UriMatcher mUriMatcher;

    private static final int COLLECTION_USER_URI_INDICATOR = 1;
    private static final int SINGLE_USER_URI_INDICATOR = 2;

    private static final int COLLECTION_DEVICE_URI_INDICATOR = 3;
    private static final int SINGLE_DEVICE_URI_INDICATOR = 4;

    private static final int COLLECTION_HRATE_URI_INDICATOR = 5;
    private static final int SINGLE_HRATE_URI_INDICATOR = 6;

    private static final int COLLECTION_SPORTS_URI_INDICATOR = 7;
    private static final int SINGLE_SPORTS_URI_INDICATOR = 8;

    private static final int COLLECTION_LOCATION_URI_INDICATOR = 9;
    private static final int SINGLE_LOCATION_URI_INDICATOR = 10;

    private static final int COLLECTION_SLEEP_URI_INDICATOR = 11;
    private static final int SINGLE_SLEEP_URI_INDICATOR = 12;

    private static final int COLLECTION_MESSAGE_URI_INDICATOR = 13;
    private static final int SINGLE_MESSAGE_URI_INDICATOR = 14;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        mUriMatcher.addURI(LocalDataContract.AUTHORITY, "users", COLLECTION_USER_URI_INDICATOR);
        mUriMatcher.addURI(LocalDataContract.AUTHORITY, "users/#", SINGLE_USER_URI_INDICATOR);

        mUriMatcher.addURI(LocalDataContract.AUTHORITY, "devices", COLLECTION_DEVICE_URI_INDICATOR);
        mUriMatcher.addURI(LocalDataContract.AUTHORITY, "devices/#", SINGLE_DEVICE_URI_INDICATOR);

        mUriMatcher.addURI(LocalDataContract.AUTHORITY, "hrates", COLLECTION_HRATE_URI_INDICATOR);
        mUriMatcher.addURI(LocalDataContract.AUTHORITY, "hrates/#", SINGLE_HRATE_URI_INDICATOR);

        mUriMatcher.addURI(LocalDataContract.AUTHORITY, "sports", COLLECTION_SPORTS_URI_INDICATOR);
        mUriMatcher.addURI(LocalDataContract.AUTHORITY, "sports/#", SINGLE_SPORTS_URI_INDICATOR);

        mUriMatcher.addURI(LocalDataContract.AUTHORITY, "locations", COLLECTION_LOCATION_URI_INDICATOR);
        mUriMatcher.addURI(LocalDataContract.AUTHORITY, "locations/#", SINGLE_LOCATION_URI_INDICATOR);

        mUriMatcher.addURI(LocalDataContract.AUTHORITY, "sleeps", COLLECTION_SLEEP_URI_INDICATOR);
        mUriMatcher.addURI(LocalDataContract.AUTHORITY, "sleeps/#", SINGLE_SLEEP_URI_INDICATOR);

        mUriMatcher.addURI(LocalDataContract.AUTHORITY, "messages", COLLECTION_MESSAGE_URI_INDICATOR);
        mUriMatcher.addURI(LocalDataContract.AUTHORITY, "messages/#", SINGLE_MESSAGE_URI_INDICATOR);

    }

    private LocalDataDbHelper mLocalDataDbHelper;

    @Override
    public boolean onCreate() {
        mLocalDataDbHelper = new LocalDataDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase database = mLocalDataDbHelper.getReadableDatabase();

        switch (mUriMatcher.match(uri)) {
            case COLLECTION_USER_URI_INDICATOR:
                queryBuilder.setTables(LocalDataContract.UserInfo.TABLE_NAME);
                break;
            case SINGLE_USER_URI_INDICATOR:
                queryBuilder.setTables(LocalDataContract.UserInfo.TABLE_NAME);
                queryBuilder.appendWhere(LocalDataContract.UserInfo._ID + "="
                + uri.getPathSegments().get(1));
                break;
            case COLLECTION_DEVICE_URI_INDICATOR:
                queryBuilder.setTables(LocalDataContract.DeviceInfo.TABLE_NAME);
                break;
            case SINGLE_DEVICE_URI_INDICATOR:
                queryBuilder.setTables(LocalDataContract.DeviceInfo.TABLE_NAME);
                queryBuilder.appendWhere(LocalDataContract.DeviceInfo._ID + "="
                        + uri.getPathSegments().get(1));
                break;
            case COLLECTION_HRATE_URI_INDICATOR:
                queryBuilder.setTables(LocalDataContract.HeartRate.TABLE_NAME);
                break;
            case SINGLE_HRATE_URI_INDICATOR:
                queryBuilder.setTables(LocalDataContract.HeartRate.TABLE_NAME);
                queryBuilder.appendWhere(LocalDataContract.HeartRate._ID + "="
                        + uri.getPathSegments().get(1));
                break;
            case COLLECTION_SPORTS_URI_INDICATOR:
                queryBuilder.setTables(LocalDataContract.Sports.TABLE_NAME);
                break;
            case SINGLE_SPORTS_URI_INDICATOR:
                queryBuilder.setTables(LocalDataContract.Sports.TABLE_NAME);
                queryBuilder.appendWhere(LocalDataContract.Sports._ID + "="
                        + uri.getPathSegments().get(1));
                break;
            case COLLECTION_SLEEP_URI_INDICATOR:
                queryBuilder.setTables(LocalDataContract.Sleep.TABLE_NAME);
                break;
            case SINGLE_SLEEP_URI_INDICATOR:
                queryBuilder.setTables(LocalDataContract.Sleep.TABLE_NAME);
                queryBuilder.appendWhere(LocalDataContract.Sleep._ID + "="
                + uri.getPathSegments().get(1));
                break;
            case COLLECTION_LOCATION_URI_INDICATOR:
                queryBuilder.setTables(LocalDataContract.Location.TABLE_NAME);
                break;
            case SINGLE_LOCATION_URI_INDICATOR:
                queryBuilder.setTables(LocalDataContract.Location.TABLE_NAME);
                queryBuilder.appendWhere(LocalDataContract.Location._ID + "="
                + uri.getPathSegments().get(1));
                break;
            case COLLECTION_MESSAGE_URI_INDICATOR:
                queryBuilder.setTables(LocalDataContract.Message.TABLE_NAME);
                break;
            case SINGLE_MESSAGE_URI_INDICATOR:
                queryBuilder.setTables(LocalDataContract.Message.TABLE_NAME);
                queryBuilder.appendWhere(LocalDataContract.Message._ID + "="
                + uri.getPathSegments().get(1));
                break;
        }

        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs,
                null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case COLLECTION_USER_URI_INDICATOR:
            case COLLECTION_DEVICE_URI_INDICATOR:
            case COLLECTION_HRATE_URI_INDICATOR:
            case COLLECTION_SPORTS_URI_INDICATOR:
            case COLLECTION_SLEEP_URI_INDICATOR:
            case COLLECTION_LOCATION_URI_INDICATOR:
            case COLLECTION_MESSAGE_URI_INDICATOR:
                return LocalDataContract.CONTENT_TYPE;
            case SINGLE_USER_URI_INDICATOR:
            case SINGLE_HRATE_URI_INDICATOR:
            case SINGLE_DEVICE_URI_INDICATOR:
            case SINGLE_SPORTS_URI_INDICATOR:
            case SINGLE_SLEEP_URI_INDICATOR:
            case SINGLE_MESSAGE_URI_INDICATOR:
            case SINGLE_LOCATION_URI_INDICATOR:
                return LocalDataContract.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId = 0;
        SQLiteDatabase database = mLocalDataDbHelper.getWritableDatabase();
        String tableName = null;
        switch (mUriMatcher.match(uri)) {
            case COLLECTION_USER_URI_INDICATOR:
                tableName = LocalDataContract.UserInfo.TABLE_NAME;
                break;
            case COLLECTION_DEVICE_URI_INDICATOR:
                tableName = LocalDataContract.DeviceInfo.TABLE_NAME;
                break;
            case COLLECTION_HRATE_URI_INDICATOR:
                tableName = LocalDataContract.HeartRate.TABLE_NAME;
                break;
            case COLLECTION_SPORTS_URI_INDICATOR:
                tableName = LocalDataContract.Sports.TABLE_NAME;
                break;
            case COLLECTION_SLEEP_URI_INDICATOR:
                tableName = LocalDataContract.Sleep.TABLE_NAME;
                break;
            case COLLECTION_LOCATION_URI_INDICATOR:
                tableName = LocalDataContract.Location.TABLE_NAME;
                break;
            case COLLECTION_MESSAGE_URI_INDICATOR:
                tableName = LocalDataContract.Message.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unkown uri" + uri);
        }
        rowId = database.insert(tableName, null, values);

        if(rowId > 0) {
            Uri insertUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(uri, null);
            return insertUri;
        }
        throw new SQLException("Fail to insert row for " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mLocalDataDbHelper.getWritableDatabase();
        String tableName = null;
        String rowId = null;
        int count = 0;
        switch (mUriMatcher.match(uri)) {
            case SINGLE_USER_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
            case COLLECTION_USER_URI_INDICATOR:
                tableName = LocalDataContract.UserInfo.TABLE_NAME;
                break;
            case SINGLE_DEVICE_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
            case COLLECTION_DEVICE_URI_INDICATOR:
                tableName = LocalDataContract.DeviceInfo.TABLE_NAME;
                break;
            case SINGLE_HRATE_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
            case COLLECTION_HRATE_URI_INDICATOR:
                tableName = LocalDataContract.HeartRate.TABLE_NAME;
                break;
            case SINGLE_SPORTS_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
            case COLLECTION_SPORTS_URI_INDICATOR:
                tableName = LocalDataContract.Sports.TABLE_NAME;
                break;
            case SINGLE_SLEEP_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
            case COLLECTION_SLEEP_URI_INDICATOR:
                tableName = LocalDataContract.Sleep.TABLE_NAME;
                break;
            case SINGLE_LOCATION_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
            case COLLECTION_LOCATION_URI_INDICATOR:
                tableName = LocalDataContract.Location.TABLE_NAME;
                break;
            case SINGLE_MESSAGE_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
            case COLLECTION_MESSAGE_URI_INDICATOR:
                tableName = LocalDataContract.Message.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unkown uri" + uri);
        }
        if(rowId == null) {
            count = database.delete(tableName, selection, selectionArgs);
        }
        else {
            count = database.delete(tableName, "_id" + "=" + rowId
                    + (TextUtils.isEmpty(selection)?"":" AND (" + selection + ')'), selectionArgs);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mLocalDataDbHelper.getWritableDatabase();
        String tableName = null;
        String rowId = null;
        int count = 0;
        switch (mUriMatcher.match(uri)) {
            case SINGLE_USER_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
            case COLLECTION_USER_URI_INDICATOR:
                tableName = LocalDataContract.UserInfo.TABLE_NAME;
                break;
            case SINGLE_DEVICE_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
            case COLLECTION_DEVICE_URI_INDICATOR:
                tableName = LocalDataContract.DeviceInfo.TABLE_NAME;
                break;
            case SINGLE_HRATE_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
            case COLLECTION_HRATE_URI_INDICATOR:
                tableName = LocalDataContract.HeartRate.TABLE_NAME;
                break;
            case SINGLE_SPORTS_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
            case COLLECTION_SPORTS_URI_INDICATOR:
                tableName = LocalDataContract.Sports.TABLE_NAME;
                break;
            case SINGLE_SLEEP_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
            case COLLECTION_SLEEP_URI_INDICATOR:
                tableName = LocalDataContract.Sleep.TABLE_NAME;
                break;
            case SINGLE_LOCATION_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
            case COLLECTION_LOCATION_URI_INDICATOR:
                tableName = LocalDataContract.Location.TABLE_NAME;
                break;
            case SINGLE_MESSAGE_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
            case COLLECTION_MESSAGE_URI_INDICATOR:
                tableName = LocalDataContract.Message.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unkown uri" + uri);
        }
        if(rowId == null) {
            count = database.update(tableName, values, selection, selectionArgs);
        }
        else {
            count = database.update(tableName, values, "_id" + "=" + rowId
            + (TextUtils.isEmpty(selection)?"":" AND (" + selection + ")"), selectionArgs);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
