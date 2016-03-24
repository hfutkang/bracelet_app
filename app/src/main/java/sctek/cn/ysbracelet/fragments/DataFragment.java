package sctek.cn.ysbracelet.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import sctek.cn.ysbracelet.thread.BleDataSendThread;
import sctek.cn.ysbracelet.uiwidget.PullDownProgressController;
import sctek.cn.ysbracelet.uiwidget.PullToRefreshScrollView;
import sctek.cn.ysbracelet.activitys.HeartRateActivity;
import sctek.cn.ysbracelet.ble.BleData;
import sctek.cn.ysbracelet.ble.BleDataParser;
import sctek.cn.ysbracelet.ble.BlePacket;
import sctek.cn.ysbracelet.ble.BluetoothLeManager;
import sctek.cn.ysbracelet.ble.BluetoothLeService;
import sctek.cn.ysbracelet.ble.Commands;
import sctek.cn.ysbracelet.devicedata.HeartRateData;
import sctek.cn.ysbracelet.devicedata.SleepData;
import sctek.cn.ysbracelet.devicedata.SportsData;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
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

    public static final int TIMEOUT_GET_SPROTS_DATA = 1001;
    public static final int TIMEOUT_GET_HEART_RATE_DATA = 1002;
    public static final int TIMEOUT_GET_SLEEP_DATA = 1003;
    public static final int GET_BLEDATA_TIMEOUT_MS = 2000;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ImageView deviceConnectedIv;
    private ImageView deviceBatteryIv;
    private ImageView deviceIconIb;
    private TextView deviceStateTv;

    private PullToRefreshScrollView mPullToRefreshSv;

    private ImageView sportsForwardIv;
    private ImageView hRateForwardIv;
    private ImageView sleepForwardIv;

    private LinearLayout progressBarsLo;
    private PullDownProgressController mPullDownProgressController;

    private SportViewHolder sportViewHolder;
    private SleepViewHolder sleepViewHolder;
    private HRateViewHolder hRateViewHolder;

    private BluetoothLeManager mBluetoothLeManager;

    private BleDataSendThread getSportsDataThread;
    private BleDataSendThread getSleepDataThread;
    private BleDataSendThread getHRateDataThread;

    private boolean loading = false;

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

        mBluetoothLeManager = BluetoothLeManager.getInstance();

        mBluetoothLeManager.setCharacteristicListener(this);

        initThreads();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                startLoadData();
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothLeService.ACTION_CONNECTED);
        filter.addAction(BluetoothLeService.ACTION_DISCONNECTED);
        getActivity().registerReceiver(bleStateBroadcastReceiver, filter);
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
        getSportsDataThread = new BleDataSendThread(data, mHandler);

        data = new BleData(Commands.CMD_GET_HEART_RATE, null);
        getHRateDataThread = new BleDataSendThread(data, mHandler);

        data = new BleData(Commands.CMD_GET_SLEEP_DATA, null);
        getSleepDataThread = new BleDataSendThread(data, mHandler);

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

        hRateForwardIv = (ImageView)view.findViewById(R.id.hrate_forward_iv);
        sportsForwardIv.setOnClickListener(onForwardViewClickedListener);
        hRateForwardIv.setOnClickListener(onForwardViewClickedListener);
        sleepForwardIv.setOnClickListener(onForwardViewClickedListener);

        sportViewHolder = new SportViewHolder();
        sportViewHolder.walk = (TextView)view.findViewById(R.id.walk_steps_tv);
        sportViewHolder.run = (TextView)view.findViewById(R.id.run_steps_tv);
        sportViewHolder.calories = (TextView)view.findViewById(R.id.calories_tv);

        hRateViewHolder = new HRateViewHolder();
        hRateViewHolder.rate = (TextView)view.findViewById(R.id.rate_tv);

        sleepViewHolder = new SleepViewHolder();

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        BluetoothLeManager.getInstance().setCharacteristicListener(null);
        getActivity().unregisterReceiver(bleStateBroadcastReceiver);
        getSleepDataThread.interrupt();
        getSportsDataThread.interrupt();
        getHRateDataThread.interrupt();
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
        startLoadData();
    }

    @Override
    public void onReceiveData(BlePacket packet) {
        if(packet.cmd == Commands.CMD_GET_SPORT_DATA) {
            SportsData sd = BleDataParser.parseSporsDataFrom(packet);
            showSportsData(sd);
            mHandler.removeMessages(TIMEOUT_GET_SPROTS_DATA);
            getHRateDataThread.start();
            mHandler.sendEmptyMessageDelayed(TIMEOUT_GET_HEART_RATE_DATA, GET_BLEDATA_TIMEOUT_MS);
        }
        else if(packet.cmd == Commands.CMD_GET_HEART_RATE) {
            HeartRateData hd = BleDataParser.parseHeartRateDataFrom(packet);
            showHRateData(hd);
            mHandler.removeMessages(TIMEOUT_GET_HEART_RATE_DATA);
            getSleepDataThread.start();
            mHandler.sendEmptyMessageDelayed(TIMEOUT_GET_SLEEP_DATA, GET_BLEDATA_TIMEOUT_MS);

        }
        else if(packet.cmd == Commands.CMD_GET_SLEEP_DATA) {
            SleepData sld = BleDataParser.parseSleepDataFrom(packet);
            showSleepData(sld);
            mHandler.removeMessages(TIMEOUT_GET_SLEEP_DATA);
            showMessage(R.string.success_load_data);
            loading = false;
        }
    }

    @Override
    public void onReceiveRssi(int rssi) {

    }

    @Override
    public void onDataValid() {

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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Commands.CMD_GET_SPORT_DATA:
                    if(msg.arg1 == BleDataSendThread.FAIL_SEND_DATA) {
//                        showMessage(R.string.fail_get_sports_data);
                        getHRateDataThread.start();
                        mHandler.removeMessages(TIMEOUT_GET_SPROTS_DATA);
                        mHandler.sendEmptyMessageDelayed(TIMEOUT_GET_HEART_RATE_DATA, GET_BLEDATA_TIMEOUT_MS);
                    }
                    break;
                case Commands.CMD_GET_HEART_RATE:
                    if(msg.arg1 == BleDataSendThread.FAIL_SEND_DATA) {
//                        showMessage(R.string.fail_get_heart_rate_data);
                        getSleepDataThread.start();
                        mHandler.removeMessages(TIMEOUT_GET_HEART_RATE_DATA);
                        mHandler.sendEmptyMessageDelayed(TIMEOUT_GET_SLEEP_DATA, GET_BLEDATA_TIMEOUT_MS);
                    }
                    break;
                case Commands.CMD_GET_SLEEP_DATA:
                    if(msg.arg1 == BleDataSendThread.FAIL_SEND_DATA) {
//                        showMessage(R.string.fail_get_sleep_data);
                        showMessage(R.string.fail_load_data);
                        mHandler.removeMessages(TIMEOUT_GET_SLEEP_DATA);
                        loading = false;
                    }
                    break;
                case TIMEOUT_GET_SPROTS_DATA:
//                    showMessage(R.string.timeout_get_sports_data);
                    getHRateDataThread.start();
                    mHandler.sendEmptyMessageDelayed(TIMEOUT_GET_HEART_RATE_DATA, GET_BLEDATA_TIMEOUT_MS);
                    break;
                case TIMEOUT_GET_HEART_RATE_DATA:
//                    showMessage(R.string.timeout_get_heart_rate_data);
                    getSleepDataThread.start();
                    mHandler.sendEmptyMessageDelayed(TIMEOUT_GET_SLEEP_DATA, GET_BLEDATA_TIMEOUT_MS);
                    break;
                case TIMEOUT_GET_SLEEP_DATA:
//                    showMessage(R.string.timeout_get_sleep_data);
                    showMessage(R.string.fail_load_data);
                    loading = false;
                    break;
            }
        }
    };

    private BroadcastReceiver bleStateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BluetoothLeService.ACTION_CONNECTED)) {
                showMessage(R.string.ble_connected);
            }
            else if(action.equals(BluetoothLeService.ACTION_DISCONNECTED)) {
                showMessage(R.string.ble_disconnected);
            }
        }
    };

    private View.OnClickListener onForwardViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.hrate_forward_iv:
                    startActivity(new Intent(getContext(), HeartRateActivity.class));
                    break;
            }
        }
    };

    private void startLoadData() {

        showMessage(R.string.loading_data);

        if(loading)
            return;

        if(mBluetoothLeManager.isConnected()) {
            getSportsDataThread.start();
            mHandler.sendEmptyMessageDelayed(TIMEOUT_GET_SPROTS_DATA, GET_BLEDATA_TIMEOUT_MS);
        }
        else {
            showMessage(R.string.ble_disconnected);
        }
    }

    private void showMessage(int msgResId) {
        deviceStateTv.setText(msgResId);
    }

    private void showSportsData(SportsData data) {
        String stepUnit = getString(R.string.step_unit);
        String calUnit = getString(R.string.calorie_unit);

        sportViewHolder.walk.setText(data.walkSteps + stepUnit);
        sportViewHolder.run.setText(data.runSteps + stepUnit);
        sportViewHolder.calories.setText(data.calories + calUnit);
    }

    private void showHRateData(HeartRateData data) {
        String unit = getString(R.string.heart_rate_unit);

        hRateViewHolder.rate.setText(data.rate + unit);
    }

    private void showSleepData(SleepData data) {
        String sleepUnit = getString(R.string.sleep_unit);

        sleepViewHolder.total.setText(data.total + sleepUnit);
        sleepViewHolder.deep.setText(data.deep + sleepUnit);
        sleepViewHolder.shallow.setText(data.shallow + sleepUnit);
    }
}
