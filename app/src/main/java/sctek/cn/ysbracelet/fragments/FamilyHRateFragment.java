package sctek.cn.ysbracelet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.FamilyHRateDataMonthVpAdapter;
import sctek.cn.ysbracelet.adapters.FamilyHRateDataWeekVpAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FamilyHRateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FamilyHRateFragment extends FamilyDataBaseFragment {

    public FamilyHRateFragment() {
        super();
    }

    public static FamilyHRateFragment newInstance(String param1, String param2) {
        FamilyHRateFragment fragment = new FamilyHRateFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_family_hrate, container, false);
    }

    @Override
    protected void initDataElement() {
        super.initDataElement();
        mAdapter = new FamilyHRateDataWeekVpAdapter(getContext());
    }

    @Override
    protected PagerAdapter constructAdapter(int mode) {
        if(mode == MODE_WEEK) {
            return new FamilyHRateDataWeekVpAdapter(getContext());
        }
        else if(mode == MODE_MONTH) {
            return new FamilyHRateDataMonthVpAdapter(getContext());
        }
        return null;
    }

    protected void initViewElement(View view) {
        super.initViewElement(view);

    }


}
