package sctek.cn.ysbracelet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.Thread.BleDataSendThread;
import sctek.cn.ysbracelet.UIWidget.PullDownProgressController;
import sctek.cn.ysbracelet.UIWidget.PullToRefreshScrollView;
import sctek.cn.ysbracelet.ble.BleData;
import sctek.cn.ysbracelet.ble.BlePacket;
import sctek.cn.ysbracelet.ble.BluetoothLeManager;
import sctek.cn.ysbracelet.ble.BluetoothLeService;
import sctek.cn.ysbracelet.ble.Commands;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionLister} interface
 * to handle interaction events.
 * Use the {@link DataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataFragment extends Fragment implements PullToRefreshScrollView.OnPullToRefreshListener
                        , BluetoothLeService.OnCharacteristicChangedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = DataFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionLister mListener;

    private ImageView deviceConnectedIv;
    private ImageView deviceBatteryIv;
    private ImageView deviceIconIb;
    private TextView deviceStateTv;

    private PullToRefreshScrollView mPullToRefreshSv;

    private LinearLayout progressBarsLo;
    private PullDownProgressController mPullDownProgressController;

    private SportViewHolder sportViewHolder;
    private SleepViewHolder sleepViewHolder;
    private HRateViewHolder hRateViewHolder;

    private BluetoothLeManager mBluetoothLeManager;

    private BleDataSendThread getSportsDataThread;
    private BleDataSendThread getSleepDataThread;
    private BleDataSendThread getHRateDataThread;

    public DataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DataFragment newInstance(String param1, String param2) {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mBluetoothLeManager.setCharacteristicListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_data_my_device, container, false);
        initElement(view);
        return view;
    }

    private void initThreads() {

        BleData data = new BleData(Commands.CMD_GET_SPORT_DATA, null);
        getSportsDataThread = new BleDataSendThread(data, null);

        data = new BleData(Commands.CMD_GET_HEART_RATE, null);
        getHRateDataThread = new BleDataSendThread(data, null);

        data = new BleData(Commands.CMD_GET_SLEEP_DATA, null);
        getSleepDataThread = new BleDataSendThread(data, null);
    }

    private void initElement(View view) {

        deviceBatteryIv = (ImageView)view.findViewById(R.id.device_battery_iv);
        deviceConnectedIv = (ImageView)view.findViewById(R.id.device_connected_iv);
        deviceStateTv = (TextView)view.findViewById(R.id.device_state_tv);
        deviceIconIb = (ImageButton)view.findViewById(R.id.device_icon_ib);

        progressBarsLo = (LinearLayout)view.findViewById(R.id.down_progress_lo);
        mPullDownProgressController = new PullDownProgressController(progressBarsLo);

        mPullToRefreshSv = (PullToRefreshScrollView)view.findViewById(R.id.pull_to_refresh_sv);
        mPullToRefreshSv.setOnScrollListener(this);

        sportViewHolder.walk = (TextView)view.findViewById(R.id.walk_steps_tv);
        sportViewHolder.run = (TextView)view.findViewById(R.id.run_steps_tv);
        sportViewHolder.calories = (TextView)view.findViewById(R.id.calories_tv);

        hRateViewHolder.rate = (TextView)view.findViewById(R.id.rate_tv);

        sleepViewHolder.total = (TextView)view.findViewById(R.id.total_tv);
        sleepViewHolder.deep = (TextView)view.findViewById(R.id.deep_tv);
        sleepViewHolder.shallow = (TextView)view.findViewById(R.id.shallow_tv);

        if(mBluetoothLeManager.isConnected())
            deviceStateTv.setText(R.string.ble_disconnected);
        else
            deviceStateTv.setText(R.string.ble_connected);

    }

    private void requestData() {

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
        Log.e(TAG, "onAttach");
        if (context instanceof OnFragmentInteractionLister) {
            mListener = (OnFragmentInteractionLister) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        BluetoothLeManager.getInstance().setCharacteristicListener(null);
    }

    @Override
    public void onPullDown(int deltaY) {
        mPullDownProgressController.updateProgress(deltaY);
    }

    @Override
    public void onCanceled() {
        mPullDownProgressController.hideSwipeDown();
    }

    @Override
    public void onRefresh() {
        deviceStateTv.setText(R.string.loading_data);
    }

    @Override
    public void onReceiveData(BlePacket packet) {

    }

    @Override
    public void onReceiveRssi(int rssi) {

    }

    private class SportViewHolder {

        public TextView walk;
        public TextView run;
        public TextView calories;

    }

    private class HRateViewHolder {

        public TextView rate;

    }

    private class SleepViewHolder {

        public TextView total;
        public TextView deep;
        public TextView shallow;
    }
}