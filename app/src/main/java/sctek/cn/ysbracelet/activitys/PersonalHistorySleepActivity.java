package sctek.cn.ysbracelet.activitys;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.PersonalHistorySleepLvAdapter;
import sctek.cn.ysbracelet.devicedata.SleepData;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

public class PersonalHistorySleepActivity extends PersonalHistoryDataBaseActivity {

    private final static String TAG = PersonalHistorySleepActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initViewElement() {
        super.initViewElement();

        actionBarV.setBackgroundColor(Color.BLUE);
        titleTv.setText(R.string.sleep_title);
        actionIb.setVisibility(View.GONE);

        recordsLv = (ListView)findViewById(R.id.history_sleep_lv);
        adapter = new PersonalHistorySleepLvAdapter(this, records);
        recordsLv.setAdapter(adapter);
        recordsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonalHistorySleepLvAdapter.ViewHolder viewHolder = (PersonalHistorySleepLvAdapter.ViewHolder) view.getTag();
                if(viewHolder.detailV.getVisibility() == View.GONE) {
                    viewHolder.detailV.setVisibility(View.VISIBLE);
                }
                else {
                    viewHolder.detailV.setVisibility(View.GONE);
                }
            }
        });

        emptyTv = (TextView)findViewById(R.id.empty_tv);
        emptyTv.setVisibility(View.GONE);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_personal_history_sleep);
    }

    @Override
    protected void loadData() {
        String[] projection = new String[] {LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_TOTALE
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEEP
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_SHALLOW
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_WAKE
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_END };

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.Sleep.CONTENT_URI
                , projection
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEVICE + "=" + "'" + deviceId + "'"
                , null
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + " desc");

        while (cursor.moveToNext()) {
            SleepData data = new SleepData();
            data.total = cursor.getInt(0);
            data.deep = cursor.getInt(1);
            data.shallow = cursor.getInt(2);
            data.wake = cursor.getInt(3);
            data.tempStartTime = cursor.getString(4);
            data.tempEndTime = cursor.getString(5);
            records.add(data);
        }
        cursor.close();
    }
}
