package sctek.cn.ysbracelet.activitys;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
            dayTv.setText(time);

            int runSteps = cursor.getInt(0);
            int walkSteps = cursor.getInt(1);
            int totalSteps = runSteps + walkSteps;

            walkTv.setText(walkSteps + "");
            runTv.setText(runSteps + "");
            totalStepsTv.setText(totalSteps + "");

            double calories = YsTextUtils.calculateCalories(runSteps, walkSteps, device.weight, device.height);
            caloriesTv.setText(new DecimalFormat("#.00").format(calories));

        }
        else {
            dayTv.setText(R.string.no_data_yet);
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

        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        String goal = preferences.getInt(deviceId + "_sports_goal", 0) + "";
        goalTv.setText(goal);

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
                    showGoalDialog();
                    break;
                case R.id.nav_back_ib:
                    onBackPressed();
                    break;
            }
        }
    };

    protected void showGoalDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.goal_dialog_view, null);
        final EditText goalEt = (EditText)view.findViewById(R.id.goal_et);

        goalEt.setText(goalTv.getText());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle(R.string.goal);
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String goal = goalEt.getText().toString();
                if(goal.isEmpty())
                    goal = "0";
                int g = Integer.parseInt(goal);
                SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
                preferences.edit().putInt(deviceId + "_sports_goal", g).commit();
                goalTv.setText(g + "");
            }
        });
        builder.show();
    }
}
