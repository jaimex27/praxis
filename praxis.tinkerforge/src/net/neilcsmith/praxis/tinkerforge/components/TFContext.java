/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2013 Neil C Smith.
 * 
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details.
 * 
 * You should have received a copy of the GNU General Public License version 3
 * along with this work; if not, see http://www.gnu.org/licenses/
 * 
 * 
 * Please visit http://neilcsmith.net if you need additional information or
 * have any questions.
 */
package net.neilcsmith.praxis.tinkerforge.components;

import com.tinkerforge.Device;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.neilcsmith.praxis.util.ArrayUtils;

/**
 *
 * @author Neil C Smith
 */
class TFContext {

    private final Map<String, Device> devices;
    private final Set<Device> locked;
    private final TFRoot root;
    private Listener[] listeners;
    
    TFContext(TFRoot root) {
        devices = new LinkedHashMap<String, Device>();
        locked = new HashSet<Device>();
        this.root = root;
        listeners = new Listener[0];
    }
    
    void addDevice(String uid, Device device) {
//        if (devices.containsKey(uid)) {
//            throw new IllegalStateException("Context already has device for UID: " + uid);
//        }
        devices.put(uid, device);
        fireListeners();
    }
    
    void removeDevice(String uid) {
        Device d = devices.remove(uid);
        if (d != null) {
            locked.remove(d);
        }
        fireListeners();
    }
    
    void removeAll() {
        devices.clear();
        fireListeners();
        locked.clear();
    }
    
    private void fireListeners() {
        for (Listener listener : listeners) {
            listener.stateChanged(this);
        }
    }
    
    public Device findDevice(String uid) {
        return devices.get(uid);
    }
    
    public List<Device> findDevices(Class<? extends Device> type) {
        List<Device> list = new ArrayList<Device>();
        for (Device device : devices.values()) {
            if (type.isInstance(device)) {
                list.add(device);
            }            
        }
        return list;
    }

    public void lockDevice(Device device) throws DeviceLockedException {
        if (!locked.add(device)) {
            throw new DeviceLockedException();
        }
    }

    public void releaseDevice(Device device) {
        locked.remove(device);
    }
    
    public boolean isLocked(Device device) {
        return locked.contains(device);
    }

    public void addListener(Listener listener) {
        listeners = ArrayUtils.add(listeners, listener);
    }

    public void removeListener(Listener listener) {
        listeners = ArrayUtils.remove(listeners, listener);
    }
    
    public long getTime() {
//        return root.getTime();
        return System.nanoTime();
    }
    
    public boolean invokeLater(Runnable task) {
        return root.invokeLater(task);
    }
    
    // @TODO change to deviceAdded, deviceRemoved, deviceReset?
    static interface Listener {

        void stateChanged(TFContext context);
    }

    static class DeviceLockedException extends Exception {
    }
}
