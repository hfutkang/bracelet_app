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
public class FamilySportsStatisticsMonthVpAdapter extends FamilyStatisticsBaseVpAdapter {

    private final static String TAG = FamilySportsStatisticsMonthVpAdapter.class.getSimpleName();

    public FamilySportsStatisticsMonthVpAdapter(Context context) {
        super(context);
    }
    @Override
    protected int getPageCount() {
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.Sports.CONTENT_URI
                , new String[]{LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME}
                , null
                , null
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME + " asc limit 1");
        String earliestDate = null;
        if(cursor.moveToNext()) {
            earliestDate = cursor.getString(0);
        }
        cursor.close();

        int pages = 0;
        try {
            pages = mDateManager.monthsFromNowTo(earliestDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pages;
    }

    @Override
    protected BaseAdapter constructLvAdapter(Context context, int postion) {
        return new FamilySportsStatisticsMonthLvAdapter(context, postion);
    }
}
