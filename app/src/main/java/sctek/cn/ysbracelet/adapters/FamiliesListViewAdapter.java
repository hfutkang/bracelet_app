package sctek.cn.ysbracelet.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.uiwidget.CircleImageView;
import sctek.cn.ysbracelet.user.YsUser;

/**
 * Created by kang on 16-3-16.
 */
public class FamiliesListViewAdapter extends BaseAdapter {

    private final static String TAG = FamiliesListViewAdapter.class.getSimpleName();

    private boolean withAddButton;
    private List<DeviceInformation> devices;
    private Context mContext;

    private DisplayImageOptions displayImageOptions;

    public FamiliesListViewAdapter(Context context, boolean withAddBt) {
        withAddButton = withAddBt;
        devices = YsUser.getInstance().getDevices();
        mContext = context;

        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.gravatar_stub)
                .showImageOnFail(R.drawable.gravatar_stub)
                .showImageForEmptyUri(R.drawable.gravatar_stub)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @Override
    public int getCount() {
        return withAddButton ? devices.size() + 1 : devices.size();
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

        if(position + 1 > devices.size()) {
            viewHolder.imageView.setBorderColor(Color.YELLOW);
            viewHolder.imageView.setProgress(100);
            viewHolder.imageView.setImageResource(R.drawable.add_bracelet);
        }
        else {
            viewHolder.imageView.setProgress(devices.get(position).getPower());
            ImageLoader.getInstance().displayImage(devices.get(position).getImagePath(), viewHolder.imageView
                                        ,displayImageOptions);
//            viewHolder.imageView.setImageResource(R.drawable.gravatar_stub);
        }
        return convertView;
    }

    private class ViewHolder {
        CircleImageView imageView;
    }
}
