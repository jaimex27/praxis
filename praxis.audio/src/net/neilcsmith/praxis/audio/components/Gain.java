/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 - Neil C Smith. All rights reserved.
 * 
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details.
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please visit http://neilcsmith.net if you need additional information or
 * have any questions.
 */

package net.neilcsmith.praxis.audio.components;

import net.neilcsmith.praxis.audio.DefaultAudioInputPort;
import net.neilcsmith.praxis.audio.DefaultAudioOutputPort;
import net.neilcsmith.praxis.core.Port;
import net.neilcsmith.praxis.core.types.PMap;
import net.neilcsmith.praxis.impl.AbstractComponent;
import net.neilcsmith.praxis.impl.FloatProperty;

/**
 *
 * @author Neil C Smith
 */
public class Gain extends AbstractComponent {

    private net.neilcsmith.rapl.components.Gain gain;
    
    public Gain() {
        gain = new net.neilcsmith.rapl.components.Gain();
        FloatProperty level =  FloatProperty.create( new GainBinding(), 0, 2, gain.getGain(), PMap.valueOf("scale-hint", "Exponential"));
        registerControl("level", level);
        registerPort("level", level.createPort());
        registerPort(Port.IN, new DefaultAudioInputPort(this, gain));
        registerPort(Port.OUT, new DefaultAudioOutputPort(this, gain));
    }
    
    private class GainBinding implements FloatProperty.Binding {

        public void setBoundValue(long time, double value) {
            gain.setGain((float) value);
        }

        public double getBoundValue() {
            return gain.getGain();
        }
        
    }
    
}
