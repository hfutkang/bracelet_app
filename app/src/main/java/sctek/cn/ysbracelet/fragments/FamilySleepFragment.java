package sctek.cn.ysbracelet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.FamilySleepMonthVpAdapter;
import sctek.cn.ysbracelet.adapters.FamilySleepWeekVpAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FamilySleepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FamilySleepFragment extends FamilyDataBaseFragment {

    private static final String TAG = FamilySleepFragment.class.getSimpleName();

    public FamilySleepFragment() {
        // Required empty public constructor
    }

    public static FamilySleepFragment newInstance(String param1, String param2) {
        FamilySleepFragment fragment = new FamilySleepFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_family_sleep, container, false);
    }

    @Override
    protected void initDataElement() {
        super.initDataElement();
        mAdapter = new FamilySleepWeekVpAdapter(getContext());
    }

    @Override
    protected PagerAdapter constructAdapter(int mode) {
        if(mode == MODE_WEEK) {
            return new FamilySleepWeekVpAdapter(getContext());
        }
        else if(mode == MODE_MONTH) {
            return new FamilySleepMonthVpAdapter(getContext());
        }
        return null;
    }

    protected void initViewElement(View view) {
        super.initViewElement(view);

    }

}
