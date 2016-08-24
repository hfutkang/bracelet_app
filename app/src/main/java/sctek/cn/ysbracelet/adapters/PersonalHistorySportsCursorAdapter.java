package sctek.cn.ysbracelet.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.activitys.PersonalLatestDataBaseActivity;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.devicedata.SportsData;
import sctek.cn.ysbracelet.utils.YsTextUtils;

/**
 * Created by kang on 16-3-18.
 */
public class PersonalHistorySportsCursorAdapter extends CursorAdapter {

    private final static String TAG = PersonalHistorySportsCursorAdapter.class.getSimpleName();

    private DeviceInformation mDevice;
    private int goal;

    public PersonalHistorySportsCursorAdapter(Context context, Cursor c, boolean autoRequery, DeviceInformation device) {
        super(context, c, autoRequery);
        mDevice = device;
        SharedPreferences preferences = context.getSharedPreferences(PersonalLatestDataBaseActivity.PREFERENCE_NAME, Context.MODE_PRIVATE);
        goal = preferences.getInt(mDevice.serialNumber + "_sports_goal", 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.sports_item_view, null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.summaryV = view.findViewById(R.id.sports_summary_rl);
        viewHolder.detailV = view.findViewById(R.id.sports_detail_ll);
        viewHolder.dayTv = (TextView) view.findViewById(R.id.day_tv);
        viewHolder.startTv = (TextView)view.findViewById(R.id.start_tv);
        viewHolder.endTv = (TextView)view.findViewById(R.id.end_tv);
        viewHolder.totalStepsTv = (TextView)view.findViewById(R.id.total_steps_tv);
        viewHolder.goalTv = (TextView)view.findViewById(R.id.goal_tv);
        viewHolder.totalTimeTv = (TextView)view.findViewById(R.id.total_time_tv);
        viewHolder.caloriesTv = (TextView)view.findViewById(R.id.calories_tv);
        viewHolder.longestTv = (TextView)view.findViewById(R.id.longest_time_tv);
        viewHolder.runTv = (TextView)view.findViewById(R.id.run_tv);
        viewHolder.walkTv = (TextView)view.findViewById(R.id.walk_tv);
        viewHolder.breakTv = (TextView)view.findViewById(R.id.break_tv);

        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        setView((ViewHolder)view.getTag(), cursor);
    }

    private void setView(ViewHolder holder, Cursor cursor) {
        holder.detailV.setVisibility(View.GONE);
        SportsData data = new SportsData();

        data.runSteps = cursor.getInt(0);
        data.walkSteps = cursor.getInt(1);
        data.tempTime = cursor.getString(2);

        holder.dayTv.setText(data.tempTime);

        int total = data.runSteps + data.walkSteps;
        holder.totalStepsTv.setText("" + total);
        holder.runTv.setText("" + data.runSteps);
        holder.walkTv.setText("" + data.walkSteps);
        holder.goalTv.setText("" + goal);

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
