package sctek.cn.ysbracelet.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.devicedata.HeartRateData;

/**
 * Created by kang on 16-3-17.
 */
public class PersonalHistoryHRateCursorAdapter extends CursorAdapter {

    private final static String TAG = PersonalHistoryHRateCursorAdapter.class.getSimpleName();

    public PersonalHistoryHRateCursorAdapter(Context context, Cursor c, boolean autoQurey) {
        super(context, c, autoQurey);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_hrate_item_view, null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.rate = (TextView)view.findViewById(R.id.rate_tv);
        viewHolder.type = (TextView)view.findViewById(R.id.measure_type_tv);
        viewHolder.time = (TextView)view.findViewById(R.id.time_tv);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();
        HeartRateData data = new HeartRateData();
        data.rate = cursor.getInt(0);
        data.tempTime = cursor.getString(1);
        data.type = cursor.getString(2);

        holder.rate.setText("" + data.rate);
        holder.time.setText(data.tempTime);
        holder.type.setText(data.type);
    }

    private class ViewHolder {
        TextView rate;
        TextView type;
        TextView time;
    }
}
