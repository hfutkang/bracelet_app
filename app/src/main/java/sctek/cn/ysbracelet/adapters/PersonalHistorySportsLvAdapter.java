package sctek.cn.ysbracelet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.devicedata.SportsData;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.utils.YsTextUtils;

/**
 * Created by kang on 16-3-18.
 */
public class PersonalHistorySportsLvAdapter extends BaseAdapter {

    private final static String TAG = PersonalHistorySportsLvAdapter.class.getSimpleName();

    private Context mContext;
    private List<YsData> mRecords;
    private DeviceInformation mDevice;

    public PersonalHistorySportsLvAdapter(Context context, List<YsData> records, DeviceInformation device) {
        mContext = context;
        mRecords = records;
        mDevice = device;
    }
    @Override
    public int getCount() {
        return mRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sports_item_view, null);

            viewHolder = new ViewHolder();
            viewHolder.summaryV = convertView.findViewById(R.id.sports_summary_rl);
            viewHolder.detailV = convertView.findViewById(R.id.sports_detail_ll);
            viewHolder.dayTv = (TextView) convertView.findViewById(R.id.day_tv);
            viewHolder.startTv = (TextView)convertView.findViewById(R.id.start_tv);
            viewHolder.endTv = (TextView)convertView.findViewById(R.id.end_tv);
            viewHolder.totalStepsTv = (TextView)convertView.findViewById(R.id.total_steps_tv);
            viewHolder.goalTv = (TextView)convertView.findViewById(R.id.goal_tv);
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

    private void setView(ViewHolder holder, int position) {
        holder.detailV.setVisibility(View.GONE);
        SportsData data = (SportsData)mRecords.get(position);

        String[] tempTime = data.tempTime.split(" ");
        holder.dayTv.setText(tempTime[0]);
        holder.endTv.setText(tempTime[1]);

        int total = data.runSteps + data.walkSteps;
        holder.totalStepsTv.setText("" + total);
        holder.runTv.setText("" + data.runSteps);
        holder.walkTv.setText("" + data.walkSteps);

        double calories = YsTextUtils.calculateCalories(data.runSteps, data.walkSteps, mDevice.weight, mDevice.height);
        holder.caloriesTv.setText(new DecimalFormat("#.00").format(calories));
    }

    public class ViewHolder {

        public View summaryV;
        public View detailV;

        public TextView startTv;
        public TextView endTv;
        public TextView dayTv;
        public TextView totalStepsTv;
        public TextView goalTv;

        public TextView totalTimeTv;
        public TextView caloriesTv;
        public TextView longestTv;
        public TextView runTv;
        public TextView walkTv;
        public TextView breakTv;

    }
}
