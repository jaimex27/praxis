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

package net.neilcsmith.ripl.ops;

import net.neilcsmith.ripl.PixelData;
import net.neilcsmith.ripl.SurfaceOp;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class Reverse implements SurfaceOp {

    private SurfaceOp op;
    private PixelData target;

    private Reverse(SurfaceOp op, PixelData target) {
        this.op = op;
        this.target = target;
    }

    public void process(PixelData output, PixelData... inputs) {
        op.process(target, output);
    }

    public static SurfaceOp op(SurfaceOp op, PixelData target) {
        if (op == null || target == null) {
            throw new NullPointerException();
        }
        return new Reverse(op, target);
    }

}
