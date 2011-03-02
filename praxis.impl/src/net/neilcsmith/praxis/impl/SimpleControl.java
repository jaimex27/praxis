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
package net.neilcsmith.praxis.impl;

import java.util.logging.Logger;
import net.neilcsmith.praxis.core.Call;
import net.neilcsmith.praxis.core.CallArguments;
import net.neilcsmith.praxis.core.Control;
import net.neilcsmith.praxis.core.PacketRouter;
import net.neilcsmith.praxis.core.info.ControlInfo;

/**
 *
 * @author Neil C Smith
 */
public abstract class SimpleControl implements Control {

    private final static Logger LOG = Logger.getLogger(SimpleControl.class.getName());
    
    private ControlInfo info;

    protected SimpleControl(ControlInfo info) {
        this.info = info;
    }

    public final void call(Call call, PacketRouter router) throws Exception {
        CallArguments out = null;
        switch (call.getType()) {
            case INVOKE:
                out = process(call.getTimecode(), call.getArgs(), false);
                if (out == null) {
                    throw new Exception("No response returned from INVOKE\n" + call);
                }
                break;
            case INVOKE_QUIET:
                out = process(call.getTimecode(), call.getArgs(), true);
                break;
            default:
                LOG.warning("Unexpected call - \n" + call);
        }
        if (out != null) {
            router.route(Call.createReturnCall(call, out));
        }
    }

    public final ControlInfo getInfo() {
        return info;
    }

    protected abstract CallArguments process(long time, CallArguments args, boolean quiet) throws Exception;
}
