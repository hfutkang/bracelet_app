package sctek.cn.ysbracelet.activitys;

import android.content.CursorLoader;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.PersonalHistorySportsCursorAdapter;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.user.YsUser;

public class PersonalHistorySportsActivity extends PersonalHistoryDataBaseActivity {

    private final static String TAG = PersonalHistorySportsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initViewElement() {
        super.initViewElement();

        actionBarV.setBackgroundColor(Color.YELLOW);
        titleTv.setText(R.string.sports_title);
        actionIb.setVisibility(View.VISIBLE);

        recordsLv = (ListView)findViewById(R.id.history_sports_lv);
        DeviceInformation device = YsUser.getInstance().getDevice(deviceId);
        adapter = new PersonalHistorySportsCursorAdapter(this, null, true, device);
        recordsLv.setAdapter(adapter);
        recordsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonalHistorySportsCursorAdapter.ViewHolder viewHolder = (PersonalHistorySportsCursorAdapter.ViewHolder) view.getTag();
                if(viewHolder.detailV.getVisibility() == View.GONE) {
                    viewHolder.detailV.setVisibility(View.VISIBLE);
                }
                else {
                    viewHolder.detailV.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_personal_history_sports);
    }

    @Override
    protected Loader createLoader() {
        String[] projection = new String[] {LocalDataContract.Sports.COLUMNS_NAME_SPORTS_RUN
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_WALK
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME
                , LocalDataContract.Sports._ID};
        String selection = LocalDataContract.Sports.COLUMNS_NAME_SPORTS_DEVICE + "=" + "'" + deviceId + "'";
        String order = LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME + " desc";
        return new CursorLoader(this, LocalDataContract.Sports.CONTENT_URI, projection, selection, null, order);
    }

}
