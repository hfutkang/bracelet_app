package sctek.cn.ysbracelet.map;

import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kang on 16-4-8.
 */
public class OverlaysManager {

    private static final String TAG = OverlaysManager.class.getSimpleName();

    private Map<String, Marker> deviceMarkerMap;
    private Map<String, Polyline> devicePolylineMap;
    private Map<String, Boolean> deviceSelectedMap;

    public OverlaysManager() {
        deviceMarkerMap = new HashMap<>();
        devicePolylineMap = new HashMap<>();
        deviceSelectedMap = new HashMap<>();
    }

    public void addMarker(String device, Marker marker) {
        deviceSelectedMap.put(device, false);
        deviceMarkerMap.put(device, marker);
        marker.setVisible(deviceSelectedMap.get(device));
    }

    public void moveMarkerTo(String device, LatLng latLng) {
        Log.e(TAG, "marker position:" + latLng);
        deviceMarkerMap.get(device).setPosition(latLng);
    }

    public void addPolyline(String device, Polyline polyline) {
        devicePolylineMap.put(device, polyline);
        polyline.setVisible(deviceSelectedMap.get(device));
    }

    public void movePolylineTo(String device, List<LatLng> latLngs) {
        devicePolylineMap.get(device).setPoints(latLngs);
    }

    public void toggleMarkerVisible(BaiduMap map, String device) {
        boolean visible = deviceSelectedMap.get(device);

        Marker marker = deviceMarkerMap.get(device);
        marker.setVisible(!visible);

        deviceSelectedMap.put(device, !visible);

        if(!visible) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(marker.getPosition());
            map.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
        }

    }

    public void togglePolylineVisible(BaiduMap map, String device) {
        boolean visible = deviceSelectedMap.get(device);

        Polyline polyline = devicePolylineMap.get(device);
        polyline.setVisible(!visible);

        deviceSelectedMap.put(device, !visible);

        if(!visible) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(polyline.getPoints().get(0));
            map.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
        }

    }

    public void showMarkers() {
        Collection<String> keys = deviceSelectedMap.keySet();
        for(String key : keys) {
            boolean visible = deviceSelectedMap.get(key);
            Marker marker = deviceMarkerMap.get(key);
            Polyline polyline = devicePolylineMap.get(key);

            marker.setVisible(visible);
            polyline.setVisible(false);
        }
    }

    public void showPolylines() {
        Collection<String> keys = deviceSelectedMap.keySet();
        for(String key : keys) {
            boolean visible = deviceSelectedMap.get(key);
            Marker marker = deviceMarkerMap.get(key);
            Polyline polyline = devicePolylineMap.get(key);

            polyline.setVisible(visible);
            marker.setVisible(false);
        }
    }

    public void hideOverlays() {
        Collection<String> keys = deviceSelectedMap.keySet();
        for(String key : keys) {
            deviceMarkerMap.get(key).setVisible(false);
            devicePolylineMap.get(key).setVisible(false);
        }
    }

    public boolean hasTrail(String device) {
        Polyline polyline = devicePolylineMap.get(device);
        List<LatLng> latLngs = polyline.getPoints();
        if(latLngs.size() <= 2) {
            LatLng latlng = latLngs.get(0);
            if(latlng.latitude == 0) {
                return false;
            }
        }
        return true;
    }

    public LatLng getMarkerPosition(String device) {
        return deviceMarkerMap.get(device).getPosition();
    }

    public void setMarkerTitle(String device, String title) {
        deviceMarkerMap.get(device).setTitle(title);
    }

    public String getMarkerTitle(String device) {
        return deviceMarkerMap.get(device).getTitle();
    }
}
