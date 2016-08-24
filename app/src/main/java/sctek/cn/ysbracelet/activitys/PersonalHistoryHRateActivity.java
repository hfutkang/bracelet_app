package sctek.cn.ysbracelet.activitys;

import android.content.CursorLoader;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.PersonalHistoryHRateCursorAdapter;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

public class PersonalHistoryHRateActivity extends PersonalHistoryDataBaseActivity{

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
        adapter = new PersonalHistoryHRateCursorAdapter(this, null, true);
        recordsLv.setAdapter(adapter);
    }

    @Override
    protected Loader createLoader() {
        String[] projection = new String[]{LocalDataContract.HeartRate.COLUMNS_NAME_RATE
                , LocalDataContract.HeartRate.COLUMNS_NAME_TIME
                , LocalDataContract.HeartRate.COLUMNS_NAME_TYPE
                , LocalDataContract.HeartRate._ID};
        String selection = LocalDataContract.HeartRate.COLUMNS_NAME_DEVICE + "=" + "'" + deviceId + "'";
        String order = LocalDataContract.HeartRate.COLUMNS_NAME_TIME + " desc";
        return new CursorLoader(this,LocalDataContract.HeartRate.CONTENT_URI, projection, selection, null, order);
    }

}
