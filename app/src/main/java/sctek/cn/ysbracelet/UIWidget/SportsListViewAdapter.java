package sctek.cn.ysbracelet.UIWidget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.devicedata.SportsData;

/**
 * Created by kang on 16-3-11.
 */
public class SportsListViewAdapter extends BaseAdapter{

    private final static String TAG = SportsListViewAdapter.class.getSimpleName();

    private Context mContext;
    private List<SportsData> dataList;
    private YsDateManager dateManager;

    public SportsListViewAdapter(Context context, List<SportsData> datas) {
        mContext = context;
        dataList = datas;
        dateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SHOW2);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.history_hrate_item_view, null);
            holder = new ViewHolder();
            holder.dateTv = (TextView)convertView.findViewById(R.id.date_tv);
            holder.weekdayTv = (TextView)convertView.findViewById(R.id.weekday_tv);
            holder.run = (TextView)convertView.findViewById(R.id.run_steps_tv);
            holder.walk = (TextView)convertView.findViewById(R.id.walk_steps_tv);
            holder.calories = (TextView)convertView.findViewById(R.id.calories_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        dateManager.setDate(dataList.get(position).date);
        holder.dateTv.setText(dateManager.getCurrentDate());
        holder.weekdayTv.setText(dateManager.getDayOfWeek() + "");
        holder.run.setText(dataList.get(position).runSteps + "");
        holder.walk.setText(dataList.get(position).walkSteps + "");
        holder.calories.setText(dataList.get(position).calories + "");

        return convertView;
    }

    private class ViewHolder {
        TextView dateTv;
        TextView weekdayTv;
        TextView run;
        TextView walk;
        TextView calories;

    }
}
