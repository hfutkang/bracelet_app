package sctek.cn.ysbracelet.UiWidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;;

public class PullToRefreshListView extends ListView implements OnScrollListener {
	
	private final static String TAG = PullToRefreshListView.class.getSimpleName();
	
	private OnPullToRefreshListener mPullToRefreshListener;
	private int distanceOfPullDown = 0;
	
	public PullToRefreshListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setOnScrollListener(this);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setOnScrollListener(this);
	}
	
	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setOnScrollListener(this);
	}
	
	public void setOnListViewScroll(OnPullToRefreshListener scroll) {
		mPullToRefreshListener = scroll;
	}
	
	@SuppressLint("NewApi")
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		// TODO Auto-generated method stub
		distanceOfPullDown += -deltaY;
		if(mPullToRefreshListener != null && deltaY < 0) {
			mPullToRefreshListener.onPullDown(deltaY);
		}
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
				isTouchEvent);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (mPullToRefreshListener != null
			    && (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING))
				mPullToRefreshListener.onCanceled();
				if(distanceOfPullDown >= 100)
					mPullToRefreshListener.onRefresh();
				distanceOfPullDown = 0;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
		if(mPullToRefreshListener != null&&distanceOfPullDown > 0) {
			mPullToRefreshListener.onCanceled();
		}
		distanceOfPullDown = 0;
	}
	
	public interface OnPullToRefreshListener {
		void onPullDown(int deltaY);
		void onCanceled();
		void onRefresh();
	}

}
