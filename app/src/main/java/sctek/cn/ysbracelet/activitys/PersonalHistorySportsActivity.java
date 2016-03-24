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
import sctek.cn.ysbracelet.adapters.PersonalHistorySportsLvAdapter;

public class PersonalHistorySportsActivity extends AppCompatActivity {

    private final static String TAG = PersonalHistorySportsActivity.class.getSimpleName();

    private ListView recordsLv;
    private TextView emptyTv;

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton actionIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_history_sports);
        initViewElement();
    }

    private void initViewElement() {

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        actionBarV.setBackgroundColor(Color.YELLOW);
        titleTv.setText(R.string.sports_title);
        actionIb.setVisibility(View.VISIBLE);

        recordsLv = (ListView)findViewById(R.id.history_sports_lv);
        PersonalHistorySportsLvAdapter adapter = new PersonalHistorySportsLvAdapter(this, null);
        recordsLv.setAdapter(adapter);
        recordsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonalHistorySportsLvAdapter.ViewHolder viewHolder = (PersonalHistorySportsLvAdapter.ViewHolder) view.getTag();
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
