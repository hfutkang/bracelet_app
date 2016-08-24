package sctek.cn.ysbracelet.activitys;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.PersonalHistorySleepCursorAdapter;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;

public class PersonalHistorySleepActivity extends PersonalHistoryDataBaseActivity implements LoaderManager.LoaderCallbacks{

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
        adapter = new PersonalHistorySleepCursorAdapter(this, null, true, deviceId);
        recordsLv.setAdapter(adapter);
        recordsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonalHistorySleepCursorAdapter.ViewHolder viewHolder = (PersonalHistorySleepCursorAdapter.ViewHolder) view.getTag();
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
    protected Loader createLoader() {
        String[] projection = new String[]{LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_TOTALE
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEEP
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_SHALLOW
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_WAKE
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_END
                , LocalDataContract.Sleep._ID};
        String selection = LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_DEVICE + "=" + "'" + deviceId + "'";
        String order = LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + " desc";
        return new CursorLoader(this, LocalDataContract.Sleep.CONTENT_URI, projection, selection, null, order);
    }
}
