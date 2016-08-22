package sctek.cn.ysbracelet.activitys;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.github.gfranks.fab.menu.FloatingActionButton;
import com.github.gfranks.fab.menu.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import sctek.cn.ysbracelet.DateManager.YsDateManager;
import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.adapters.FamiliesListViewAdapter;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.devicedata.PositionData;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.map.OverlaysManager;
import sctek.cn.ysbracelet.sqlite.LocalDataContract;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.uiwidget.HorizontalListView;
import sctek.cn.ysbracelet.user.UserManagerUtils;
import sctek.cn.ysbracelet.user.YsUser;
import sctek.cn.ysbracelet.utils.DialogUtils;

public class LocationAcitvity extends AppCompatActivity implements HttpConnectionWorker.ConnectionWorkListener{

    private static final String TAG = LocationAcitvity.class.getSimpleName();

    private final static int HIDE_INFOR_WINDOW = 1;
    private final static int INFOR_WINDOW_SHOW_TIME = 3000;

    private final static int MODE_RUNTIME = 1;
    private final static int MODE_HISTORY = 2;
    private final static int MODE_FIND = 3;

    private View actionBarV;
    private TextView titleTv;
    private ImageButton backIb;
    private ImageButton actionIb;

    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private FloatingActionsMenu mFloatingActionsMenu;
    private FloatingActionButton rtimeLocationFab;
    private FloatingActionButton fstepFab;
    private FloatingActionButton otherFab;
    private FloatingActionButton refreshFab;
    private int currentMode = MODE_RUNTIME;

    private View datePickerView;
    private ImageButton preDateIb;
    private ImageButton nextDateIb;
    private TextView dateTv;

    private YsDateManager mDateManager;
    private String currentDate;
    private String currentShowDate;

    private DatePickerDialog mDatePickerDialog;
    private DatePicker mDatePicker;

    private HorizontalListView familiesLv;

    private List<DeviceInformation> mDevices;

    private OverlaysManager mOverlaysManager;

    private CoordinateConverter mCoordinateConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mDateManager = new YsDateManager(YsDateManager.DATE_FORMAT_DAY);
        currentDate = mDateManager.getCurrentDate();

        mDatePickerDialog = new DatePickerDialog(this, onDateSetListener, 0, 0, 0);
        mDatePicker = mDatePickerDialog.getDatePicker();
        mDatePicker.setMaxDate(System.currentTimeMillis());

        mDevices = YsUser.getInstance().getDevices();

        mOverlaysManager = new OverlaysManager();

        mCoordinateConverter = new CoordinateConverter();
        mCoordinateConverter.from(CoordinateConverter.CoordType.GPS);

        initViewElement();

        showHintDialog();

        new InitOverlaysAsyncTask().execute();

        new LoadPolylinesAsyncTask().execute();

