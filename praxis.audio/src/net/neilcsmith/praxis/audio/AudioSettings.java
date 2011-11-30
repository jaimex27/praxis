/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2011 Neil C Smith.
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
package net.neilcsmith.praxis.audio;

import net.neilcsmith.praxis.settings.Settings;

/**
 *
 * @author Neil C Smith <http://neilcsmith.net>
 */
public class AudioSettings {
    
    public final static String KEY_LIBRARY = "audio.library";
    public final static String KEY_DEVICE = "audio.device";
    public final static String KEY_SAMPLERATE = "audio.samplerate";
    public final static String KEY_BUFFERSIZE = "audio.buffersize";
    
    private static int DEFAULT_SAMPLERATE = 44100;
    private static int DEFAULT_BUFFERSIZE = 512;
    private static String DEFAULT_LIBRARY = "JavaSound";
    
    private AudioSettings() {}
    
    public static String getLibrary() {
        return Settings.get(KEY_LIBRARY, DEFAULT_LIBRARY);
    }
    
    public static void setLibrary(String library) {
        Settings.put(KEY_LIBRARY, library);
    }
    
    public static int getSamplerate() {
        return Settings.getInt(KEY_SAMPLERATE, DEFAULT_SAMPLERATE);
    }
    
    public static void setSamplerate(int samplerate) {
        Settings.putInt(KEY_SAMPLERATE, samplerate);
    }
    
    public static int getBuffersize() {
        return Settings.getInt(KEY_BUFFERSIZE, DEFAULT_BUFFERSIZE);
    }
    
    public static void setBuffersize(int buffersize) {
        Settings.putInt(KEY_BUFFERSIZE, buffersize);
    }
    
}
