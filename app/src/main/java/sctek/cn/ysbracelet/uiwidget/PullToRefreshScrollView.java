package sctek.cn.ysbracelet.uiwidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by kang on 16-3-4.
 */
public class PullToRefreshScrollView extends ScrollView implements View.OnTouchListener{

    private final static String TAG = PullToRefreshListView.class.getSimpleName();

    private OnPullToRefreshListener mPullToRefreshListener;
    private int distanceOfPullDown = 0;

    public PullToRefreshScrollView(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public PullToRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public PullToRefreshScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PullToRefreshScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOnTouchListener(this);
    }

    public void setOnScrollListener(OnPullToRefreshListener scroll) {
        mPullToRefreshListener = scroll;
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        distanceOfPullDown += -deltaY;
        if(mPullToRefreshListener != null && deltaY < 0) {
            mPullToRefreshListener.onPullDown(deltaY);
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mPullToRefreshListener != null&&distanceOfPullDown > 0) {
            mPullToRefreshListener.onCanceled();
        }
        distanceOfPullDown = 0;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mPullToRefreshListener != null&&(event.getAction() == MotionEvent.ACTION_UP)) {
            mPullToRefreshListener.onCanceled();
            if (distanceOfPullDown >= 100)
                mPullToRefreshListener.onRefresh();

            distanceOfPullDown = 0;
        }
        return false;
    }

    public interface OnPullToRefreshListener {
        void onPullDown(int deltaY);
        void onCanceled();
        void onRefresh();
    }
}
