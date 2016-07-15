package sctek.cn.ysbracelet.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.user.YsUser;

/**
 * Created by kang on 16-4-13.
 */
public abstract class FamilyDataBaseLvAdapter extends BaseAdapter{

    private final static String TAG = FamilyDataBaseLvAdapter.class.getSimpleName();

    protected Context mContext;
    protected List<DeviceInformation> mDevices;

    protected int pageOffset;

    protected YsDateManager mDateManager;

    protected List<YsData> mRecords;

    private View emptyV;

    public FamilyDataBaseLvAdapter(Context context, View view, int offset) {
        mContext = context;
        mDevices = YsUser.getInstance().getDevices();
        mDateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SECOND);
        pageOffset = offset;

        emptyV = view;

        mRecords = new ArrayList<>();
        loadDataAsyncTask.execute();
    }

    protected abstract List<YsData> getDataSet(int offset);
    protected abstract int getItemCount(int offset);
    protected abstract View inflaterItemView(int position, View convertView);

    @Override
    public int getCount() {
        return mRecords.size();
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

        return inflaterItemView(position, convertView);
    }

    public abstract class BaseViewHolder {
        View summaryV = null;
        View detailV = null;
    }

    private AsyncTask<Void, Void, Void> loadDataAsyncTask = new AsyncTask<Void, Void, Void>() {

        @Override
        protected Void doInBackground(Void... params) {
            Log.e(TAG, "doInBackground");
            mRecords = getDataSet(pageOffset);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e(TAG, "onPostExecute");
            if(mRecords.size() == 0)
                emptyV.setVisibility(View.VISIBLE);
            else {
                emptyV.setVisibility(View.GONE);
                notifyDataSetChanged();
            }
        }
    };
}
