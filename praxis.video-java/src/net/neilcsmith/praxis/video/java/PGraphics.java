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
 *
 *
 * Parts of the API of this package, as well as some of the code, is derived from
 * the Processing project (http://processing.org)
 *
 * Copyright (c) 2004-09 Ben Fry and Casey Reas
 * Copyright (c) 2001-04 Massachusetts Institute of Technology
 *
 */
package net.neilcsmith.praxis.video.java;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import net.neilcsmith.ripl.Surface;
import net.neilcsmith.ripl.SurfaceOp;
import net.neilcsmith.ripl.ops.Blend;
import net.neilcsmith.ripl.ops.Blit;
import net.neilcsmith.ripl.ops.Bounds;
import net.neilcsmith.ripl.ops.RectFill;
import net.neilcsmith.ripl.ops.ScaledBlit;
import net.neilcsmith.ripl.ops.ShapeRender;
import static net.neilcsmith.praxis.video.java.VideoConstants.*;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class PGraphics {

    private final static Logger LOG = Logger.getLogger(PGraphics.class.getName());
    private final static Set<String> warned = new HashSet<String>();
    private final static double alphaOpaque = 0.999;
    private PImage image;
    private Blend blend = Blend.NORMAL;
    private Color fillColor = Color.WHITE;
    private Color strokeColor = Color.BLACK;
    private BasicStroke stroke = new BasicStroke(1);
    private PShape shape;

    public PGraphics(PImage image) {
        if (image == null) {
            throw new NullPointerException();
        }
        this.image = image;
    }

    public PImage getImage() {
        return image;
    }

    private static void warn(String msg) {
        if (!warned.contains(msg)) {
            LOG.warning(msg);
            warned.add(msg);
        }
    }

    // BEGINNING OF PUBLIC DRAWING METHODS

    public void background(double grey) {
        background(grey, grey, grey, 255);
    }

    public void background(double grey, double alpha) {
        background(grey, grey, grey, alpha);
    }

    public void background(double r, double g, double b) {
        background(r, g, b, 255);
    }

    public void background(double r, double g, double b, double a) {
        image.clear();
        int ir = round(r);
        int ig = round(g);
        int ib = round(b);
        if (ir == 0 && ig == 0 && ib == 0) {
            return;
        }
        int ia = round(a);
        Color bg = new Color(ir, ig, ib, ia);
        image.process(RectFill.op(bg, NORMAL, 0, 0,
                image.getWidth(), image.getHeight()));
    }

    public void beginShape() {
        shape = PShape.beginShape();
    }

    public void bezier(double x1, double y1,
                     double x2, double y2,
                     double x3, double y3,
                     double x4, double y4) {
    beginShape();
    vertex(x1, y1);
    bezierVertex(x2, y2, x3, y3, x4, y4);
    endShape();
  }

    public void bezierVertex(double x1, double y1,
            double x2, double y2, double x3, double y3) {
        shape.bezierVertex(x1, y1, x2, y2, x3, y3);
    }

    public void blendMode(Blend blend) {
        if (blend == null) {
            throw new NullPointerException();
        }
        this.blend = blend;
    }

    public void breakShape() {
        shape.breakShape();
    }

    public void clear() {
        image.clear();
    }

    public void ellipse(double x, double y, double w, double h) {
        x -= (w / 2);
        y -= (h / 2);
        renderShape(new Ellipse2D.Double(x, y, w, h));
    }

    public void endShape() {
        endShape(OPEN);
    }

    public void endShape(boolean close) {
        shape.endShape(close);
        renderShape(shape.getShape());
    }

    public void fill(double grey) {
        fill(grey, grey, grey, 255);
    }

    public void fill(double grey, double alpha) {
        fill(grey, grey, grey, alpha);
    }

    public void fill(double r, double g, double b) {
        fill(r, g, b, 255);
    }

    public void fill(double r, double g, double b, double a) {
        fillColor = new Color(round(r), round(g), round(b), round(a));
    }

    public void image(PImage src, double x, double y) {
        image.process(Blit.op(blend, (int) x, (int) y), src);
    }

    public void image(PImage src, double x, double y, double w, double h,
            double u, double v) {
        image.process(Blit.op(blend,
                new Bounds((int) u, (int) v, (int) w, (int) h),
                (int) x, (int) y), src);
    }

    public void image(PImage src, double x, double y, double w, double h) {
        //image(src, x, y, w, h, 0, 0, image.width, image.height);
        // @TODO check this is correct
        image(src, x, y, w, h, 0, 0, src.width, src.height);
    }

    public void image(PImage src, double x, double y, double w, double h,
            double u1, double v1, double u2, double v2) {
        int ix = (int) x;
        int iy = (int) y;
        int iw = (int) w;
        int ih = (int) h;
        int iu1 = (int) u1;
        int iv1 = (int) v1;
        int iu2 = (int) u2;
        int iv2 = (int) v2;
        int srcW = iu2 - iu1;
        int srcH = iv2 - iv1;
        if (iw == srcW && ih == srcH) {
            image.process(Blit.op(blend, new Bounds(iu1, iv1, srcW, srcH), ix, iy), src);
        } else {
            image.process(ScaledBlit.op(blend, new Bounds(iu1, iv1, srcW, srcH),
                    new Bounds(ix, iy, iw, ih)), src);
        }
    }

    public void line(double x1, double y1, double x2, double y2) {
        if (strokeColor == null) {
            return;
        }
        renderShape(new Line2D.Double(x1, y1, x2, y2));
    }

    public void noFill() {
        fillColor = null;
    }

    public void noSmooth() {
    }

    public void noStroke() {
        strokeColor = null;
    }

    public void op(SurfaceOp op) {
        image.process(op);
    }

    public void op(SurfaceOp op, Surface src) {
        image.process(op, new PImage(src));
    }

    public void op(SurfaceOp op, PImage src) {
        image.process(op, src);
    }

    public void point(double x, double y) {
//        line(x, y, x, y);
        image.process(RectFill.op(strokeColor, blend, round(x), round(y),
                1, 1));
    }

    public void quad(double x1, double y1, double x2, double y2,
            double x3, double y3, double x4, double y4) {
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        xPoints[0] = (int) x1;
        xPoints[1] = (int) x2;
        xPoints[2] = (int) x3;
        xPoints[3] = (int) x4;
        yPoints[0] = (int) y1;
        yPoints[1] = (int) y2;
        yPoints[2] = (int) y3;
        yPoints[3] = (int) y4;
        polygon(xPoints, yPoints, 4);
    }

    public void rect(double x, double y, double w, double h) {
        if (strokeColor == null) {
            if (fillColor == null) {
                return;
            }
            image.process(RectFill.op(fillColor, blend, (int) x, (int) y, 
                    (int) (w + 0.5), (int) (h + 0.5)));
        } else {
            renderShape(new Rectangle2D.Double(x, y, w, h));
        }
        
        
    }

    public void smooth() {
    }

    public void stroke(double grey) {
        stroke(grey, grey, grey, 255);
    }

    public void stroke(double grey, double alpha) {
        stroke(grey, grey, grey, alpha);
    }

    public void stroke(double r, double g, double b) {
        stroke(r, g, b, 255);
    }

    public void stroke(double r, double g, double b, double a) {
        strokeColor = new Color(round(r), round(g), round(b), round(a));
    }

    public void strokeWeight(double weight) {
        if (weight < 1) {
            weight = 1;
        }
        stroke = new BasicStroke((float) weight);
    }

    public void triangle(double x1, double y1, double x2, double y2,
            double x3, double y3) {
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        xPoints[0] = (int) x1;
        xPoints[1] = (int) x2;
        xPoints[2] = (int) x3;
        yPoints[0] = (int) y1;
        yPoints[1] = (int) y2;
        yPoints[2] = (int) y3;
        polygon(xPoints, yPoints, 3);
    }

    public void vertex(double x, double y) {
        shape.vertex(x, y);
    }

    // END OF PUBLIC DRAWING METHODS


    private void renderShape(Shape shape) {
        if (strokeColor == null) {
            if (fillColor == null) {
                return;
            } else {
                image.process(ShapeRender.op(shape, fillColor, blend));
            }
        } else {
            image.process(ShapeRender.op(shape, fillColor, stroke, strokeColor, blend));
        }
    }

    private void polygon(final int[] xPoints, final int[] yPoints, final int nPoints) {
        renderShape(new Polygon(xPoints, yPoints, nPoints));
    }

    private int round(double x) {
        return (int) (x + 0.5);
    }
}
