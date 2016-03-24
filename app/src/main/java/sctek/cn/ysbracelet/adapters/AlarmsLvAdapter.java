package sctek.cn.ysbracelet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.R;

/**
 * Created by kang on 16-3-19.
 */
public class AlarmsLvAdapter extends BaseAdapter{

    private final static String TAG = AlarmsLvAdapter.class.getSimpleName();

    private List<Object> alarms;
    private Context mContext;

    public AlarmsLvAdapter(Context context, List<Object> ams) {
        mContext = context;
        this.alarms = new ArrayList<>();
        alarms.add(new Object());
        alarms.add(new Object());
        alarms.add(new Object());
        alarms.add(new Object());
        alarms.add(new Object());
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.alarm_item_view, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)convertView.findViewById(R.id.name_tv);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time_tv);
            viewHolder.sw = (Switch) convertView.findViewById(R.id.alarm_sw);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView name;
        public TextView time;
        public Switch sw;
    }
}
