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
import sctek.cn.ysbracelet.devicedata.SleepData;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.user.YsUser;
import sctek.cn.ysbracelet.utils.YsTextUtils;

/**
 * Created by kang on 16-3-17.
 */
public class FamilySleepMonthLvAdapter extends FamilyDataBaseLvAdapter{

    private final static String TAG = FamilySleepMonthLvAdapter.class.getSimpleName();

    public FamilySleepMonthLvAdapter(Context context, View emptyV, int offset) {
        super(context, emptyV, offset);
    }

    @Override
    protected List<YsData> getDataSet(int offset) {

        String dateStart = mDateManager.getFirstDayOfMonthBy(-offset);
        String dateEnd = mDateManager.getLastDayOfMonthBy(-offset);

        String[] projection = new String[] {LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_TOTALE
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEEP
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_SHALLOW
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_WAKE
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_END
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEVICE};

        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.Sleep.CONTENT_URI, projection
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + ">=" + "'" + dateStart + "'"
                        + " AND " + LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + "<" + "'" + dateEnd + "'"
                , null
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + " desc");

        List<YsData> records = new ArrayList<>();
        while (cursor.moveToNext()) {
            SleepData data = new SleepData();
            data.total = cursor.getInt(0);
            data.deep = cursor.getInt(1);
            data.shallow = cursor.getInt(2);
            data.wake = cursor.getInt(3);
            data.tempStartTime = cursor.getString(4);
            data.tempEndTime = cursor.getString(5);
            data.deviceId = cursor.getString(6);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.family_sleep_item_view, null);

            viewHolder = new ViewHolder();
            viewHolder.summaryV = convertView.findViewById(R.id.sleep_summary_rl);
            viewHolder.detailV = convertView.findViewById(R.id.sleep_detail_ll);

            viewHolder.name = (TextView)convertView.findViewById(R.id.name_tv);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time_tv);
            viewHolder.totalTime = (TextView)convertView.findViewById(R.id.total_time_tv);

            viewHolder.deepTv = (TextView)convertView.findViewById(R.id.deep_time_tv);
            viewHolder.shallowTv = (TextView)convertView.findViewById(R.id.shallow_time_tv);
            viewHolder.fallingTv = (TextView)convertView.findViewById(R.id.fall_time_tv);
            viewHolder.lieTimeTv = (TextView)convertView.findViewById(R.id.lie_time_tv);
            viewHolder.awakeTv = (TextView)convertView.findViewById(R.id.awake_time_tv);
            viewHolder.wakeTimesTv = (TextView)convertView.findViewById(R.id.wake_times_tv);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        setView(viewHolder, position);

        return convertView;
    }

    private void setView (ViewHolder holder, int postion) {
        holder.detailV.setVisibility(View.GONE);

        SleepData data = (SleepData) mRecords.get(postion);
        String[] tempE = data.tempStartTime.split(" ");

        String name = YsUser.getInstance().getDevice(data.deviceId).getName();
        holder.name.setText(name);
        holder.time.setText(tempE[0]);

        holder.totalTime.setText(YsTextUtils.parseHourForMinute(mContext, data.total));
        holder.deepTv.setText(YsTextUtils.parseHourForMinute(mContext, data.deep));
        holder.shallowTv.setText(YsTextUtils.parseHourForMinute(mContext, data.shallow));
        holder.wakeTimesTv.setText("" + data.wake);
    }

    public class ViewHolder extends BaseViewHolder{

        public TextView name;
        public TextView time;
        public TextView totalTime;

        public TextView deepTv;
        public TextView shallowTv;
        public TextView fallingTv;
        public TextView lieTimeTv;
        public TextView awakeTv;
        public TextView wakeTimesTv;

    }
}
