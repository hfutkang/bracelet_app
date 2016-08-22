package sctek.cn.ysbracelet.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.activitys.EditorDeviceInfoActivity;
import sctek.cn.ysbracelet.activitys.FenceActivity;
import sctek.cn.ysbracelet.activitys.LocationAcitvity;
import sctek.cn.ysbracelet.activitys.PersonalHeartRateActivity;
import sctek.cn.ysbracelet.activitys.PersonalSleepAcitvity;
import sctek.cn.ysbracelet.activitys.PersonalSportsAcitvity;
import sctek.cn.ysbracelet.activitys.SearchDeviceActivity;
import sctek.cn.ysbracelet.activitys.WarnActivity;
import sctek.cn.ysbracelet.adapters.FamiliesListViewAdapter;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.uiwidget.CircleImageView;
import sctek.cn.ysbracelet.uiwidget.HorizontalListView;
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
    public static final String EXTR_DEVICE_MAC = "mac";
    public static final int REQUEST_CODE_ADD = 1;
    public static final int REQUEST_CODE_EDIT = 2;
    public static final int RESULT_CODE_ADD_OK = 1;
    public static final int RESULT_CODE_ADD_FAIL = 2;
    public static final int RESULT_CODE_EDIT_OK = 3;
    public static final int RESULT_CODE_DELETE_OK = 4;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private HorizontalListView fimaliesLv;
    private ImageButton preIb;
    private ImageButton nextIb;

    private View selectorLo;
    private CircleImageView gravatarCiv;
    private TextView nameTv;
    private TextView sexTv;
    private TextView ageTv;

    private DisplayImageOptions displayImageOptions;

    private FamiliesListViewAdapter adapter;

    private CircleImageView addCiv;

    private TextView sportsTv;
    private TextView hRateTv;
    private TextView sleepTv;
    private TextView warningTv;
    private TextView locationTv;
    private TextView fenceTv;

    private YsUser mUser;
    private DeviceInformation selectedDevcie;

    public HomeFragment() {
        // Required empty public constructor
    }

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

        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.gravatar_stub)
                .showImageOnFail(R.drawable.gravatar_stub)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.gravatar_stub)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    private void initViewElement(View view) {

        fimaliesLv = (HorizontalListView)view.findViewById(R.id.families_hlv);
        adapter = new FamiliesListViewAdapter(getContext(), true);
        fimaliesLv.setAdapter(adapter);
        fimaliesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >= mUser.getDeviceCount()) {
                    startActivityForResult(new Intent(getContext(), SearchDeviceActivity.class), REQUEST_CODE_ADD);
                }
                else {
                    selectedDevcie = mUser.getDevice(position);
                    ImageLoader.getInstance().displayImage(selectedDevcie.getImagePath(), gravatarCiv, displayImageOptions);
                    gravatarCiv.setProgress(selectedDevcie.getPower());
                    nameTv.setText(selectedDevcie.getName());
                    ageTv.setText("" + selectedDevcie.getAge());
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

        selectorLo = view.findViewById(R.id.family_selector_lo);
        gravatarCiv = (CircleImageView)view.findViewById(R.id.selected_member_cv);
        nameTv = (TextView)view.findViewById(R.id.name_tv);
        ageTv = (TextView)view.findViewById(R.id.age_tv);
        sexTv = (TextView)view.findViewById(R.id.sex_tv);

        addCiv = (CircleImageView)view.findViewById(R.id.add_device_cv);

        if(mUser.getDeviceCount() == 0) {
            addCiv.setVisibility(View.VISIBLE);
            selectorLo.setVisibility(View.GONE);
        }
        else {
            selectedDevcie = mUser.getDevice(0);
            ImageLoader.getInstance().displayImage(selectedDevcie.getImagePath(), gravatarCiv, displayImageOptions);
            nameTv.setText(selectedDevcie.getName());
            ageTv.setText("" + selectedDevcie.getAge());
            sexTv.setText(selectedDevcie.getSex());
        }

        addCiv.setOnClickListener(onViewClickedListener);
        gravatarCiv.setOnClickListener(onViewClickedListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onActivityResult onCreateView");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult " + requestCode + " " + resultCode);
        if(requestCode == REQUEST_CODE_ADD) {
            if(resultCode == RESULT_CODE_ADD_OK) {
                adapter.notifyDataSetChanged();
                if(selectedDevcie == null) {
                    addCiv.setVisibility(View.GONE);
                    selectorLo.setVisibility(View.VISIBLE);
                    selectedDevcie = mUser.getDevice(0);

                    ImageLoader.getInstance().displayImage(selectedDevcie.getImagePath(), gravatarCiv, displayImageOptions);
                    nameTv.setText(selectedDevcie.getName());
                    ageTv.setText("" + selectedDevcie.getAge());
                    sexTv.setText(selectedDevcie.getSex());
                }
            }
        } else if(requestCode == REQUEST_CODE_EDIT) {
            if(resultCode == RESULT_CODE_EDIT_OK) {
                adapter.notifyDataSetChanged();
                ImageLoader.getInstance().displayImage(selectedDevcie.getImagePath(), gravatarCiv, displayImageOptions);
                nameTv.setText(selectedDevcie.getName());
                ageTv.setText("" + selectedDevcie.getAge());
                sexTv.setText(selectedDevcie.getSex());
            }
            else if(resultCode == RESULT_CODE_DELETE_OK) {
                adapter.notifyDataSetChanged();
                if(mUser.getDeviceCount() == 0) {
                    addCiv.setVisibility(View.VISIBLE);
                    selectorLo.setVisibility(View.GONE);
                    selectedDevcie = null;
                }
                else {
                    selectedDevcie = YsUser.getInstance().getDevice(0);
                    ImageLoader.getInstance().displayImage(selectedDevcie.getImagePath(), gravatarCiv, displayImageOptions);
                    nameTv.setText(selectedDevcie.getName());
                    ageTv.setText("" + selectedDevcie.getAge());
                    sexTv.setText(selectedDevcie.getSex());
                }
            }
        }
    }

    private View.OnClickListener onViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Class cl = null;
            switch (v.getId()) {
                case R.id.heart_rate_title_tv:
                    cl = PersonalHeartRateActivity.class;
                    break;
                case R.id.sports_title_tv:
                    cl = PersonalSportsAcitvity.class;
                    break;
                case R.id.sleep_title_tv:
                    cl = PersonalSleepAcitvity.class;
                    break;
                case R.id.location_title_tv:
                    cl = LocationAcitvity.class;
                    break;
                case R.id.warning_title_tv:
                    cl = WarnActivity.class;
                    break;
                case R.id.fence_title_tv:
                    cl = FenceActivity.class;
                    break;
                case R.id.member_next_ib:
                    break;
                case R.id.member_previous_ib:
                    break;
                case R.id.selected_member_cv:
                    cl = EditorDeviceInfoActivity.class;
                    break;
                case R.id.add_device_cv:
                    startActivityForResult(new Intent(getContext(), SearchDeviceActivity.class), REQUEST_CODE_ADD);
                    break;
            }
            startActivity(cl);

        }
    };

    private void startActivity(Class cl) {
        if (selectedDevcie == null || cl == null)
            return;

        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        bundle.putString(EXTR_DEVICE_ID, selectedDevcie.getSerialNumber());
        intent.putExtras(bundle);

        intent.setClass(getContext(), cl);

        if(cl == EditorDeviceInfoActivity.class) {
            startActivityForResult(intent, REQUEST_CODE_EDIT);
        } else{
            startActivity(intent);
        }
    }

}
