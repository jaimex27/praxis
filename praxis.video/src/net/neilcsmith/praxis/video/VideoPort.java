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
package net.neilcsmith.praxis.video;

import net.neilcsmith.praxis.core.Port;
import net.neilcsmith.praxis.core.PortConnectionException;
import net.neilcsmith.praxis.core.info.PortInfo;
import net.neilcsmith.praxis.core.types.PMap;
import net.neilcsmith.praxis.video.pipes.VideoPipe;


/**
 *
 * @author Neil C Smith
 */
public abstract class VideoPort implements Port {

    public static abstract class Input extends VideoPort {
        
        private PortInfo info;
        
        public Input() {
            this(PMap.EMPTY);
        }
        
        public Input(PMap properties) {
            info = PortInfo.create(VideoPort.class, PortInfo.Direction.IN, properties);
        }

        public final void connect(Port port) throws PortConnectionException {
            if (port instanceof Output) {
                port.connect(this);
            } else {
                throw new PortConnectionException();
            }
            
        }

        public final void disconnect(Port port) {
            if (port instanceof Output) {
                port.disconnect(this);
            }
            
        }
        
        public final PortInfo getInfo() {
            return info;
        }

        protected abstract void addVideoOutputPort(Output port, VideoPipe source) throws PortConnectionException;

        protected abstract void removeVideoOutputPort(Output port, VideoPipe source);
    }

    public static abstract class Output extends VideoPort {
        
        private PortInfo info;
        
        public Output() {
            this(PMap.EMPTY);
        }
        
        public Output(PMap properties) {
            info = PortInfo.create(VideoPort.class, PortInfo.Direction.OUT, properties);
        }
        
        public final PortInfo getInfo() {
            return info;
        }

        protected final void makeConnection(Input port, VideoPipe source) throws PortConnectionException {
            port.addVideoOutputPort(this, source);
        }

        protected final void breakConnection(Input port, VideoPipe source) {
            port.removeVideoOutputPort(this, source);
        }
    }
}
