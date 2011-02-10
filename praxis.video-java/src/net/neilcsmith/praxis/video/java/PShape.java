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

package net.neilcsmith.praxis.video.java;

import java.awt.Shape;
import java.awt.geom.Path2D;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class PShape {

    private Path2D.Double path;
    private boolean breakShape;
    private Shape shape;

    private PShape() {
        this.path = new Path2D.Double();
        this.breakShape = true;
    }

    public void vertex(double x, double y) {
        if (breakShape) {
            path.moveTo(x, y);
            breakShape = false;
        } else {
            path.lineTo(x, y);
        }
    }

    public void bezierVertex(double x1, double y1, double x2, double y2,
            double x3, double y3) {
        if (breakShape) {
            path.moveTo(x3, y3);
            breakShape = false;
        } else {
            path.curveTo(x1, y1, x2, y2, x3, y3);
        }
    }

    public void breakShape() {
        breakShape = true;
    }

    public void endShape(boolean close) {
        if (close) {
            path.closePath();
        }
        shape = path;
        path = null;
    }

    Shape getShape() {
        return shape;
    }

    public static PShape beginShape() {
        return new PShape();
    }

}
