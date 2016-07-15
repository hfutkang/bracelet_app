package sctek.cn.ysbracelet.activitys;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.fragments.HomeFragment;

/**
 * Created by kang on 16-4-6.
 */
public abstract class PersonalHistoryDataBaseActivity extends AppCompatActivity {

    private final static String TAG = PersonalHistoryDataBaseActivity.class.getSimpleName();

    protected ListView recordsLv;
    protected TextView emptyTv;

    protected View actionBarV;
    protected TextView titleTv;
    protected ImageButton backIb;
    protected ImageButton actionIb;

    protected BaseAdapter adapter;

    protected List<YsData> records;

    protected String deviceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView();

        initDataElement();

        initViewElement();

        new LoadDataAsyncTask().execute();

    }

    public class LoadDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mProgressDialog;

        public LoadDataAsyncTask() {
            mProgressDialog = new ProgressDialog(PersonalHistoryDataBaseActivity.this);
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            if(records.size() == 0) {
                emptyTv.setVisibility(View.VISIBLE);
            }
            else {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    protected void initViewElement(){

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);
        actionIb.setVisibility(View.GONE);

        backIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        emptyTv = (TextView)findViewById(R.id.empty_tv);
        emptyTv.setVisibility(View.GONE);
    }

    private void initDataElement() {
        records = new ArrayList<>();
        deviceId = getIntent().getStringExtra(HomeFragment.EXTR_DEVICE_ID);
    }

    protected abstract void setContentView();

    protected abstract void loadData();
}
