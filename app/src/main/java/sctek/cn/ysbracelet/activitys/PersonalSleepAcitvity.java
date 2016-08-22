package sctek.cn.ysbracelet.activitys;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
            Log.e(TAG, deviceId + " S:" + tempStart + " E:" + tempEnd);
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
            totalTimeTv.setText(YsTextUtils.parseHourForMinute(this, total));
            deepTv.setText(YsTextUtils.parseHourForMinute(this, deep));
            shallowTv.setText(YsTextUtils.parseHourForMinute(this, shallow));

        }
        else {
            dayTv.setText(R.string.no_data_yet);
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

        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        int goal = preferences.getInt(deviceId + "_sleep_goal", 0);

        goalTv.setText(YsTextUtils.parseHourForMinute(this, goal));

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

        String goal = YsTextUtils.parseIntStr(goalTv.getText().toString());
        if(goal == null)
            goalEt.setText("0");
        else
            goalEt.setText(goal);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle(R.string.sleep_goal);
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e(TAG, "onOkButton clicked");
                try {
                    String goal = goalEt.getText().toString();
                    if (goal.isEmpty())
                        goal = "0";
                    int g = Integer.parseInt(goal);
                    SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
                    preferences.edit().putInt(deviceId + "_sleep_goal", g).commit();
                    goalTv.setText(YsTextUtils.parseHourForMinute(PersonalSleepAcitvity.this, g));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.show();
    }
}
