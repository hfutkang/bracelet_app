package sctek.cn.ysbracelet.activitys;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.PersonalHistorySportsLvAdapter;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.devicedata.SportsData;
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
        adapter = new PersonalHistorySportsLvAdapter(this, records, device);
        recordsLv.setAdapter(adapter);
        recordsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonalHistorySportsLvAdapter.ViewHolder viewHolder = (PersonalHistorySportsLvAdapter.ViewHolder) view.getTag();
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
    protected void loadData() {
        String[] projection = new String[] {LocalDataContract.Sports.COLUMNS_NAME_SPORTS_RUN
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_WALK
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME};

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.Sports.CONTENT_URI
                , projection
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_DEVICE + "=" + "'" + deviceId + "'"
                , null
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME + " desc");

        while (cursor.moveToNext()) {
            SportsData data = new SportsData();
            data.runSteps = cursor.getInt(0);
            data.walkSteps = cursor.getInt(1);
            data.tempTime = cursor.getString(2);
            records.add(data);
        }
        cursor.close();
    }
}
