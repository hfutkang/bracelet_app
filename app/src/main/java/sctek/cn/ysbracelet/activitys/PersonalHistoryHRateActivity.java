package sctek.cn.ysbracelet.activitys;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.PersonalHistoryHRateLvAdapter;
import sctek.cn.ysbracelet.devicedata.HeartRateData;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

public class PersonalHistoryHRateActivity extends PersonalHistoryDataBaseActivity {

    private final static String TAG = PersonalHistoryHRateActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_personal_history_hrate);
    }

    @Override
    protected void initViewElement() {
        super.initViewElement();

        actionBarV.setBackgroundColor(Color.RED);
        titleTv.setText(R.string.heart_rate_title);
        actionIb.setVisibility(View.GONE);

        recordsLv = (ListView)findViewById(R.id.history_hrate_lv);
        adapter = new PersonalHistoryHRateLvAdapter(this, records);
        recordsLv.setAdapter(adapter);

    }

    @Override
    protected void loadData() {

        String[] projection = new String[]{LocalDataContract.HeartRate.COLUMNS_NAME_RATE
                            , LocalDataContract.HeartRate.COLUMNS_NAME_TIME
                            , LocalDataContract.HeartRate.COLUMNS_NAME_TYPE };

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.HeartRate.CONTENT_URI, projection
                        , LocalDataContract.HeartRate.COLUMNS_NAME_DEVICE + "=?"
                        , new String[]{deviceId}
                        , LocalDataContract.HeartRate.COLUMNS_NAME_TIME + " desc");

        while (cursor.moveToNext()) {
            HeartRateData data = new HeartRateData();
            data.rate = cursor.getInt(0);
            data.tempTime = cursor.getString(1);
            data.type = cursor.getString(2);
            records.add(data);
        }
        cursor.close();
    }
}
