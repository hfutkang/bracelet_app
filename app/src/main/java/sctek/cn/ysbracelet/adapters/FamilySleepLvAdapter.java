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
public class FamilySleepLvAdapter extends BaseAdapter{

    private final static String TAG = FamilySleepLvAdapter.class.getSimpleName();

    private List<Object> records;
    private Context mContext;

    private YsDateManager dateManager;

    public FamilySleepLvAdapter(Context context, List<Object> objects) {
        records = objects;
        mContext = context;
        dateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SECOND);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.family_sleep_item_view, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)convertView.findViewById(R.id.name_tv);
            viewHolder.totalTime = (TextView)convertView.findViewById(R.id.total_time_tv);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time_tv);

            viewHolder.summaryV = convertView.findViewById(R.id.sleep_summary_rl);
            viewHolder.detailV = convertView.findViewById(R.id.sleep_detail_ll);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        dateManager.showCurrentDate(viewHolder.time);
        return convertView;
    }

    public class ViewHolder {

        public View summaryV;
        public View detailV;

        public TextView name;
        public TextView time;
        public TextView totalTime;
    }
}
