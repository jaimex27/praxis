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
package net.neilcsmith.praxis.terminal.impl;

import net.neilcsmith.praxis.core.Call;
import net.neilcsmith.praxis.core.CallArguments;
import net.neilcsmith.praxis.core.ControlAddress;
import net.neilcsmith.praxis.core.PacketRouter;
import net.neilcsmith.praxis.core.info.ControlInfo;
import net.neilcsmith.praxis.core.interfaces.ScriptService;
import net.neilcsmith.praxis.core.types.PString;
import net.neilcsmith.praxis.impl.AbstractControl;
import net.neilcsmith.praxis.terminal.Terminal;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class DefaultTerminalContext extends AbstractControl implements Terminal.Context {

    private Call activeCall;
    private Terminal terminal;

    public DefaultTerminalContext(Terminal terminal) {
        if (terminal == null) {
            throw new NullPointerException();
        }
        this.terminal = terminal;
    }

    @Override
    public void eval(String script) throws Exception {
        ControlAddress to = ControlAddress.create(
                findService(ScriptService.INSTANCE),
                ScriptService.EVAL);
        Call call = Call.createCall(to, getAddress(), System.nanoTime(), PString.valueOf(script));
        route(call);
        activeCall = call;
    }

    @Override
    public void clear() throws Exception {
        ControlAddress to = ControlAddress.create(
                findService(ScriptService.INSTANCE),
                ScriptService.CLEAR);
        Call call = Call.createQuietCall(to, getAddress(), System.nanoTime(), CallArguments.EMPTY);
        route(call);
        activeCall = null;
    }

    @Override
    public void call(Call call, PacketRouter router) throws Exception {
        switch (call.getType()) {
            case RETURN:
                if (call.getMatchID() == activeCall.getMatchID()) {
                    terminal.processResponse(call.getArgs());
                    activeCall = null;
                }
                break;
            case ERROR:
                if (call.getMatchID() == activeCall.getMatchID()) {
                    terminal.processError(call.getArgs());
                    activeCall = null;
                }
                break;
            default:

        }
    }

    @Override
    public ControlInfo getInfo() {
        return null;
    }

    private void route(Call call) {
        PacketRouter router = getLookup().get(PacketRouter.class);
        router.route(call);
    }
}