        initMyCurrentPosition();
    }

    private void initViewElement() {

        actionBarV = findViewById(R.id.action_bar_rl);
        actionBarV.setBackgroundColor(getResources().getColor(R.color.aqua));
        titleTv = (TextView)findViewById(R.id.title_tv);
        titleTv.setText(R.string.location_title);
        actionIb = (ImageButton)findViewById(R.id.action_ib);
        actionIb.setVisibility(View.GONE);

        backIb = (ImageButton)findViewById(R.id.nav_back_ib);

        mMapView = (MapView)findViewById(R.id.baidu_mv);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMarkerClickListener(onMarkerClickListener);

        datePickerView = findViewById(R.id.date_picker_rl);
        datePickerView.getBackground().setAlpha(100);
        dateTv = (TextView)findViewById(R.id.date_tv);
        preDateIb = (ImageButton)findViewById(R.id.date_previous_ib);
        nextDateIb = (ImageButton)findViewById(R.id.date_next_ib);
        currentShowDate = mDateManager.showCurrentDate(dateTv);
        datePickerView.setVisibility(View.GONE);

        dateTv.setOnClickListener(onDatePickerButtonClickedListener);
        preDateIb.setOnClickListener(onDatePickerButtonClickedListener);
        nextDateIb.setOnClickListener(onDatePickerButtonClickedListener);

        mFloatingActionsMenu = (FloatingActionsMenu)findViewById(R.id.location_fam);
        rtimeLocationFab = (FloatingActionButton)findViewById(R.id.runtime_fab);
        fstepFab = (FloatingActionButton)findViewById(R.id.trail_fab);
        otherFab = (FloatingActionButton)findViewById(R.id.find_fab);
        refreshFab = (FloatingActionButton)findViewById(R.id.refresh_fab);

        rtimeLocationFab.setOnClickListener(onFlatingButtonClickedListener);
        fstepFab.setOnClickListener(onFlatingButtonClickedListener);
        otherFab.setOnClickListener(onFlatingButtonClickedListener);
        refreshFab.setOnClickListener(onFlatingButtonClickedListener);

        familiesLv = (HorizontalListView)findViewById(R.id.families_hlv);

        familiesLv.setBackgroundColor(Color.TRANSPARENT);
        FamiliesListViewAdapter adapter = new FamiliesListViewAdapter(this, false);
        familiesLv.setAdapter(adapter);
        familiesLv.setOnItemClickListener(onMemberItemClickedListener);
    }

    private void initMarkerOverlay() {
        ContentResolver cr = getContentResolver();
        String[] projections = new String[] {LocalDataContract.Location.COLUMNS_NAME_LOCATION_LATITUDE
                                                , LocalDataContract.Location.COLUMNS_NAME_LOCATION_LONGITUDE
                                                , LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME};
        for (DeviceInformation d : mDevices) {
            Log.e(TAG, d.serialNumber);
            Cursor cursor = cr.query(LocalDataContract.Location.CONTENT_URI
                                        , projections
                                        , LocalDataContract.Location.COLUMNS_NAME_LOCATION_DEVICE + "=?"
                                            + " and " + LocalDataContract.Location.COLUMNS_NAME_LOCATION_TYPE + "=" + "'gps'"
                                        , new String[]{d.serialNumber}
                                        , LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME + " desc limit 1");
            LatLng latLng;
            String time;

            if(cursor.moveToFirst()) {
                double latitude = cursor.getDouble(0);
                double longitude = cursor.getDouble(1);
                time = cursor.getString(2);
                LatLng ll = new LatLng(latitude, longitude);
                Log.e(TAG, "lat:" + ll.latitude + " lon:" + ll.longitude);
                latLng = mCoordinateConverter.coord(ll).convert();

            }
            else {
                latLng = new LatLng(0, 0);
                time = "";

            }

            MarkerOptions options = new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
            Marker marker = (Marker)mBaiduMap.addOverlay(options);
            marker.setTitle(time);
            mOverlaysManager.addMarker(d.serialNumber, marker);

            cursor.close();
        }
    }

    private void initPolylineOverlay() {
        ContentResolver cr = getContentResolver();
        String[] projections = new String[] {LocalDataContract.Location.COLUMNS_NAME_LOCATION_LATITUDE
                                        , LocalDataContract.Location.COLUMNS_NAME_LOCATION_LONGITUDE
                                        , LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME};
        String  where = LocalDataContract.Location.COLUMNS_NAME_LOCATION_DEVICE + "=?"
                + " and " + LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME + " > " + currentDate
                + " and " + LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME + " < " + mDateManager.getNextDate()
                + " and " + LocalDataContract.Location.COLUMNS_NAME_LOCATION_TYPE + "=" + "'gps'";

        for(DeviceInformation d : mDevices) {
            Cursor cursor = cr.query(LocalDataContract.Location.CONTENT_URI
                    , projections
                    , where
                    , new String[]{d.serialNumber}
                    , LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME + " asc");
            List<LatLng> positions = new ArrayList<>();
            while (cursor.moveToNext()) {
                double lat = cursor.getDouble(0);
                double lon = cursor.getDouble(1);
                LatLng latLng = new LatLng(lat, lon);

                Log.e(TAG, "lat:" + latLng.latitude + " lon:" + latLng.longitude);

                positions.add(mCoordinateConverter.coord(latLng).convert());
            }
            if(positions.size() < 2) {
                positions.clear();
                positions.add(new LatLng(0, 0));
                positions.add(new LatLng(0, 0));
            }
            PolylineOptions options = new PolylineOptions();
            options.width(10).points(positions).color(Color.RED);
            Polyline polyline = (Polyline) mBaiduMap.addOverlay(options);
            mOverlaysManager.addPolyline(d.serialNumber, polyline);

            cursor.close();
        }
    }

    private void loadPolylineOverlay() {
        ContentResolver cr = getContentResolver();
        String[] projections = new String[] {LocalDataContract.Location.COLUMNS_NAME_LOCATION_LATITUDE
                , LocalDataContract.Location.COLUMNS_NAME_LOCATION_LONGITUDE};
        String  where = LocalDataContract.Location.COLUMNS_NAME_LOCATION_DEVICE + "=?"
                + " and " + LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME + " > " + "'" + currentShowDate + "'"
                + " and " + LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME + " < " + "'" + mDateManager.getNextDate() + "'"
                + " and " + LocalDataContract.Location.COLUMNS_NAME_LOCATION_TYPE + "=" + "'gps'";

        Log.e(TAG, currentShowDate + " " + mDateManager.getNextDate());
        for(DeviceInformation d : mDevices) {
            Cursor cursor = cr.query(LocalDataContract.Location.CONTENT_URI
                    , projections
                    , where
                    , new String[]{d.serialNumber}
                    , LocalDataContract.Location.COLUMNS_NAME_LOCATION_TIME + " asc");
            List<LatLng> positions = new ArrayList<>();
            while (cursor.moveToNext()) {
                double lat = cursor.getDouble(0);
                double lon = cursor.getDouble(1);
                LatLng latLng = new LatLng(lat, lon);
                Log.e(TAG, "lat:" + latLng.latitude + " lon:" + latLng.longitude);
                positions.add(mCoordinateConverter.coord(latLng).convert());
            }
            Log.e(TAG, positions.size() + "");
            if(positions.size() < 2) {
                positions.clear();
                positions.add(new LatLng(0, 0));
                positions.add(new LatLng(0, 0));
            }
            mOverlaysManager.movePolylineTo(d.serialNumber, positions);
            cursor.close();
        }
    }

    private void initMyCurrentPosition() {
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL
                            , false , null));
        LocationClient locationClient= new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        locationClient.setLocOption(option);
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.e(TAG, "onReceiveLocation");
                MyLocationData locationData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        .latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude())
                        .build();
                mBaiduMap.setMyLocationData(locationData);
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

                MapStatus.Builder msBuilder = new MapStatus.Builder();
                msBuilder.target(latLng).zoom(15.f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(msBuilder.build()));
            }
        });
        locationClient.start();

    }

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mDateManager.setDate(year, monthOfYear, dayOfMonth);
            currentShowDate = mDateManager.showCurrentDate(dateTv);
            new LoadPolylinesAsyncTask().execute();
        }
    };

    private View.OnClickListener onDatePickerButtonClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.date_previous_ib:
                    currentShowDate = mDateManager.showPreviousDate(dateTv);
                    new LoadPolylinesAsyncTask().execute();
                    break;
                case R.id.date_next_ib:
                    if(currentShowDate.equals(currentDate))
                        return;
                    currentShowDate = mDateManager.showNextDate(dateTv);
                    new LoadPolylinesAsyncTask().execute();
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
                    currentMode = MODE_RUNTIME;
                    titleTv.setText(R.string.location_title);
                    datePickerView.setVisibility(View.GONE);
                    refreshFab.setVisibility(View.VISIBLE);
                    mOverlaysManager.showMarkers();
                    break;
                case R.id.trail_fab:
                    currentMode = MODE_HISTORY;
                    titleTv.setText(R.string.trail_title);
                    refreshFab.setVisibility(View.GONE);
                    datePickerView.setVisibility(View.VISIBLE);
                    mBaiduMap.hideInfoWindow();
                    mOverlaysManager.showPolylines();
                    break;
                case R.id.find_fab:
                    currentMode = MODE_FIND;
                    titleTv.setText(R.string.find_title);
                    datePickerView.setVisibility(View.GONE);
                    refreshFab.setVisibility(View.VISIBLE);
                    mBaiduMap.hideInfoWindow();
                    mOverlaysManager.hideOverlays();
                    break;
                case R.id.refresh_fab:
                    UserManagerUtils.getLatestPositionForUser(YsUser.getInstance().getName(), LocationAcitvity.this);
                    refreshFab.setEnabled(false);
                    break;
            }
        }
    };

    private AdapterView.OnItemClickListener onMemberItemClickedListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DeviceInformation device = mDevices.get(position);
            if(currentMode == MODE_RUNTIME) {
                LatLng latLng = mOverlaysManager.getMarkerPosition(device.serialNumber);
                if (latLng.latitude == 0 && latLng.longitude == 0) {
                    DialogUtils.makeToast(LocationAcitvity.this, R.string.no_position_data);
                    return;
                }
                mOverlaysManager.toggleMarkerVisible(mBaiduMap, device.serialNumber);
            }
            else if(currentMode == MODE_HISTORY) {
                if(!mOverlaysManager.hasTrail(device.serialNumber)) {
                    DialogUtils.makeToast(LocationAcitvity.this, R.string.no_trail_data);
                    return;
                }
                mOverlaysManager.togglePolylineVisible(mBaiduMap, device.serialNumber);
            }
        }
    };

    private void showHintDialog() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean showdialog = preferences.getBoolean("show_dialog", true);
        if(!showdialog)
            return;
        View view = LayoutInflater.from(this).inflate(R.layout.location_hint_dialog_view, null);
        final CheckBox checkBox = (CheckBox)view.findViewById(R.id.no_long_show_tv);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.location_title)
                .setView(view)
                .setPositiveButton(R.string.i_know, null)
                .show();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("show_dialog", !isChecked);
                editor.commit();
            }
        });
    }

    private void showDatePickerDialog() {
        mDatePicker.updateDate(mDateManager.getYear(), mDateManager.getMonth(), mDateManager.getDayOfMonth());
        mDatePickerDialog.show();
    }

    private BaiduMap.OnMarkerClickListener onMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            showInforWindow(marker);
            return false;
        }
    };

    private void showInforWindow(Marker marker) {
        mHandler.removeMessages(HIDE_INFOR_WINDOW);
        LatLng latLng = marker.getPosition();
        String title = marker.getTitle();

        TextView textView = new TextView(this);
        textView.setText(title);
        InfoWindow infoWindow = new InfoWindow(textView, latLng, -70);
        mBaiduMap.showInfoWindow(infoWindow);
        mHandler.sendEmptyMessageDelayed(HIDE_INFOR_WINDOW, INFOR_WINDOW_SHOW_TIME);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HIDE_INFOR_WINDOW:
                    mBaiduMap.hideInfoWindow();
                    break;
            }
        }
    };


    @Override
    public void onWorkDone(int resCode) {
        refreshFab.setEnabled(true);
    }

    @Override
    public void onResult(YsData result) {

        PositionData data = (PositionData)result;
        if(data.type.equals("base"))
            return;
        LatLng latLng = new LatLng(data.latitude, data.longitude);

        mOverlaysManager.moveMarkerTo(data.deviceId, mCoordinateConverter.coord(latLng).convert());
        mOverlaysManager.setMarkerTitle(data.deviceId, data.tempTime);

        try {
            data.insert(getContentResolver());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Exception e) {
        refreshFab.setEnabled(true);
    }

    private class InitOverlaysAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;

        public InitOverlaysAsyncTask() {
            dialog = new ProgressDialog(LocationAcitvity.this);
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(dialog.isShowing())
                dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            initMarkerOverlay();
            initPolylineOverlay();
            return null;
        }
    }

    private class LoadPolylinesAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;

        public LoadPolylinesAsyncTask() {
            dialog = new ProgressDialog(LocationAcitvity.this);
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(dialog.isShowing())
                dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadPolylineOverlay();
            return null;
        }
    }


}
