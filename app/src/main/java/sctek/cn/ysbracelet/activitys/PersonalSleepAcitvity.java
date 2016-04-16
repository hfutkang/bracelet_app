package sctek.cn.ysbracelet.activitys;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.fragments.HomeFragment;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.utils.YsTextUtils;

public class PersonalSleepAcitvity extends PersonalLatestDataBaseActivity {

    private final static String TAG = PersonalSleepAcitvity.class.getSimpleName();

    private TextView startTv;
    private TextView endTv;
    private TextView dayTv;
    private TextView totalTimeTv;
    private TextView goalTv;
    private TextView deepTv;
    private TextView shallowTv;
    private TextView fallingSleepTv;
    private TextView lieTimeTv;
    private TextView awakeTv;
    private TextView wakeTimesTv;

    private Button goalIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_personal_sleep);
    }

    @Override
    protected void loadLatestRecord() {
        if(deviceId == null) {
            return;
        }

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
                , LocalDataContract.Sleep.COLUMNS_NAME_SLEEP_START + " desc limit 1");

        if(cursor.moveToFirst()) {

            String tempStart = cursor.getString(4);
            String tempEnd = cursor.getString(5);
            String[] tempS = tempStart.split(" ");
            String[] tempE = tempEnd.split(" ");

            startTv.setText(tempS[1]);
            dayTv.setText(tempE[0]);
            endTv.setText(tempE[1]);

            int total = cursor.getInt(0);
            int deep = cursor.getInt(1);
            int shallow = cursor.getInt(2);
            int wake = cursor.getInt(3);

            wakeTimesTv.setText(wake + "");
            totalTimeTv.setText(YsTextUtils.paserHourForMinute(this, total));
            deepTv.setText(YsTextUtils.paserHourForMinute(this, deep));
            shallowTv.setText(YsTextUtils.paserHourForMinute(this, shallow));


        }

        cursor.close();
    }

    protected void initViewElement() {
        super.initViewElement();

        actionBarV.setBackgroundColor(Color.BLUE);
        titleTv.setText(R.string.sleep_title);
        actionIb.setOnClickListener(onViewClickedListener);
        backIb.setOnClickListener(onViewClickedListener);

        goalIb = (Button) findViewById(R.id.goal_ib);
        goalIb.setOnClickListener(onViewClickedListener);

        startTv = (TextView)findViewById(R.id.start_tv);
        endTv = (TextView)findViewById(R.id.end_tv);
        dayTv = (TextView)findViewById(R.id.day_tv);
        totalTimeTv = (TextView)findViewById(R.id.total_time_tv);
        goalTv = (TextView)findViewById(R.id.goal_tv);
        deepTv = (TextView)findViewById(R.id.deep_time_tv);
        shallowTv = (TextView)findViewById(R.id.shallow_time_tv);
        fallingSleepTv = (TextView)findViewById(R.id.fall_time_tv);
        lieTimeTv = (TextView)findViewById(R.id.lie_time_tv);
        awakeTv = (TextView)findViewById(R.id.awake_time_tv);
        wakeTimesTv = (TextView)findViewById(R.id.wake_times_tv);

    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.action_ib:
                    Intent intent = new Intent(PersonalSleepAcitvity.this, PersonalHistorySleepActivity.class);
                    intent.putExtra(HomeFragment.EXTR_DEVICE_ID, deviceId);
                    startActivity(intent);
                    break;
                case R.id.goal_ib:
                    break;
                case R.id.nav_back_ib:
                    onBackPressed();
                    break;
            }
        }
    };
}
