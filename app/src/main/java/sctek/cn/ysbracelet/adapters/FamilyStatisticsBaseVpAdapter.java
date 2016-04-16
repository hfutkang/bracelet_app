package sctek.cn.ysbracelet.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.user.YsUser;

/**
 * Created by kang on 16-4-13.
 */
public abstract class FamilyStatisticsBaseVpAdapter extends PagerAdapter{

    private final static String TAG = FamilyStatisticsBaseVpAdapter.class.getSimpleName();

    protected Context mContext;
    protected YsDateManager mDateManager;
    protected List<DeviceInformation> mDevices;

    public FamilyStatisticsBaseVpAdapter(Context context) {
        mContext = context;
        mDevices = YsUser.getInstance().getDevices();
        mDateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SECOND);
    }

    protected abstract int getPageCount();
    protected abstract BaseAdapter constructLvAdapter(Context context, int postion);

    @Override
    public int getCount() {
        return getPageCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.family_viewpager_item_view, null);
        ListView lv = (ListView)view.findViewById(R.id.data_lv);
        lv.setAdapter(constructLvAdapter(mContext, position));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FamilyStatisticsBaseLvAdapter.ViewHolder viewHolder = (FamilyStatisticsBaseLvAdapter.ViewHolder) view.getTag();
                if(viewHolder.lineChart.getVisibility() == View.GONE) {
                    viewHolder.lineChart.setVisibility(View.VISIBLE);
                }
                else {
                    viewHolder.lineChart.setVisibility(View.GONE);
                }
            }
        });
        container.addView(view);
        return view;
    }


}
