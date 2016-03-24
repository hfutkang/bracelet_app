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

/**
 * Created by kang on 16-3-17.
 */
public class PersonalHistoryHRateLvAdapter extends BaseAdapter{

    private final static String TAG = PersonalHistoryHRateLvAdapter.class.getSimpleName();

    private List<Object> records;
    private Context mContext;

    private YsDateManager dateManager;

    public PersonalHistoryHRateLvAdapter(Context context, List<Object> objects) {
        records = objects;
        mContext = context;
        dateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SHOW2);
    }
    @Override
    public int getCount() {
        return 5;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.history_hrate_item_view, null);

            viewHolder = new ViewHolder();
            viewHolder.rate = (TextView)convertView.findViewById(R.id.rate_tv);
            viewHolder.type = (TextView)convertView.findViewById(R.id.measure_type_tv);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time_tv);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        dateManager.showCurrentDate(viewHolder.time);
        return convertView;
    }

    private class ViewHolder {
        TextView rate;
        TextView type;
        TextView time;
    }
}
