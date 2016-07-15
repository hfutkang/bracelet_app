package sctek.cn.ysbracelet.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.BaseAdapter;

import java.text.ParseException;

import sctek.cn.ysbracelet.sqlite.LocalDataContract;

/**
 * Created by kang on 16-4-13.
 */
public class FamilyHRateDataWeekVpAdapter extends FamilyDataBaseVpAdapter {

    private final static String TAG = FamilyHRateDataWeekVpAdapter.class.getSimpleName();

    public FamilyHRateDataWeekVpAdapter(Context context) {
        super(context);
    }
    @Override
    protected int getPageCount() {
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.HeartRate.CONTENT_URI
                , new String[]{LocalDataContract.HeartRate.COLUMNS_NAME_TIME}
                , null
                , null
                , LocalDataContract.HeartRate.COLUMNS_NAME_TIME + " asc limit 1");
        String earliestDate = null;
        if(cursor.moveToNext()) {
            earliestDate = cursor.getString(0);
        }
        cursor.close();

        int pages = 0;
        try {
            pages = mDateManager.weeksFromNowTo(earliestDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pages;
    }

    @Override
    protected BaseAdapter constructLvAdapter(Context context, View emptyV, int postion) {
        return new FamilyHRateWeekLvAdapter(context, emptyV, postion);
    }
}
