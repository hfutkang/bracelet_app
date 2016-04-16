package sctek.cn.ysbracelet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;

/**
 * Created by kang on 16-4-13.
 */
public abstract class FamilyDataBaseFragment extends Fragment{

    private final static String TAG = FamilyDataBaseFragment.class.getSimpleName();

    protected TextView emptyTv;
    protected ViewPager mViewPager;
    protected PagerAdapter mAdapter;
    protected TextView timeTv;

    protected final static int MODE_WEEK = 2;
    protected final static int MODE_MONTH = 3;

    protected ImageView weekIv;
    protected ImageView monthIv;

    protected int mode;

    protected YsDateManager dateManager;

    protected OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataElement();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflaterView(inflater, container);
        initViewElement(view);
        return view;
    }

    protected abstract View inflaterView(LayoutInflater inflater, ViewGroup container);

    protected void initDataElement(){
        mode = MODE_WEEK;
        dateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SECOND);
    }

    protected abstract PagerAdapter constructAdapter(int mode);

    protected void initViewElement(View view) {
        emptyTv = (TextView)view.findViewById(R.id.empty_tv);
        emptyTv.setVisibility(View.GONE);
        mViewPager = (ViewPager)view.findViewById(R.id.family_data_vp);
        timeTv = (TextView)view.findViewById(R.id.time_tv);

        timeTv.setText(getTimeStr(0));

        weekIv = (ImageView)view.findViewById(R.id.week_iv);
        monthIv = (ImageView)view.findViewById(R.id.month_iv);

        weekIv.setSelected(true);
        monthIv.setSelected(false);

        weekIv.setOnClickListener(onImageViewClickedListener);
        monthIv.setOnClickListener(onImageViewClickedListener);

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                timeTv.setText(getTimeStr(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected String getTimeStr(int position) {
        if(mode == MODE_WEEK) {
            String start = dateManager.getFirstDayOfWeekBy(-position);
            String end = dateManager.getLastDayOfWeekBy(-position);
            return start + "/" + end;
        }
        else if(mode == MODE_MONTH) {
            return dateManager.getMonthBy(-position);
        }
        return "";
    }

    protected View.OnClickListener onImageViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.week_iv:
                    if(mode != MODE_WEEK) {
                        mode = MODE_WEEK;
                        weekIv.setSelected(true);
                        monthIv.setSelected(false);
                        timeTv.setText(getTimeStr(0));
                        mAdapter = constructAdapter(mode);
                        mViewPager.setAdapter(mAdapter);
                    }
                    break;
                case R.id.month_iv:
                    if(mode != MODE_MONTH) {
                        mode = MODE_MONTH;
                        monthIv.setSelected(true);
                        weekIv.setSelected(false);
                        timeTv.setText(getTimeStr(0));
                        mAdapter = constructAdapter(mode);
                        mViewPager.setAdapter(mAdapter);
                    }
                    break;
            }
        }
    };
}
