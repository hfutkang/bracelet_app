package sctek.cn.ysbracelet.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.devicedata.SportsData;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.user.YsUser;
import sctek.cn.ysbracelet.utils.YsTextUtils;

/**
 * Created by kang on 16-3-17.
 */
public class FamilySportsMonthLvAdapter extends FamilyDataBaseLvAdapter{

    private final static String TAG = FamilySportsMonthLvAdapter.class.getSimpleName();

    public FamilySportsMonthLvAdapter(Context context, View emptyV, int offset) {
        super(context, emptyV, offset);
    }

    @Override
    protected List<YsData> getDataSet(int offset) {

        String dateStart = mDateManager.getFirstDayOfMonthBy(-offset);
        String dateEnd = mDateManager.getLastDayOfMonthBy(-offset);

        String[] projection = new String[] {LocalDataContract.Sports.COLUMNS_NAME_SPORTS_RUN
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_WALK
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_DEVICE};

        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.Sports.CONTENT_URI, projection
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME + ">=" + "'" + dateStart + "'"
                        + " AND " + LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME + "<" + "'" + dateEnd + "'"
                , null
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME + " desc");

        List<YsData> records = new ArrayList<>();
        while (cursor.moveToNext()) {
            SportsData data = new SportsData();
            data.runSteps = cursor.getInt(0);
            data.walkSteps = cursor.getInt(1);
            data.tempTime = cursor.getString(2);
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
        Log.e(TAG, "inflaterItemView");
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.family_sports_item_view, null);

            viewHolder = new ViewHolder();
            viewHolder.summaryV = convertView.findViewById(R.id.sports_summary_rl);
            viewHolder.detailV = convertView.findViewById(R.id.sports_detail_ll);

            viewHolder.name = (TextView)convertView.findViewById(R.id.name_tv);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time_tv);
            viewHolder.steps = (TextView)convertView.findViewById(R.id.total_steps_tv);
            viewHolder.calories = (TextView)convertView.findViewById(R.id.calories_tv);

            viewHolder.totalTimeTv = (TextView)convertView.findViewById(R.id.total_time_tv);
            viewHolder.caloriesTv = (TextView)convertView.findViewById(R.id.calories_tv);
            viewHolder.longestTv = (TextView)convertView.findViewById(R.id.longest_time_tv);
            viewHolder.runTv = (TextView)convertView.findViewById(R.id.run_tv);
            viewHolder.walkTv = (TextView)convertView.findViewById(R.id.walk_tv);
            viewHolder.breakTv = (TextView)convertView.findViewById(R.id.break_tv);

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

        SportsData data = (SportsData) mRecords.get(postion);

        DeviceInformation device = YsUser.getInstance().getDevice(data.deviceId);
        String name = device.getName();
        holder.name.setText(name);
        holder.time.setText(data.tempTime);



        int total = data.runSteps + data.walkSteps;
        holder.steps.setText("" + total);
        holder.runTv.setText("" + data.runSteps);
        holder.walkTv.setText("" + data.walkSteps);

        double calories = YsTextUtils.calculateCalories(data.runSteps, data.walkSteps, device.weight, device.height);
        holder.caloriesTv.setText(new DecimalFormat("#.00").format(calories));
        holder.calories.setText(new DecimalFormat("#.00").format(calories));
    }

    public class ViewHolder extends BaseViewHolder{

        public TextView name;
        public TextView time;
        public TextView steps;
        public TextView calories;

        public TextView totalTimeTv;
        public TextView caloriesTv;
        public TextView longestTv;
        public TextView runTv;
        public TextView walkTv;
        public TextView breakTv;

    }
}
