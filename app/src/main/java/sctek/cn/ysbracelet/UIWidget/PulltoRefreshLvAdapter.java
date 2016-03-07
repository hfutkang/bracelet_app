package sctek.cn.ysbracelet.UIWidget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import sctek.cn.ysbracelet.R;

/**
 * Created by kang on 16-3-4.
 */
public class PulltoRefreshLvAdapter extends BaseAdapter {

    private final static String TAG = PulltoRefreshLvAdapter.class.getSimpleName();

    private static int itemsViewResId[] = new int[]{R.layout.sports_item_view, R.layout.my_heart_rate_item_view, R.layout.sleep_item_view};

    private View[] itemsView = new View[3];

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public PulltoRefreshLvAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return itemsViewResId.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "getView" + " " + position);
        if(itemsView[position] == null) {
            convertView = mLayoutInflater.inflate(itemsViewResId[position], parent, false);
            itemsView[position] = convertView;
        }
        return itemsView[position];
    }
}
