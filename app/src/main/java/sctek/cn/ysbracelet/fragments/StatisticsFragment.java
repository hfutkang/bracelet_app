package sctek.cn.ysbracelet.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.activitys.FamilyHRateStatisticsActivity;
import sctek.cn.ysbracelet.activitys.FamilySleepStatisticsActivity;
import sctek.cn.ysbracelet.activitys.FamilySportsStatisticsActivity;
import sctek.cn.ysbracelet.activitys.PersonalHRateStatisticsActivity;
import sctek.cn.ysbracelet.activitys.PersonalSleepStatisticsActivity;
import sctek.cn.ysbracelet.activitys.PersonalSportsStatisticsActivity;
import sctek.cn.ysbracelet.adapters.FamiliesListViewAdapter;
import sctek.cn.ysbracelet.uiwidget.HorizontalListView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private HorizontalListView fimaliesLv;
    private ImageButton preIb;
    private ImageButton nextIb;

    private View hrateV;
    private View hrateSelectorV;
    private View sportsV;
    private View sportsSelectorV;
    private View sleepV;
    private View sleepSelectorV;

    private TextView personalHrateTv;
    private TextView familyHrateTv;
    private TextView personalSportsTv;
    private TextView familySportsTv;
    private TextView personalSleepTv;
    private TextView familySleepTv;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        initViewElement(view);
        return view;
    }

    private void initViewElement(View view) {

        fimaliesLv = (HorizontalListView)view.findViewById(R.id.families_hlv);
        FamiliesListViewAdapter adapter = new FamiliesListViewAdapter(getContext(), true);
        fimaliesLv.setAdapter(adapter);

        hrateV = view.findViewById(R.id.hrate_statistics_rl);
        hrateSelectorV = view.findViewById(R.id.hrate_sta_selector_rl);
        hrateV.setOnClickListener(onViewClickedListener);

        sportsV = view.findViewById(R.id.sports_statistics_rl);
        sportsSelectorV = view.findViewById(R.id.sports_sta_selector_rl);
        sportsV.setOnClickListener(onViewClickedListener);

        sleepV = view.findViewById(R.id.sleep_statistics_rl);
        sleepSelectorV = view.findViewById(R.id.sleep_sta_selector_rl);
        sleepV.setOnClickListener(onViewClickedListener);

        personalHrateTv = (TextView)view.findViewById(R.id.personal_hrate_sta_tv);
        familyHrateTv = (TextView)view.findViewById(R.id.family_hrate_sta_tv);
        personalHrateTv.setOnClickListener(onViewClickedListener);
        familyHrateTv.setOnClickListener(onViewClickedListener);

        personalSleepTv = (TextView)view.findViewById(R.id.personal_sleep_sta_tv);
        familySleepTv = (TextView)view.findViewById(R.id.family_sleep_sta_tv);
        personalSleepTv.setOnClickListener(onViewClickedListener);
        familySleepTv.setOnClickListener(onViewClickedListener);

        personalSportsTv = (TextView)view.findViewById(R.id.personal_sports_sta_tv);
        familySportsTv = (TextView)view.findViewById(R.id.family_sports_sta_tv);
        personalSportsTv.setOnClickListener(onViewClickedListener);
        familySportsTv.setOnClickListener(onViewClickedListener);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.hrate_statistics_rl:
                    toggleViewVisibility(hrateSelectorV);
                    break;
                case R.id.sports_statistics_rl:
                    toggleViewVisibility(sportsSelectorV);
                    break;
                case R.id.sleep_statistics_rl:
                    toggleViewVisibility(sleepSelectorV);
                    break;
                case R.id.personal_hrate_sta_tv:
                    startActivity(new Intent(getContext(), PersonalHRateStatisticsActivity.class));
                    break;
                case R.id.personal_sports_sta_tv:
                    startActivity(new Intent(getContext(), PersonalSportsStatisticsActivity.class));
                    break;
                case R.id.personal_sleep_sta_tv:
                    startActivity(new Intent(getContext(), PersonalSleepStatisticsActivity.class));
                    break;
                case R.id.family_hrate_sta_tv:
                    startActivity(new Intent(getContext(), FamilyHRateStatisticsActivity.class));
                    break;
                case R.id.family_sleep_sta_tv:
                    startActivity(new Intent(getContext(), FamilySleepStatisticsActivity.class));
                    break;
                case R.id.family_sports_sta_tv:
                    startActivity(new Intent(getContext(), FamilySportsStatisticsActivity.class));
                    break;
            }
        }
    };

    private void toggleViewVisibility(View view) {
        if(view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
        else {
            view.setVisibility(View.GONE);
        }
    }
}
