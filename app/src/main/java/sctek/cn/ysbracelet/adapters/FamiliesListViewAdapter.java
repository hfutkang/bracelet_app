package sctek.cn.ysbracelet.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.uiwidget.CircleImageView;
import sctek.cn.ysbracelet.user.YsUser;

/**
 * Created by kang on 16-3-16.
 */
public class FamiliesListViewAdapter extends BaseAdapter {

    private final static String TAG = FamiliesListViewAdapter.class.getSimpleName();

    private boolean withAddButton;
    private YsUser user;
    private Context mContext;

    public FamiliesListViewAdapter(Context context, boolean withAddBt) {
        withAddButton = withAddBt;
        user = YsUser.getInstance();
        mContext = context;
    }

    @Override
    public int getCount() {
        return withAddButton ? user.getDeviceCount() + 1 : user.getDeviceCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "getView");
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fimaly_member_item_view, null);
            viewHolder.imageView = (CircleImageView)convertView.findViewById(R.id.face_cv);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(position + 1 > user.getDeviceCount()) {
            viewHolder.imageView.setBorderColor(Color.YELLOW);
            viewHolder.imageView.setProgress(100);
            viewHolder.imageView.setImageResource(R.drawable.add_bracelet);
        }
        else {
            viewHolder.imageView.setProgress(user.getDevice(position).getPower());
            ImageLoader.getInstance().displayImage(user.getDevice(position).getImagePath(), viewHolder.imageView);
            viewHolder.imageView.setImageResource(R.drawable.gravatar_stub);
        }
        return convertView;
    }

    private class ViewHolder {
        CircleImageView imageView;
    }
}
