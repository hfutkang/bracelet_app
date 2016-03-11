package sctek.cn.ysbracelet.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.github.gfranks.fab.menu.FloatingActionButton;
import com.github.gfranks.fab.menu.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.Thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.ble.BluetoothLeManager;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.devicedata.PositionData;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.http.YsHttpConnection;
import sctek.cn.ysbracelet.utils.UrlUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment implements HttpConnectionWorker.ConnectionWorkListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private FloatingActionsMenu mFloatingActionsMenu;
    private FloatingActionButton rtimeLocationFab;
    private FloatingActionButton fstepFab;
    private FloatingActionButton otherFab;
    private FloatingActionButton refreshFab;

    private View datePickerView;
    private ImageButton preDateIb;
    private ImageButton nextDateIb;
    private TextView dateTv;

    private YsDateManager mDateManager;
    private String currentDate;
    private String currentShowDate;

    private LocationState currentState;

    private enum LocationState {RUNTIEM, FOOTSTEP};

    private DeviceInformation mDevice;

    private Marker startMarker;
    private Marker endMarker;
    private Polyline footStepPloyLine;

    private ArrayList<LatLng> footSteps;

    private DatePickerDialog mDatePickerDialog;
    private DatePicker mDatePicker;

    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
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

        currentState = LocationState.RUNTIEM;
        mDevice = BluetoothLeManager.getInstance().getBindedDevice();

        footSteps = new ArrayList<>();

        mDateManager = new YsDateManager(YsDateManager.DATE_FORMAT_SHOW1);
        currentDate = mDateManager.getCurrentDate();
        mDatePickerDialog = new DatePickerDialog(getContext(), onDateSetListener, 0, 0, 0);
        mDatePicker = mDatePickerDialog.getDatePicker();
        mDatePicker.setMaxDate(System.currentTimeMillis());

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getLatestLocation();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        initViewElement(view);
        return view;
    }

    private void initViewElement(View view) {

        mMapView = (MapView)view.findViewById(R.id.baidu_mv);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();

        initOverly();

        datePickerView = view.findViewById(R.id.date_picker_rl);
        datePickerView.getBackground().setAlpha(100);
        dateTv = (TextView)view.findViewById(R.id.date_tv);
        preDateIb = (ImageButton)view.findViewById(R.id.date_previous_ib);
        nextDateIb = (ImageButton)view.findViewById(R.id.date_next_ib);
        currentShowDate = mDateManager.showCurrentDate(dateTv);
        datePickerView.setVisibility(View.GONE);

        dateTv.setOnClickListener(onDatePickerButtonClickedListener);
        preDateIb.setOnClickListener(onDatePickerButtonClickedListener);
        nextDateIb.setOnClickListener(onDatePickerButtonClickedListener);

        mFloatingActionsMenu = (FloatingActionsMenu)view.findViewById(R.id.location_fam);
        rtimeLocationFab = (FloatingActionButton)view.findViewById(R.id.runtime_fab);
        fstepFab = (FloatingActionButton)view.findViewById(R.id.footstep_fab);
        otherFab = (FloatingActionButton)view.findViewById(R.id.other_fab);
        refreshFab = (FloatingActionButton)view.findViewById(R.id.refresh_fab);

        rtimeLocationFab.setOnClickListener(onFlatingButtonClickedListener);
        fstepFab.setOnClickListener(onFlatingButtonClickedListener);
        otherFab.setOnClickListener(onFlatingButtonClickedListener);
        refreshFab.setOnClickListener(onFlatingButtonClickedListener);

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
    public void onWorkDone(int resCode) {

        mFloatingActionsMenu.setEnabled(true);
        preDateIb.setEnabled(true);
        dateTv.setEnabled(true);
        nextDateIb.setEnabled(true);

        if(currentState == LocationState.RUNTIEM)
            refreshFab.setEnabled(true);

        if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS) {
            if(currentState == LocationState.FOOTSTEP) {
                footStepPloyLine.setPoints(footSteps);
                endMarker.setPosition(footSteps.get(footSteps.size() -1));
            }
        }
        else if(resCode == XmlNodes.RESPONSE_CODE_FAIL) {

        }
    }

    @Override
    public void onResult(YsData result) {
        PositionData data = (PositionData)result;
        if(currentState == LocationState.RUNTIEM) {
            endMarker.setPosition(new LatLng(data.latitude, data.longitude));
        }
        else {
            if(footSteps.size() == 0)
                startMarker.setPosition(new LatLng(data.latitude, data.longitude));
            footSteps.add(new LatLng(data.latitude, data.longitude));
        }

    }

    @Override
    public void onError(Exception e) {
        mFloatingActionsMenu.setEnabled(true);
        if(currentState == LocationState.RUNTIEM) {
            refreshFab.setEnabled(true);
        }
        else if(currentState == LocationState.FOOTSTEP) {
            preDateIb.setEnabled(true);
            dateTv.setEnabled(true);
            nextDateIb.setEnabled(true);
        }
    }

    private View.OnClickListener onDatePickerButtonClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.date_previous_ib:
                    currentShowDate = mDateManager.showPreviousDate(dateTv);
                    mBaiduMap.clear();
                    footSteps.clear();
                    initOverly();
                    getFootSteps(mDateManager.getHttpDate());
                    break;
                case R.id.date_next_ib:
                    if(currentShowDate.equals(currentDate))
                        return;
                    mBaiduMap.clear();
                    footSteps.clear();
                    initOverly();
                    currentShowDate = mDateManager.showNextDate(dateTv);
                    getFootSteps(mDateManager.getHttpDate());
                    break;
                case R.id.date_tv:
                    showDatePickerDialog();
                    break;
            }
        }
    };

    private View.OnClickListener onFlatingButtonClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.runtime_fab:
                    if(currentState == LocationState.FOOTSTEP) {
                        mBaiduMap.clear();
                        initOverly();
                        datePickerView.setVisibility(View.GONE);
                        currentState = LocationState.RUNTIEM;
                        refreshFab.setVisibility(View.VISIBLE);
                        getLatestLocation();
                    }
                    break;
                case R.id.footstep_fab:
                    if(currentState == LocationState.RUNTIEM) {
                        mBaiduMap.clear();
                        footSteps.clear();
                        initOverly();
                        currentState = LocationState.FOOTSTEP;
                        datePickerView.setVisibility(View.VISIBLE);
                        refreshFab.setVisibility(View.GONE);
                        getFootSteps(mDateManager.getHttpDate());
                    }
                    break;
                case R.id.other_fab:
                    break;
                case R.id.refresh_fab:
                    getLatestLocation();
                    break;
            }
        }
    };

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mDateManager.setDate(year, monthOfYear, dayOfMonth);
            currentShowDate = mDateManager.showCurrentDate(dateTv);
            getFootSteps(mDateManager.getHttpDate());
        }
    };

    private void getLatestLocation() {
        if(mDevice.getSerialNumber() == null)
            return;
        String url = UrlUtils.compositeGetLatestLocationUrl(mDevice.getSerialNumber());
        YsHttpConnection connection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        HttpConnectionWorker worker = new HttpConnectionWorker(connection, this);
        worker.start();
        refreshFab.setEnabled(false);
        mFloatingActionsMenu.setEnabled(false);
    }

    private void getFootSteps(String date) {

        if(mDevice.getSerialNumber() == null) {
            return;
        }

        String url = UrlUtils.compositeGetFootStepsUrl(mDevice.getSerialNumber(),date, date);
        YsHttpConnection connection = new YsHttpConnection(url, YsHttpConnection.METHOD_GET, null);
        HttpConnectionWorker worker = new HttpConnectionWorker(connection, this);
        worker.start();

        preDateIb.setEnabled(false);
        dateTv.setEnabled(false);
        nextDateIb.setEnabled(false);
        mFloatingActionsMenu.setEnabled(false);

    }

    private void showDatePickerDialog() {
        mDatePicker.updateDate(mDateManager.getYear(), mDateManager.getMonth(), mDateManager.getDayOfMonth());
        mDatePickerDialog.show();
    }

    private void initOverly() {
        MarkerOptions ooA = new MarkerOptions().position(new LatLng(0, 0))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation_location_dark))
                .zIndex(9).draggable(true);
        startMarker = (Marker)mBaiduMap.addOverlay(ooA);
        endMarker = (Marker)mBaiduMap.addOverlay(ooA);

        List<LatLng> points = new ArrayList<>();
        points.add(new LatLng(0, 0));
        points.add(new LatLng(0, 0));
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(R.color.burlywood).points(points);
        footStepPloyLine = (Polyline) mBaiduMap.addOverlay(ooPolyline);
    }
}
