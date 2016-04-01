package sctek.cn.ysbracelet.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.activitys.FenceActivity;
import sctek.cn.ysbracelet.activitys.LocationAcitvity;
import sctek.cn.ysbracelet.activitys.PersonalSleepAcitvity;
import sctek.cn.ysbracelet.activitys.WarnActivity;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.uiwidget.CircleImageView;
import sctek.cn.ysbracelet.adapters.FamiliesListViewAdapter;
import sctek.cn.ysbracelet.uiwidget.HorizontalListView;
import sctek.cn.ysbracelet.activitys.HeartRateActivity;
import sctek.cn.ysbracelet.activitys.PersonalSportsAcitvity;
import sctek.cn.ysbracelet.user.YsUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    public final static String TAG = HomeFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String EXTR_DEVICE_ID = "deviceId";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private HorizontalListView fimaliesLv;
    private ImageButton preIb;
    private ImageButton nextIb;

    private CircleImageView gravatarCiv;
    private TextView nameTv;
    private TextView sexTv;
    private TextView ageTv;

    private TextView sportsTv;
    private TextView hRateTv;
    private TextView sleepTv;
    private TextView warningTv;
    private TextView locationTv;
    private TextView fenceTv;

    private YsUser mUser;
    private DeviceInformation selectedDevcie;
    private int currentPosition;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        
        mUser = YsUser.getInstance();
    }

    private void initViewElement(View view) {

        fimaliesLv = (HorizontalListView)view.findViewById(R.id.families_hlv);
        FamiliesListViewAdapter adapter = new FamiliesListViewAdapter(getContext(), true);
        fimaliesLv.setAdapter(adapter);
        fimaliesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >= mUser.getDeviceCount()) {

                }
                else {
                    selectedDevcie = mUser.getDevice(position);
                    ImageLoader.getInstance().displayImage(selectedDevcie.getImagePath(), gravatarCiv);
                    gravatarCiv.setProgress(selectedDevcie.getPower());
                    nameTv.setText(selectedDevcie.getName());
                    ageTv.setText(selectedDevcie.getAge());
                }
            }
        });

        preIb = (ImageButton)view.findViewById(R.id.member_previous_ib);
        nextIb = (ImageButton)view.findViewById(R.id.member_next_ib);
        preIb.setOnClickListener(onViewClickedListener);
        nextIb.setOnClickListener(onViewClickedListener);

        hRateTv = (TextView)view.findViewById(R.id.heart_rate_title_tv);
        sportsTv = (TextView)view.findViewById(R.id.sports_title_tv);
        sleepTv = (TextView)view.findViewById(R.id.sleep_title_tv);
        locationTv = (TextView)view.findViewById(R.id.location_title_tv);
        warningTv = (TextView)view.findViewById(R.id.warning_title_tv);
        fenceTv = (TextView)view.findViewById(R.id.fence_title_tv);

        hRateTv.setOnClickListener(onViewClickedListener);
        sportsTv.setOnClickListener(onViewClickedListener);
        sleepTv.setOnClickListener(onViewClickedListener);
        locationTv.setOnClickListener(onViewClickedListener);
        warningTv.setOnClickListener(onViewClickedListener);
        fenceTv.setOnClickListener(onViewClickedListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViewElement(view);
        return view;
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
            Bundle bundle = new Bundle();
            bundle.putString(EXTR_DEVICE_ID, selectedDevcie.getSerialNumber());
            Intent intent = new Intent();
            intent.putExtras(bundle);
            switch (v.getId()) {
                case R.id.heart_rate_title_tv:
                    intent.setClass(getContext(), HeartRateActivity.class);
                    startActivity(intent);
                    break;
                case R.id.sports_title_tv:
                    intent.setClass(getContext(), PersonalSportsAcitvity.class);
                    startActivity(intent);;
                    break;
                case R.id.sleep_title_tv:
                    intent.setClass(getContext(), PersonalSleepAcitvity.class);
                    startActivity(intent);
                    break;
                case R.id.location_title_tv:
                    intent.setClass(getContext(), LocationAcitvity.class);
                    startActivity(intent);
                    break;
                case R.id.warning_title_tv:
                    intent.setClass(getContext(), WarnActivity.class);
                    startActivity(intent);
                    break;
                case R.id.fence_title_tv:
                    intent.setClass(getContext(), FenceActivity.class);
                    startActivity(intent);
                    break;
                case R.id.member_next_ib:
                    break;
                case R.id.member_previous_ib:
                    break;
            }

        }
    };

}
