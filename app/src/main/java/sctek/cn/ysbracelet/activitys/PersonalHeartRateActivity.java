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
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.fragments.HomeFragment;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.user.UserManagerUtils;
import sctek.cn.ysbracelet.utils.DialogUtils;

public class PersonalHeartRateActivity extends PersonalLatestDataBaseActivity implements HttpConnectionWorker.ConnectionWorkListener{

    private final static String TAG = PersonalHeartRateActivity.class.getSimpleName();

    private TextView timeTv;
    private TextView rateTv;
    private Button startIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initViewElement() {
        super.initViewElement();

        actionBarV.setBackgroundColor(Color.RED);
        titleTv.setText(R.string.heart_rate_title);
        actionIb.setOnClickListener(onViewClickedListener);
        backIb.setOnClickListener(onViewClickedListener);

        timeTv = (TextView)findViewById(R.id.time_tv);

        rateTv = (TextView)findViewById(R.id.rate_tv);
        startIb = (Button) findViewById(R.id.start_measure_ib);
        startIb.setOnClickListener(onViewClickedListener);

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_heart_rate);
    }

    @Override
    protected void loadLatestRecord() {

        if(deviceId == null) {
            return;
        }

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(LocalDataContract.HeartRate.CONTENT_URI
                        , new String[]{LocalDataContract.HeartRate.COLUMNS_NAME_RATE, LocalDataContract.HeartRate.COLUMNS_NAME_TIME}
                        , LocalDataContract.HeartRate.COLUMNS_NAME_DEVICE + "=" + "'" + deviceId + "'"
                        , null
                        , LocalDataContract.HeartRate.COLUMNS_NAME_TIME + " desc limit 1");

        if(cursor.moveToFirst()) {
            timeTv.setText(cursor.getString(1));
            rateTv.setText("" + cursor.getInt(0));
        }

        cursor.close();

    }

    @Override
    public void onWorkDone(int resCode) {

        if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS){
            DialogUtils.makeToast(this, R.string.send_measure_command_success);
        }
        else if(resCode == XmlNodes.RESPONSE_CODE_FAIL) {
            DialogUtils.makeToast(this, R.string.send_measure_command_fail);
        }

   }

    @Override
    public void onResult(YsData result) {

    }

    @Override
    public void onError(Exception e) {
        DialogUtils.makeToast(this, R.string.send_measure_command_fail);
    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nav_back_ib:

                    break;
                case R.id.action_ib:
                    Intent intent = new Intent(PersonalHeartRateActivity.this, PersonalHistoryHRateActivity.class);
                    intent.putExtra(HomeFragment.EXTR_DEVICE_ID, deviceId);
                    startActivity(intent);
                    break;
                case R.id.start_measure_ib:
                    UserManagerUtils.startMeasureHeartRate(deviceId, PersonalHeartRateActivity.this);
                    break;
            }
        }
    };

}
