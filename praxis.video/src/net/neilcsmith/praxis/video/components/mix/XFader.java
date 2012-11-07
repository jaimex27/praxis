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
package net.neilcsmith.praxis.video.components.mix;

import net.neilcsmith.praxis.core.Port;
import net.neilcsmith.praxis.impl.AbstractComponent;
import net.neilcsmith.praxis.impl.FloatProperty;
import net.neilcsmith.praxis.impl.StringProperty;
import net.neilcsmith.praxis.video.impl.DefaultVideoInputPort;
import net.neilcsmith.praxis.video.impl.DefaultVideoOutputPort;
import net.neilcsmith.praxis.video.pipes.VideoPipe;
import net.neilcsmith.praxis.video.pipes.impl.MultiInOut;
import net.neilcsmith.praxis.video.pipes.impl.Placeholder;
import net.neilcsmith.praxis.video.render.Surface;
import net.neilcsmith.praxis.video.render.ops.Blend;
import net.neilcsmith.praxis.video.render.ops.Blit;

/**
 *
 * @author Neil C Smith
 */
public class XFader extends AbstractComponent {

    private static enum MixMode {

        Normal,
        Add,
        Difference,
        BitXor
    }
    private XFaderPipe mixer;
    private Placeholder pl1;
    private Placeholder pl2;

    public XFader() {
//        try {
        mixer = new XFaderPipe();
        pl1 = new Placeholder();
        pl2 = new Placeholder();
        mixer.addSource(pl1);
        mixer.addSource(pl2);
        registerPort(Port.IN + "-1", new DefaultVideoInputPort(this, pl1));
        registerPort(Port.IN + "-2", new DefaultVideoInputPort(this, pl2));
        registerPort(Port.OUT, new DefaultVideoOutputPort(this, mixer));
        StringProperty mode = createModeControl();
        registerControl("mode", mode);
        registerPort("mode", mode.createPort());
        FloatProperty mix = createMixControl();
        registerControl("mix", mix);
        registerPort("mix", mix.createPort());
//        } catch (SinkIsFullException ex) {
//            Logger.getLogger(XFader.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SourceIsFullException ex) {
//            Logger.getLogger(XFader.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private FloatProperty createMixControl() {
        FloatProperty.Binding binding = new FloatProperty.Binding() {
            @Override
            public void setBoundValue(long time, double value) {
                mixer.setMix(value);
            }

            @Override
            public double getBoundValue() {
                return mixer.getMix();
            }
        };
        return FloatProperty.create(binding, 0, 1, 0);
    }

    private StringProperty createModeControl() {
        MixMode[] modes = MixMode.values();
        String[] allowed = new String[modes.length];
        for (int i = 0; i < modes.length; i++) {
            allowed[i] = modes[i].name();
        }
        StringProperty.Binding binding = new StringProperty.Binding() {
            @Override
            public void setBoundValue(long time, String value) {
                mixer.setMode(MixMode.valueOf(value));
            }

            @Override
            public String getBoundValue() {
                return mixer.getMode().name();
            }
        };
        return StringProperty.create(binding, allowed, mixer.getMode().name());
    }

    private class XFaderPipe extends MultiInOut {

        private double mix;
        private MixMode mode;

        private XFaderPipe() {
            super(2, 1);
            this.mode = MixMode.Normal;
        }

        @Override
        protected void process(Surface[] inputs, Surface output, int idx, boolean rendering) {
            if (!rendering) {
                return;
            }
            switch (inputs.length) {
                case 0:
                    output.clear();
                    break;
                case 1:
                    drawSingle(inputs[0], output);
                    break;
                default:
                    drawComposite(inputs[0], inputs[1], output);
            }
        }

        public void setMix(double mix) {
            mix = mix < 0 ? 0.0 : (mix > 1 ? 1.0 : mix);
            this.mix = mix;
        }

        public double getMix() {
            return this.mix;
        }

        public void setMode(MixMode mode) {
            if (mode == null) {
                throw new NullPointerException();
            }
            this.mode = mode;
        }

        public MixMode getMode() {
            return mode;
        }

        private void drawSingle(Surface input, Surface output) {
            if (mix == 0.0) {
                output.copy(input);
            } else if (mix == 1.0) {
                output.clear();
            } else {
                output.process(Blit.op(Blend.NORMAL.opacity(1 - mix)), input);
            }
            input.release();
        }

        private void drawComposite(Surface input1, Surface input2, Surface output) {
            if (mix == 0.0) {
                output.process(Blit.op(), input1);
            } else if (mix == 1.0) {
                output.process(Blit.op(), input2);
            } else {

                switch (mode) {
                    case Add:
                        renderAdd(input1, input2, output);
                        break;
                    case Difference:
                        renderDifference(input1, input2, output);
                        break;
                    case BitXor:
                        renderBitXor(input1, input2, output);
                        break;
                    default:
                        renderBlend(input1, input2, output);
                }
            }
        }

        private void renderBlend(Surface input1, Surface input2, Surface output) {
            if (output.hasAlpha()) {
                output.process(Blit.op(Blend.ADD.opacity(1 - mix)), input1);
                output.process(Blit.op(Blend.ADD.opacity(mix)), input2);
                input1.release();
            } else {
                output.copy(input1);
                input1.release();
                output.process(Blit.op(Blend.NORMAL.opacity(mix)), input2);
            }
            input2.release();
        }

        private void renderBitXor(Surface input1, Surface input2, Surface output) {
            double alpha;
            Surface src;
            Surface dst;
            if (mix >= 0.5) {
                alpha = (1.0 - mix) * 2;
                src = input1;
                dst = input2;
            } else {
                alpha = mix * 2;
                src = input2;
                dst = input1;
            }
            output.copy(dst);
            dst.release();
            output.process(Blit.op(Blend.BITXOR.opacity(alpha)), src);
            src.release();
        }
        
        private void renderAdd(Surface input1, Surface input2, Surface output) {
            double alpha;
            Surface src;
            Surface dst;
            if (mix >= 0.5) {
                alpha = (1.0 - mix) * 2;
                src = input1;
                dst = input2;
            } else {
                alpha = mix * 2;
                src = input2;
                dst = input1;
            }
            output.copy(dst);
            dst.release();
            output.process(Blit.op(Blend.ADD.opacity(alpha)), src);
            src.release();
        }
        
        private void renderDifference(Surface input1, Surface input2, Surface output) {
            double alpha;
            Surface src;
            Surface dst;
            if (mix >= 0.5) {
                alpha = (1.0 - mix) * 2;
                src = input1;
                dst = input2;
            } else {
                alpha = mix * 2;
                src = input2;
                dst = input1;
            }
            output.copy(dst);
            dst.release();
            output.process(Blit.op(Blend.DIFFERENCE.opacity(alpha)), src);
            src.release();
        }

       

        @Override
        public boolean isRenderRequired(VideoPipe source, long time) {
            if (mix == 0.0) {
                if (getSourceCount() > 1 && source == getSource(1)) {
                    return false;
                }
            } else if (mix == 1.0) {
                if (getSourceCount() > 0 && source == getSource(0)) {
                    return false;
                }
            }
            return super.isRenderRequired(source, time);

        }
    }
}
