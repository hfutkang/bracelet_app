package sctek.cn.ysbracelet.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.devicedata.HeartRateData;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.user.YsUser;

/**
 * Created by kang on 16-3-17.
 */
public class FamilyHRateWeekLvAdapter extends FamilyDataBaseLvAdapter{

    private final static String TAG = FamilyHRateWeekLvAdapter.class.getSimpleName();

    public FamilyHRateWeekLvAdapter(Context context, int offset) {
        super(context, offset);
    }

    @Override
    protected List<YsData> getDataSet(int offset) {

        String dateStart = mDateManager.getFirstDayOfWeekBy(-offset);
        String dateEnd = mDateManager.getFisrtDayOfNextWeekBy(-offset);

        String[] projection = new String[]{LocalDataContract.HeartRate.COLUMNS_NAME_RATE
                , LocalDataContract.HeartRate.COLUMNS_NAME_TIME
                , LocalDataContract.HeartRate.COLUMNS_NAME_TYPE
                , LocalDataContract.HeartRate.COLUMNS_NAME_DEVICE};

        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.HeartRate.CONTENT_URI, projection
                , LocalDataContract.HeartRate.COLUMNS_NAME_TIME + ">" + "'" + dateStart + "'"
                        + " AND " + LocalDataContract.HeartRate.COLUMNS_NAME_TIME + "<" + "'" + dateEnd + "'"
                , null
                , LocalDataContract.HeartRate.COLUMNS_NAME_TIME + " desc");

        List<YsData> records = new ArrayList<>();
        while (cursor.moveToNext()) {
            HeartRateData data = new HeartRateData();
            data.rate = cursor.getInt(0);
            data.tempTime = cursor.getString(1);
            data.type = cursor.getString(2);
            data.deviceId = cursor.getString(3);
            records.add(data);
        }
        cursor.close();
        return records;
    }

    @Override
    protected int getItemCount(int offset) {
        return mRecords.size();
    }

    @Override
    protected View inflaterItemView(int position, View convertView) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.family_hrate_item_view, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)convertView.findViewById(R.id.name_tv);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time_tv);
            viewHolder.rate = (TextView)convertView.findViewById(R.id.rate_tv);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HeartRateData data = (HeartRateData)mRecords.get(position);
        viewHolder.rate.setText("" + data.rate);
        viewHolder.time.setText(data.tempTime);
        viewHolder.name.setText(YsUser.getInstance().getDevice(data.deviceId).name);

        return convertView;
    }


    public class ViewHolder extends BaseViewHolder{

        public TextView rate;

        public TextView name;
        public TextView time;
    }
}
