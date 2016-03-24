package sctek.cn.ysbracelet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.devicedata.HeartRateData;

/**
 * Created by kang on 16-3-10.
 */
public class HRateListViewAdapter extends BaseAdapter{

    private final static String TAG = HRateListViewAdapter.class.getSimpleName();

    private String[] weekDays;

    private List<HeartRateData> dataList;
    private Context mContext;
    private YsDateManager dateManager;

    public HRateListViewAdapter(Context context, List<HeartRateData> data) {
        mContext = context;
        dataList = data;
        weekDays = mContext.getResources().getStringArray(R.array.weekdays);
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
            holder.rateTv = (TextView)convertView.findViewById(R.id.rate_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        dateManager.setDate(dataList.get(position).time);
        holder.dateTv.setText(dateManager.getCurrentDate());
        holder.weekdayTv.setText(dateManager.getDayOfWeek() + "");
        holder.rateTv.setText(dataList.get(position).rate + "");

        return convertView;
    }

    private class ViewHolder {
        TextView dateTv;
        TextView weekdayTv;
        TextView rateTv;
    }
}
