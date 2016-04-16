package sctek.cn.ysbracelet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.devicedata.SleepData;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.utils.YsTextUtils;

/**
 * Created by kang on 16-3-18.
 */
public class PersonalHistorySleepLvAdapter extends BaseAdapter {

    private final static String TAG = PersonalHistorySleepLvAdapter.class.getSimpleName();

    private Context mContext;
    private List<YsData> mRecords;

    public PersonalHistorySleepLvAdapter(Context context, List<YsData> records) {
        mContext = context;
        mRecords = records;

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sleep_item_view, null);

            viewHolder = new ViewHolder();
            viewHolder.summaryV = convertView.findViewById(R.id.sleep_summary_rl);
            viewHolder.detailV = convertView.findViewById(R.id.sleep_detail_ll);

            viewHolder.startTv = (TextView)convertView.findViewById(R.id.start_tv);
            viewHolder.endTv = (TextView)convertView.findViewById(R.id.end_tv);
            viewHolder.dayTv = (TextView)convertView.findViewById(R.id.day_tv);
            viewHolder.totalTimeTv = (TextView)convertView.findViewById(R.id.total_time_tv);
            viewHolder.goalTv = (TextView)convertView.findViewById(R.id.goal_tv);
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
        String[] tempS = data.tempStartTime.split(" ");
        String[] tempE = data.tempEndTime.split(" ");

        holder.startTv.setText(tempS[1]);
        holder.dayTv.setText(tempE[0]);
        holder.endTv.setText(tempE[1]);

        holder.totalTimeTv.setText(YsTextUtils.paserHourForMinute(mContext, data.total));
        holder.deepTv.setText(YsTextUtils.paserHourForMinute(mContext, data.deep));
        holder.shallowTv.setText(YsTextUtils.paserHourForMinute(mContext, data.shallow));
        holder.wakeTimesTv.setText("" + data.wake);
    }

    public class ViewHolder {

        public View summaryV;
        public View detailV;

        public TextView startTv;
        public TextView endTv;
        public TextView dayTv;
        public TextView totalTimeTv;
        public TextView goalTv;

        public TextView deepTv;
        public TextView shallowTv;
        public TextView fallingTv;
        public TextView lieTimeTv;
        public TextView awakeTv;
        public TextView wakeTimesTv;

    }
}
