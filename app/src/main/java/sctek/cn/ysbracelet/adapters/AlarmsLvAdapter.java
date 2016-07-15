package sctek.cn.ysbracelet.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.activitys.WarnActivity;
import sctek.cn.ysbracelet.devicedata.YsTimer;

/**
 * Created by kang on 16-3-19.
 */
public class AlarmsLvAdapter extends BaseAdapter{

    private final static String TAG = AlarmsLvAdapter.class.getSimpleName();

    private List<YsTimer> alarms;
    private Context mContext;
    private Handler mHandler;

    public AlarmsLvAdapter(Context context, List<YsTimer> ams, Handler handler) {
        mContext = context;
        this.alarms = ams;
        mHandler = handler;
    }
    @Override
    public int getCount() {
        return alarms.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.alarm_item_view, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)convertView.findViewById(R.id.alarm_name_tv);
            viewHolder.time = (TextView)convertView.findViewById(R.id.alarm_time_tv);
            viewHolder.sw = (Switch) convertView.findViewById(R.id.alarm_sw);
            viewHolder.delete = (ImageButton)convertView.findViewById(R.id.alarm_delete_ib);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        YsTimer timer = alarms.get(position);
        viewHolder.name.setText(timer.description);
        viewHolder.time.setText(parseTimeStr(timer));
        viewHolder.sw.setChecked(timer.status);

        viewHolder.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            int index = position;
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mHandler.obtainMessage(WarnActivity.ON_TIMER_SWITCH_CHANGED, index, 0).sendToTarget();
            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            int index = position;
            @Override
            public void onClick(View v) {
                mHandler.obtainMessage(WarnActivity.ON_DELETE_BUTTON_CLICKED, index, 0).sendToTarget();
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public TextView name;
        public TextView time;
        public Switch sw;
        public ImageButton delete;
    }

    private String parseTimeStr(YsTimer timer) {
        String timeStr;
        timeStr = (timer.hour < 10? "0" + timer.hour:"" + timer.hour) + ":";
        timeStr += timer.minutes < 10?"0" + timer.minutes:"" + timer.minutes;
        return timeStr;
    }
}
