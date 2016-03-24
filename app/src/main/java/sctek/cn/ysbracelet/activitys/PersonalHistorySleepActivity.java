package sctek.cn.ysbracelet.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.PersonalHistorySleepLvAdapter;

public class PersonalHistorySleepActivity extends AppCompatActivity {

    private final static String TAG = PersonalHistorySleepActivity.class.getSimpleName();

    private ListView recordsLv;
    private TextView emptyTv;

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton actionIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_history_sleep);
        initViewElement();
    }

    private void initViewElement() {

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        actionBarV.setBackgroundColor(Color.BLUE);
        titleTv.setText(R.string.sleep_title);
        actionIb.setVisibility(View.GONE);

        recordsLv = (ListView)findViewById(R.id.history_sleep_lv);
        PersonalHistorySleepLvAdapter adapter = new PersonalHistorySleepLvAdapter(this, null);
        recordsLv.setAdapter(adapter);
        recordsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonalHistorySleepLvAdapter.ViewHolder viewHolder = (PersonalHistorySleepLvAdapter.ViewHolder) view.getTag();
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
}
