package sctek.cn.ysbracelet.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.devicedata.Message;
import sctek.cn.ysbracelet.devicedata.YsData;

/**
 * Created by kang on 16-5-4.
 */
public class MessagesAdapter extends BaseAdapter {

    public static final String TAG = MessagesAdapter.class.getSimpleName();

    private List<YsData> messages;
    private Context mContext;

    public MessagesAdapter(Context context, List<YsData> msgs) {
        mContext = context;
        messages = msgs;
    }
    @Override
    public int getCount() {
        return messages.size();
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
        Log.e(TAG, "getView");
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item_view, null);
            holder = new ViewHolder();
            holder.logoIv = (ImageView)convertView.findViewById(R.id.logo_iv);
            holder.msgTv = (TextView)convertView.findViewById(R.id.msg_tv);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        Message message = (Message) messages.get(position);
        String msg = buildMsg(message);
        holder.msgTv.setText(msg);
        return convertView;
    }

    public class ViewHolder {
        public ImageView logoIv;
        public TextView msgTv;
    }

    private String buildMsg(Message msg) {
        String msgStr = null;
        if(msg.type.equals("sos")) {
            msgStr = String.format(mContext.getString(R.string.sos_msg), msg.name)
                    + "\n"
                    + mContext.getString(R.string.time) + msg.time;
        }
        else if(msg.type.equals("low_power")) {
            msgStr = String.format(mContext.getString(R.string.low_power_msg), msg.name)
                    + "\n"
                    + mContext.getString(R.string.time) + msg.time;
        }
        return msgStr;
    }
}
