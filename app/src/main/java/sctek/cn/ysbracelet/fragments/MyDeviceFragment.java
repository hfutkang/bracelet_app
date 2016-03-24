package sctek.cn.ysbracelet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import sctek.cn.ysbracelet.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyDeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDeviceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG = MyDeviceFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RadioGroup navRadioGroup;

    private OnFragmentInteractionListener mListener;

    public MyDeviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyDeviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyDeviceFragment newInstance(String param1, String param2) {
        MyDeviceFragment fragment = new MyDeviceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        addDefaultFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_my_device, container, false);
        navRadioGroup = (RadioGroup)view.findViewById(R.id.bottom_navigation_rg);
        navRadioGroup.setOnCheckedChangeListener(onNavItemCheckedListener);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        Log.e(TAG, "onButtonPressed");
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach");
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

    private RadioGroup.OnCheckedChangeListener onNavItemCheckedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.home_rb:
                    showFragment(new DataFragment());
                    break;
                case R.id.messages_rb:
                    showFragment(new LocationFragment());
                    break;
                case R.id.settings_rb:
                    showFragment(new SettingsFragment());
                    break;
            }
        }
    };

    private void addDefaultFragment() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DataFragment dataFragment = (DataFragment) fragmentManager.findFragmentByTag(DataFragment.TAG);
                if(dataFragment == null)
                    dataFragment = new DataFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_fl, dataFragment, DataFragment.TAG).commit();
            }
        });
    }

    private void showFragment(Fragment fg) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_fl, fg, DataFragment.TAG).commit();
    }
}
