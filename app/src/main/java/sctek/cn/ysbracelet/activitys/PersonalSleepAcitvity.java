package sctek.cn.ysbracelet.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;

public class PersonalSleepAcitvity extends AppCompatActivity {

    private final static String TAG = PersonalSleepAcitvity.class.getSimpleName();

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton historyIb;

    private View summaryV;
    private View detailV;

    private TextView startTv;
    private TextView endTv;
    private TextView dayTv;
    private TextView totalTimeTv;
    private TextView goalTv;

    private Button goalIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_sleep);
        initElementView();
    }

    private void initElementView() {

        actionBarV = findViewById(R.id.action_bar_rl);
        actionBarV.setBackgroundColor(Color.BLUE);
        titleTv = (TextView)findViewById(R.id.title_tv);
        titleTv.setText(R.string.sleep_title);
        historyIb = (ImageButton)findViewById(R.id.action_ib);
        historyIb.setOnClickListener(onViewClickedListener);

        backIb = (ImageButton)findViewById(R.id.nav_back_ib);

        summaryV = findViewById(R.id.sleep_summary_rl);
        detailV = findViewById(R.id.sleep_detail_ll);

        goalIb = (Button) findViewById(R.id.goal_ib);
        goalIb.setOnClickListener(onViewClickedListener);

    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.action_ib:
                    startActivity(new Intent(PersonalSleepAcitvity.this, PersonalHistorySleepActivity.class));
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
