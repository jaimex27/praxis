/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2012 Neil C Smith.
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

package net.neilcsmith.praxis.video.gstreamer.components;

import javax.sound.sampled.AudioInputStream;
import net.neilcsmith.praxis.video.render.Surface;
import net.neilcsmith.praxis.video.utils.ResizeMode;

/**
 *
 * @author Neil C Smith
 */
public abstract class VideoDelegate {
    
    public enum State {New, Ready, Playing, Paused, Error, Disposed}; 
    
    private ResizeMode resizeMode;
    
    protected VideoDelegate() {
        resizeMode = new ResizeMode(ResizeMode.Type.Stretch, 0.5, 0.5);
    }
    
    public abstract void process(Surface output);
    
    public void setResizeMode(ResizeMode mode) {
        if (mode == null) {
            throw new NullPointerException();
        }
        resizeMode = mode;
    }
    
    public ResizeMode getResizeMode() {
        return resizeMode;
    }
    
    public long getDuration() {
        return -1;
    }
    
    public long getPosition() {
        return -1;
    }
    
    public void setPosition(long position) {
        throw new UnsupportedOperationException();
    }
    
    public boolean isSeekable() {
        return false;
    }
    
    public boolean isLooping() {
        return false;
    }
    
    public boolean isLoopable() {
        return false;
    }
    
    public void setLooping(boolean loop) {
        if (loop) {
            throw new UnsupportedOperationException();
        }
    }
    
    public boolean isVariableRate() {
        return false;
    }
    
    public double getRate() {
        return 1;
    }
    
    public void setRate(double rate) {
        throw new UnsupportedOperationException();
    }
    
    public boolean isAudioStreamAvailable() {
        return false;
    }
    
    public AudioInputStream getAudioStream() {
        throw new UnsupportedOperationException();
    }
    
    public boolean canWaitOnFrame() {
        return false;
    }
    
    public void setWaitOnFrame(boolean wait) {
        if (wait) {
            throw new UnsupportedOperationException();
        }
    }
    
    public boolean getWaitOnFrame() {
        return false;
    }
    
    public abstract State initialize() throws StateException;
    
    public abstract void play() throws StateException;
    
    public abstract void pause() throws StateException;
    
    public abstract void stop() throws StateException;
    
    public abstract void dispose();
    
    public abstract State getState();

    public static class StateException extends Exception {
        
        public StateException() {}
        
        public StateException(String message) {
            super(message);
        }
        
        public StateException(Throwable cause) {
            super(cause);
        }
        
        public StateException(String message, Throwable cause) {
            super(message, cause);
        }
        
    }
}
