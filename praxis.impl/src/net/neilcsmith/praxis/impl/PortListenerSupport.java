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
package net.neilcsmith.praxis.impl;

import net.neilcsmith.praxis.core.Port;
import net.neilcsmith.praxis.core.PortListener;
import net.neilcsmith.praxis.util.ArrayUtils;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class PortListenerSupport {

    Port port;
    PortListener[] listeners;

    public PortListenerSupport(Port port) {
        if (port == null) {
            throw new NullPointerException();
        }
        listeners = new PortListener[0];
    }

    public void addListener(PortListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        listeners = ArrayUtils.add(listeners, listener);
    }

    public void removeListener(PortListener listener) {
        if (listener == null) {
            return;
        }
        listeners = ArrayUtils.remove(listeners, listener);
    }

    public void fireListeners() {
        for (PortListener listener : listeners) {
            listener.connectionsChanged(port);
        }
    }

}
