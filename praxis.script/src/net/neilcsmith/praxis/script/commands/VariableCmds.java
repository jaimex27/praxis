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
package net.neilcsmith.praxis.script.commands;

import net.neilcsmith.praxis.script.impl.VariableImpl;
import java.util.Map;
import java.util.logging.Logger;
import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.core.CallArguments;
import net.neilcsmith.praxis.script.Command;
import net.neilcsmith.praxis.script.CommandInstaller;
import net.neilcsmith.praxis.script.Env;
import net.neilcsmith.praxis.script.ExecutionException;
import net.neilcsmith.praxis.script.Namespace;
import net.neilcsmith.praxis.script.Variable;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class VariableCmds implements CommandInstaller {

    private final static VariableCmds instance = new VariableCmds();

    private final static Command SET = new Set();


    private final static Logger log = Logger.getLogger(VariableCmds.class.getName());


    private VariableCmds() {}

    public void install(Map<String, Command> commands) {
        commands.put("set", SET);
    }

    public static VariableCmds getInstance() {
        return instance;
    }

    private static class Set extends AbstractInlineCommand {

        public CallArguments process(Env context, Namespace namespace, CallArguments args) throws ExecutionException {
            if (args.getSize() != 2) {
                throw new ExecutionException();
            }
            String varName = args.get(0).toString();
            Argument val = args.get(1);
            Variable var = namespace.getVariable(varName);
            if (var != null) {
                var.setValue(val);
            } else {
                log.finest("SET COMMAND : Adding variable " + varName + " to namespace " + namespace);
                var = new VariableImpl(val);
                namespace.addVariable(varName, var);
            }
            return CallArguments.create(val);

        }
    }
}
