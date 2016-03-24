package sctek.cn.ysbracelet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.R;

/**
 * Created by kang on 16-3-18.
 */
public class PersonalHistorySleepLvAdapter extends BaseAdapter {

    private final static String TAG = PersonalHistorySleepLvAdapter.class.getSimpleName();

    private Context mContext;
    private List<Object> records;

    public PersonalHistorySleepLvAdapter(Context context, List<Object> objects) {
        mContext = context;
        records = new ArrayList<>();
        records.add(new Object());
        records.add(new Object());
        records.add(new Object());
        records.add(new Object());
        records.add(new Object());
        records.add(new Object());
        records.add(new Object());
        records.add(new Object());
    }
    @Override
    public int getCount() {
        return records.size();
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

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.detailV.setVisibility(View.GONE);

        return convertView;
    }

    public class ViewHolder {

        public View summaryV;
        public View detailV;

        public TextView startTv;
        public TextView endTv;
        public TextView dayTv;
        public TextView totalStepsTv;
        public TextView goalTv;

    }
}
