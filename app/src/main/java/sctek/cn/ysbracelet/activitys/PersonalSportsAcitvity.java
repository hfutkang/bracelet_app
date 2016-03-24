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

public class PersonalSportsAcitvity extends AppCompatActivity {

    private final static String TAG = PersonalSportsAcitvity.class.getSimpleName();

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton historyIb;

    private View summaryV;
    private View detailV;

    private TextView startTv;
    private TextView endTv;
    private TextView dayTv;
    private TextView totalStepsTv;
    private TextView goalTv;

    private Button goalIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_sports);
        initElementView();
    }

    private void initElementView() {

        actionBarV = findViewById(R.id.action_bar_rl);
        actionBarV.setBackgroundColor(Color.YELLOW);
        titleTv = (TextView)findViewById(R.id.title_tv);
        titleTv.setText(R.string.sports_title);
        historyIb = (ImageButton)findViewById(R.id.action_ib);
        historyIb.setOnClickListener(onViewClickedListener);

        backIb = (ImageButton)findViewById(R.id.nav_back_ib);

        summaryV = findViewById(R.id.sports_summary_rl);
        detailV = findViewById(R.id.sports_detail_ll);

        goalIb = (Button)findViewById(R.id.goal_ib);
        goalIb.setOnClickListener(onViewClickedListener);

    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.action_ib:
                    startActivity(new Intent(PersonalSportsAcitvity.this, PersonalHistorySportsActivity.class));
                    break;
                case R.id.goal_ib:
                    break;
                case R.id.nav_back_ib:
                    break;
            }
        }
    };
}
