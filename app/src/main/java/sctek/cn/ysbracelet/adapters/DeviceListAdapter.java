package sctek.cn.ysbracelet.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.activitys.SearchDeviceActivity;
import sctek.cn.ysbracelet.user.YsUser;

/**
 * Created by kang on 16-4-15.
 */
public class DeviceListAdapter extends BaseAdapter {

    private final static String TAG = DeviceListAdapter.class.getSimpleName();

    private Context mContext;
    private List<SearchDeviceActivity.BleDevice> mDevices;
    private Handler mHandler;

    public DeviceListAdapter(Context context, List<SearchDeviceActivity.BleDevice> devices, Handler handler) {
        mContext = context;
        mDevices = devices;
        mHandler = handler;
    }

    @Override
    public int getCount() {
        return mDevices.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ble_device_item_view, null);

            viewHolder = new ViewHolder();
            viewHolder.nameTv = (TextView)convertView.findViewById(R.id.name_tv);
            viewHolder.macTv = (TextView)convertView.findViewById(R.id.mac_tv);
            viewHolder.bondTv = (TextView)convertView.findViewById(R.id.bond_tv);
            viewHolder.bondBt = (Button)convertView.findViewById(R.id.bond_bt);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        SearchDeviceActivity.BleDevice device = mDevices.get(position);
        viewHolder.nameTv.setText(device.name);
        viewHolder.macTv.setText(device.mac);
        if(YsUser.getInstance().getDeviceByMac(device.mac) != null) {
            viewHolder.bondTv.setVisibility(View.VISIBLE);
            viewHolder.bondBt.setVisibility(View.GONE);
        }
        else {
            viewHolder.bondTv.setVisibility(View.GONE);
            viewHolder.bondBt.setVisibility(View.VISIBLE);
            viewHolder.bondBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = mHandler.obtainMessage(SearchDeviceActivity.DEVICE_BOND_BUTTON_CLICKED, position, 0);
                    msg.sendToTarget();
                }
            });
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView nameTv;
        public TextView macTv;
        public TextView bondTv;
        public Button bondBt;
    }
}
