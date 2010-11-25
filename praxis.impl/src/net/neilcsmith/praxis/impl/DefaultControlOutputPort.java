/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2010 Neil C Smith.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.core.Component;
import net.neilcsmith.praxis.core.ControlPort;
import net.neilcsmith.praxis.core.Port;
import net.neilcsmith.praxis.core.PortConnectionException;
import net.neilcsmith.praxis.core.PortListener;
import net.neilcsmith.praxis.core.info.PortInfo;

/**
 *
 * @author Neil C Smith
 */
public class DefaultControlOutputPort extends ControlPort.Output {

    private ControlPort.Input[] connections;
    private PortListenerSupport pls;
    private boolean sending;
    private final PortInfo info;

    public DefaultControlOutputPort(Component component) {
        connections = new ControlPort.Input[0];
        pls = new PortListenerSupport(this);
        info = PortInfo.create(ControlPort.class, PortInfo.Direction.OUT, null);
    }

    public void connect(Port port) throws PortConnectionException {
        if (port instanceof ControlPort.Input) {
            ControlPort.Input cport = (ControlPort.Input) port;
            List<ControlPort.Input> cons = Arrays.asList(connections);
            if (cons.contains(cport)) {
                throw new PortConnectionException();
            }
            makeConnection(cport);
            cons = new ArrayList<ControlPort.Input>(cons);
            cons.add(cport);
//            Collections.sort(cons, sorter);
            connections = cons.toArray(new ControlPort.Input[cons.size()]);
            pls.fireListeners();
        } else {
            throw new PortConnectionException();
        }
    }

    public void disconnect(Port port) {
        if (port instanceof ControlPort.Input) {
            ControlPort.Input cport = (ControlPort.Input) port;
            List<ControlPort.Input> cons = Arrays.asList(connections);
            int idx = cons.indexOf(cport);
            if (idx > -1) {
                breakConnection(cport);
                cons = new ArrayList<ControlPort.Input>(cons);
                cons.remove(idx);
                connections = cons.toArray(new ControlPort.Input[cons.size()]);
                pls.fireListeners();
            }
        }
    }

    public void disconnectAll() {
        if (connections.length == 0) {
            return;
        }
        for (ControlPort.Input port : connections) {
            breakConnection(port);
        }
        connections = new ControlPort.Input[0];
        pls.fireListeners();
    }

    public Port[] getConnections() {
        return Arrays.copyOf(connections, connections.length);
    }

    public void addListener(PortListener listener) {
        pls.addListener(listener);
    }

    public void removeListener(PortListener listener) {
        pls.removeListener(listener);
    }

    public PortInfo getInfo() {
        return info;
    }

    public void send(long time, double value) {
        if (sending) {
            return; // @TODO recursion strategy - allow up to maximum count?
        }
        sending = true;
        for (ControlPort.Input port : connections) {
            try {
                port.receive(time, value);
            } catch (Exception ex) {
                // @TODO log errors
            }

        }
        sending = false;
    }

//    public void send(int value) {
//        if (sending) {
//            return;
//        }
//        sending = true;
//        for (ControlPort.Input port : connections) {
//            port.receive(value);
//        }
//        sending = false;
//    }
    public void send(long time, Argument value) {
        if (sending) {
            return;
        }
        sending = true;
        for (ControlPort.Input port : connections) {
            try {
                port.receive(time, value);
            } catch (Exception ex) {
                // @TODO log errors
            }
        }
        sending = false;
    }
}
