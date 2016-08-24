package sctek.cn.ysbracelet.activitys;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.fragments.HomeFragment;

/**
 * Created by kang on 16-4-6.
 */
public abstract class PersonalHistoryDataBaseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    private final static String TAG = PersonalHistoryDataBaseActivity.class.getSimpleName();

    protected ListView recordsLv;
    protected TextView emptyTv;

    protected View actionBarV;
    protected TextView titleTv;
    protected ImageButton backIb;
    protected ImageButton actionIb;

    protected CursorAdapter adapter;

    protected String deviceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView();

        initDataElement();

        initViewElement();

        getLoaderManager().initLoader(0, null, this);

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
        deviceId = getIntent().getStringExtra(HomeFragment.EXTR_DEVICE_ID);
    }

    protected abstract void setContentView();

    protected abstract Loader createLoader();

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return createLoader();
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        adapter.swapCursor((Cursor)data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }
}
