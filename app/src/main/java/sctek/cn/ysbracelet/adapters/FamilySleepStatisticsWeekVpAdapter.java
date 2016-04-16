package sctek.cn.ysbracelet.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.widget.BaseAdapter;

import java.text.ParseException;

import sctek.cn.ysbracelet.sqlite.LocalDataContract;

/**
 * Created by kang on 16-4-13.
 */
public class FamilySleepStatisticsWeekVpAdapter extends FamilyStatisticsBaseVpAdapter {

    private final static String TAG = FamilySleepStatisticsWeekVpAdapter.class.getSimpleName();

    public FamilySleepStatisticsWeekVpAdapter(Context context) {
        super(context);
    }
    @Override
    protected int getPageCount() {
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.Sleep.CONTENT_URI
                , new String[]{LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START}
                , null
                , null
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + " asc limit 1");
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
    protected BaseAdapter constructLvAdapter(Context context, int postion) {
        return new FamilySleepStatisticsWeekLvAdapter(context, postion);
    }
}
