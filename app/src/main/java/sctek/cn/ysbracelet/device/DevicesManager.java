package sctek.cn.ysbracelet.device;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kang on 16-2-24.
 */
public class DevicesManager {

    private ArrayList<DeviceInformation> mDevices;

    public DevicesManager() {
        mDevices = new ArrayList<>();
    }

    public boolean isExist(DeviceInformation device) {
        Iterator<DeviceInformation> iterator = mDevices.iterator();
        while (iterator.hasNext()) {
            DeviceInformation temp = iterator.next();
            if(temp.getMac().equals(device.getMac()))
                return true;
        }
        return false;
    }

    public void addDevice(DeviceInformation device) {
        mDevices.add(device);
    }

    public void deleteDevice(DeviceInformation device) {
        Iterator<DeviceInformation> iterator = mDevices.iterator();
        while (iterator.hasNext()) {
            DeviceInformation temp = iterator.next();
            if(temp.getMac().equals(device.getMac()))
                iterator.remove();
        }
    }

    public int getCount() {
        return mDevices.size();
    }

    public DeviceInformation getDevice(int index) {
        return mDevices.get(index);
    }

    public DeviceInformation getDevice(String id) {
        for (DeviceInformation d : mDevices) {
            if(d.serialNumber.equals(id)) {
                return d;
            }
        }
        return null;
    }

    public DeviceInformation getDeviceByMac(String mac) {
        for (DeviceInformation d : mDevices) {
            if(d.mac.equals(mac)) {
                return d;
            }
        }
        return null;
    }

    public List<DeviceInformation> getDevices() { return mDevices; }

    public void clear() {
        mDevices.clear();
    }

}
