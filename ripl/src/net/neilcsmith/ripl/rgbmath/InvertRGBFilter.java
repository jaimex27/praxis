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

package net.neilcsmith.ripl.rgbmath;

/**
 *
 * @author Neil C Smith
 */
public class InvertRGBFilter implements RGBSinglePixelFilter {
    
    private static InvertRGBFilter instance = new InvertRGBFilter();
    
    private InvertRGBFilter() {}

    public void filterRGB(int[] src, int srcPos, int[] dest, int destPos, int length) {
        for (int i=0; i<length; i++) {
            dest[destPos] = ~src[srcPos] & 0x00ffffff;
            destPos++;
            srcPos++;
        }
    }

    public void filterARGB(int[] src, int srcPos, int[] dest, int destPos, int length) {
        // no op
    }
    
    public static InvertRGBFilter getInstance() {
        return instance;
    }
}
