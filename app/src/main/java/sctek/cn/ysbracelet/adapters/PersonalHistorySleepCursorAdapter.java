package sctek.cn.ysbracelet.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.devicedata.SleepData;
import sctek.cn.ysbracelet.utils.YsTextUtils;

/**
 * Created by kang on 16-3-18.
 */
public class PersonalHistorySleepCursorAdapter extends CursorAdapter {

    private final static String TAG = PersonalHistorySleepCursorAdapter.class.getSimpleName();

    public PersonalHistorySleepCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.sleep_item_view, null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.summaryV = view.findViewById(R.id.sleep_summary_rl);
        viewHolder.detailV = view.findViewById(R.id.sleep_detail_ll);

        viewHolder.startTv = (TextView)view.findViewById(R.id.start_tv);
        viewHolder.endTv = (TextView)view.findViewById(R.id.end_tv);
        viewHolder.dayTv = (TextView)view.findViewById(R.id.day_tv);
        viewHolder.totalTimeTv = (TextView)view.findViewById(R.id.total_time_tv);
        viewHolder.goalTv = (TextView)view.findViewById(R.id.goal_tv);
        viewHolder.deepTv = (TextView)view.findViewById(R.id.deep_time_tv);
        viewHolder.shallowTv = (TextView)view.findViewById(R.id.shallow_time_tv);
        viewHolder.fallingTv = (TextView)view.findViewById(R.id.fall_time_tv);
        viewHolder.lieTimeTv = (TextView)view.findViewById(R.id.lie_time_tv);
        viewHolder.awakeTv = (TextView)view.findViewById(R.id.awake_time_tv);
        viewHolder.wakeTimesTv = (TextView)view.findViewById(R.id.wake_times_tv);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        SleepData data = new SleepData();
        data.total = cursor.getInt(0);
        data.deep = cursor.getInt(1);
        data.shallow = cursor.getInt(2);
        data.wake = cursor.getInt(3);
        data.tempStartTime = cursor.getString(4);
        data.tempEndTime = cursor.getString(5);
        data.goal = 0;

        String[] tempS = data.tempStartTime.split(" ");
        String[] tempE = data.tempEndTime.split(" ");

        holder.startTv.setText(tempS[1]);
        holder.dayTv.setText(tempE[0]);
        holder.endTv.setText(tempE[1]);

        holder.goalTv.setText(YsTextUtils.parseHourForMinute(context, data.goal));

        holder.totalTimeTv.setText(YsTextUtils.parseHourForMinute(context, data.total));
        holder.deepTv.setText(YsTextUtils.parseHourForMinute(context, data.deep));
        holder.shallowTv.setText(YsTextUtils.parseHourForMinute(context, data.shallow));
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
