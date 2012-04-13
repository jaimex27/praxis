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
package net.neilcsmith.ripl.components.mix;

import net.neilcsmith.ripl.Source;
import net.neilcsmith.ripl.Surface;
import net.neilcsmith.ripl.SurfaceOp;
import net.neilcsmith.ripl.impl.AbstractInOut;
import net.neilcsmith.ripl.ops.Blend;
import net.neilcsmith.ripl.ops.Blit;

/**
 *
 * @author Neil C Smith
 */
public class Composite extends AbstractInOut {



    public static enum Mode {

        Normal,
        Add,
        Sub,
        Difference,
        Multiply,
        Screen,
        BitXor,
        Mask
    }
    private Mode mode = Mode.Normal;
    private double mix = 1.0;
    private boolean forceAlpha;
    private SurfaceOp blit;
    private Surface src;

    public Composite() {
        super(2,1,false);
//        comp = SurfaceComposite.create(Mode.SrcOver, 1.0);
    }

    public void setMix(double mix) {
        if (mix < 0 || mix > 1) {
            throw new IllegalArgumentException();
        }
        this.mix = mix;
        blit = null;
    }

    public double getMix() {
        return mix;
    }

    public void setMode(Mode mode) {
        if (mode == null) {
            throw new NullPointerException();
        }
        this.mode = mode;
        blit = null;
    }

    public Mode getMode() {
        return mode;
    }

    public void setForceAlpha(boolean forceAlpha) {
        this.forceAlpha = forceAlpha;
    }

    public boolean getForceAlpha() {
        return forceAlpha;
    }

    @Override
    protected void callSources(Surface surface, long time, boolean rendering) {
        int count = getSourceCount();
        if (count == 0) {
            return;
        } else {
            getSource(0).process(surface, this, time);
        }
        if (count == 2) {
            if (forceAlpha) {
                if (src == null || !src.hasAlpha() || !surface.checkCompatible(src, true, false)) {
                    src = surface.createSurface(surface.getWidth(), surface.getHeight(), true, null);
                }
            } else {
                if (src == null || !surface.checkCompatible(src, true, true)) {
                    src = surface.createSurface(null);
                }
            }
            getSource(1).process(src, this, time);
        }
    }

    @Override
    protected void process(Surface surface, boolean rendering) {
        if (rendering) {
            switch (getSourceCount()) {
                case 0:
                    surface.clear();
                    break;
                case 1:
                    // no op
                    break;
                default:
                    drawComposite(surface);
            }
        }

    }

    @Override
    public boolean isRenderRequired(Source source, long time) {
        if (mix == 0.0) {
            if (getSourceCount() > 1 && source == getSource(1)) {
                return false;
            }
        }
        return super.isRenderRequired(source, time);

    }

    private void drawComposite(Surface surface) {

//        if (comp == null) {
//            comp = createComposite(mode, (float) mix);
//        }
//        Surface src = getInputSurface(1);
//        Surface dst = getInputSurface(0);
//        // composite into dst in case surface doesn't have alpha
//        SurfaceGraphics g = dst.getGraphics();
//        g.setComposite(comp);
//        g.drawSurface(src, 0, 0);
//        surface.getGraphics().drawSurface(dst, 0, 0);

        if (blit == null) {
            blit = createBlit(mode, mix);
        }
        surface.process(blit, src);
        src.release();
    }


     private SurfaceOp createBlit(Mode mode, double mix) {
        switch (mode) {
            case Normal:
                return Blit.op(Blend.NORMAL.opacity(mix));
            case Add:
                return Blit.op(Blend.ADD.opacity(mix));
            case Sub:
                return Blit.op(Blend.SUB.opacity(mix));
            case Difference:
                return Blit.op(Blend.DIFFERENCE.opacity(mix));
            case Multiply:
                return Blit.op(Blend.MULTIPLY.opacity(mix));
            case Screen:
                return Blit.op(Blend.SCREEN.opacity(mix));
            case BitXor:
                return Blit.op(Blend.BITXOR.opacity(mix));
            case Mask:
                return Blit.op(Blend.MASK.opacity(mix));
            default:
                throw new IllegalArgumentException();
        }
    }
}

