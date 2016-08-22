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
import sctek.cn.ysbracelet.activitys.FamilyHRateStatisticsActivity;
import sctek.cn.ysbracelet.activitys.FamilySleepStatisticsActivity;
import sctek.cn.ysbracelet.activitys.FamilySportsStatisticsActivity;
import sctek.cn.ysbracelet.activitys.PersonalHRateStatisticsActivity;
import sctek.cn.ysbracelet.activitys.PersonalSleepStatisticsActivity;
import sctek.cn.ysbracelet.activitys.PersonalSportsStatisticsActivity;
import sctek.cn.ysbracelet.activitys.SearchDeviceActivity;
import sctek.cn.ysbracelet.adapters.FamiliesListViewAdapter;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.uiwidget.CircleImageView;
import sctek.cn.ysbracelet.uiwidget.HorizontalListView;
import sctek.cn.ysbracelet.user.YsUser;

public class StatisticsFragment extends Fragment {
    public final static String TAG = HomeFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private HorizontalListView fimaliesLv;
    private ImageButton preIb;
    private ImageButton nextIb;

    private DisplayImageOptions displayImageOptions;

    private FamiliesListViewAdapter adapter;

    private CircleImageView addCiv;

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

    private View selectorLo;
    private CircleImageView gravatarCiv;
    private TextView nameTv;
    private TextView sexTv;
    private TextView ageTv;

    private YsUser mUser;
    private DeviceInformation selectedDevcie;

    public StatisticsFragment() {
        // Required empty public constructor
    }

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

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
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
        adapter = new FamiliesListViewAdapter(getContext(), true);
        fimaliesLv.setAdapter(adapter);
        fimaliesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >= mUser.getDeviceCount()) {
                    startActivityForResult(new Intent(getContext(), SearchDeviceActivity.class), HomeFragment.REQUEST_CODE_ADD);
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

        selectorLo = view.findViewById(R.id.family_selector_lo);
        addCiv = (CircleImageView)view.findViewById(R.id.add_device_cv);

        gravatarCiv = (CircleImageView)view.findViewById(R.id.selected_member_cv);
        nameTv = (TextView)view.findViewById(R.id.name_tv);
        ageTv = (TextView)view.findViewById(R.id.age_tv);
        sexTv = (TextView)view.findViewById(R.id.sex_tv);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == HomeFragment.REQUEST_CODE_ADD) {
            if(resultCode == HomeFragment.RESULT_CODE_ADD_OK) {
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
        } else if(requestCode == HomeFragment.REQUEST_CODE_EDIT) {
            if(resultCode == HomeFragment.RESULT_CODE_EDIT_OK) {
                adapter.notifyDataSetChanged();
                ImageLoader.getInstance().displayImage(selectedDevcie.getImagePath(), gravatarCiv, displayImageOptions);
                nameTv.setText(selectedDevcie.getName());
                ageTv.setText("" + selectedDevcie.getAge());
                sexTv.setText(selectedDevcie.getSex());
            }
            else if(resultCode == HomeFragment.RESULT_CODE_DELETE_OK) {
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
            if (selectedDevcie != null)
                bundle.putString(HomeFragment.EXTR_DEVICE_ID, selectedDevcie.getSerialNumber());
            Intent intent = new Intent();
            intent.putExtras(bundle);
            Class cl = null;
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
                    cl = PersonalHRateStatisticsActivity.class;
                    break;
                case R.id.personal_sports_sta_tv:
                    cl = PersonalSportsStatisticsActivity.class;
                    break;
                case R.id.personal_sleep_sta_tv:
                    cl = PersonalSleepStatisticsActivity.class;
                    break;
                case R.id.family_hrate_sta_tv:
                    cl = FamilyHRateStatisticsActivity.class;
                    break;
                case R.id.family_sleep_sta_tv:
                    cl = FamilySleepStatisticsActivity.class;
                    break;
                case R.id.family_sports_sta_tv:
                    cl = FamilySportsStatisticsActivity.class;
                    break;
                case R.id.selected_member_cv:
                    cl = EditorDeviceInfoActivity.class;
                    break;
                case R.id.add_device_cv:
                    startActivityForResult(new Intent(getContext(), SearchDeviceActivity.class), HomeFragment.REQUEST_CODE_ADD);
                    break;
            }
            startActivity(cl);
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

    private void startActivity(Class cl) {
        if (selectedDevcie == null || cl == null)
            return;

        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        bundle.putString(HomeFragment.EXTR_DEVICE_ID, selectedDevcie.getSerialNumber());
        intent.putExtras(bundle);

        intent.setClass(getContext(), cl);

        if(cl == EditorDeviceInfoActivity.class && selectedDevcie != null) {
            startActivityForResult(intent, HomeFragment.REQUEST_CODE_EDIT);
        } else {
            startActivity(intent);
        }
    }
}
