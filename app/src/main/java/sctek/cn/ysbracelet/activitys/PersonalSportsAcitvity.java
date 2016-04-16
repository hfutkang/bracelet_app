package sctek.cn.ysbracelet.activitys;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.fragments.HomeFragment;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.user.YsUser;
import sctek.cn.ysbracelet.utils.YsTextUtils;

public class PersonalSportsAcitvity extends PersonalLatestDataBaseActivity {

    private final static String TAG = PersonalSportsAcitvity.class.getSimpleName();

    private TextView startTv;
    private TextView endTv;
    private TextView dayTv;
    private TextView totalStepsTv;
    private TextView goalTv;
    private TextView totalTimeTv;
    private TextView caloriesTv;
    private TextView longestTv;
    private TextView runTv;
    private TextView walkTv;
    private TextView breakTv;

    private Button goalIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_personal_sports);
    }

    @Override
    protected void loadLatestRecord() {
        if(deviceId == null) {
            return;
        }

        String[] projection = new String[] {LocalDataContract.Sports.COLUMNS_NAME_SPORTS_RUN
                                        , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_WALK
                                        , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME};

        DeviceInformation device = YsUser.getInstance().getDevice(deviceId);

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.Sports.CONTENT_URI
                , projection
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_DEVICE + "=" + "'" + deviceId + "'"
                , null
                , LocalDataContract.Sports.COLUMNS_NAME_SPORTS_TIME + " desc limit 1");

        if(cursor.moveToFirst()) {
            String time = cursor.getString(2);
            String[] tempTime = time.split(" ");
            dayTv.setText(tempTime[0]);
            endTv.setText(tempTime[1]);

            int runSteps = cursor.getInt(0);
            int walkSteps = cursor.getInt(1);
            int totalSteps = runSteps + walkSteps;

            walkTv.setText(walkSteps + "");
            runTv.setText(runSteps + "");
            totalStepsTv.setText(totalSteps + "");

            double calories = YsTextUtils.calculateCalories(runSteps, walkSteps, device.weight, device.height);
            caloriesTv.setText(new DecimalFormat("#.00").format(calories));

        }

        cursor.close();
    }

    protected void initViewElement() {
        super.initViewElement();

        actionBarV.setBackgroundColor(Color.YELLOW);
        titleTv.setText(R.string.sports_title);
        actionIb.setOnClickListener(onViewClickedListener);
        backIb.setOnClickListener(onViewClickedListener);

        goalIb = (Button)findViewById(R.id.goal_ib);
        goalIb.setOnClickListener(onViewClickedListener);

        startTv = (TextView)findViewById(R.id.start_tv);
        endTv = (TextView)findViewById(R.id.end_tv);
        dayTv = (TextView)findViewById(R.id.day_tv);
        totalStepsTv = (TextView)findViewById(R.id.total_steps_tv);
        goalTv = (TextView)findViewById(R.id.goal_tv);
        totalTimeTv = (TextView)findViewById(R.id.total_time_tv);
        caloriesTv = (TextView)findViewById(R.id.calories_tv);
        longestTv = (TextView)findViewById(R.id.longest_time_tv);
        runTv = (TextView)findViewById(R.id.run_tv);
        walkTv = (TextView)findViewById(R.id.walk_tv);
        breakTv = (TextView)findViewById(R.id.break_tv);

    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.action_ib:
                    Intent intent = new Intent(PersonalSportsAcitvity.this, PersonalHistorySportsActivity.class);
                    intent.putExtra(HomeFragment.EXTR_DEVICE_ID, deviceId);
                    startActivity(intent);
                    break;
                case R.id.goal_ib:
                    break;
                case R.id.nav_back_ib:
                    break;
            }
        }
    };
}
