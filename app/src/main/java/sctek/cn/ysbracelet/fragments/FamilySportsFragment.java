package sctek.cn.ysbracelet.fragments;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.FamilySportsMonthVpAdapter;
import sctek.cn.ysbracelet.adapters.FamilySportsWeekVpAdapter;


public class FamilySportsFragment extends FamilyDataBaseFragment {

    private static final String TAG = FamilySportsFragment.class.getSimpleName();

    public FamilySportsFragment() {
        // Required empty public constructor
    }


    public static FamilySportsFragment newInstance(String param1, String param2) {
        FamilySportsFragment fragment = new FamilySportsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_family_sports, container, false);
        initViewElement(view);
        return view;
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Override
    protected void initDataElement() {
        super.initDataElement();
        mAdapter = new FamilySportsWeekVpAdapter(getContext());
    }

    @Override
    protected PagerAdapter constructAdapter(int mode) {
        if(mode == MODE_WEEK) {
            return new FamilySportsWeekVpAdapter(getContext());
        }
        else if(mode == MODE_MONTH) {
            return new FamilySportsMonthVpAdapter(getContext());
        }
        return null;
    }

    protected void initViewElement(View view) {
        super.initViewElement(view);

    }

}
