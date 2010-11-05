/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 - Neil C Smith. All rights reserved.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details.
 *
 * You should have received a copy of the GNU General Public License version 2
 * along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please visit http://neilcsmith.net if you need additional information or
 * have any questions.
 */
package net.neilcsmith.praxis.script.commands;

import net.neilcsmith.praxis.core.Call;
import net.neilcsmith.praxis.core.CallArguments;
import net.neilcsmith.praxis.core.types.PReference;
import net.neilcsmith.praxis.script.Env;
import net.neilcsmith.praxis.script.Namespace;
import net.neilcsmith.praxis.script.StackFrame;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public abstract class AbstractSingleCallFrame implements StackFrame {

    private Namespace namespace;
    private CallArguments args;
    private State state;
    private Call call;
    private CallArguments result;

    protected AbstractSingleCallFrame(Namespace namespace, CallArguments args) {
        if (namespace == null || args == null) {
            throw new NullPointerException();
        }
        this.namespace = namespace;
        this.args = args;
        state = State.Incomplete;
    }

    public final State getState() {
        return state;
    }

    public final Namespace getNamespace() {
        return namespace;
    }

    public final StackFrame process(Env env) {
        if (state == State.Incomplete && call == null) {
            try {
                call = createCall(env, args);
                if (call == null || call.getType() != Call.Type.INVOKE) {
                    throw new IllegalStateException("Invalid call");
                }
                env.getPacketRouter().route(call);
            } catch (Exception ex) {
                result = CallArguments.create(PReference.wrap(ex));
                state = State.Error;
            }
        }
        return null;
    }

    public final void postResponse(Call response) {
        if (call != null && response.getMatchID() == call.getMatchID()) {
            call = null;
            result = response.getArgs();
            if (response.getType() == Call.Type.RETURN) {
                result = processResult(result);
                state = State.OK;
            } else {
                state = State.Error;
            }
        }
    }

    public final void postResponse(State state, CallArguments args) {
        throw new IllegalStateException();
    }

    public final CallArguments getResult() {
        if (result == null) {
            throw new IllegalStateException();
        }
        return result;
    }

    protected abstract Call createCall(Env env, CallArguments args) throws Exception;

    protected CallArguments processResult(CallArguments result) {
        return result;
    }

}

