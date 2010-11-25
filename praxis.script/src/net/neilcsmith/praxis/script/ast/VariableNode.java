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

package net.neilcsmith.praxis.script.ast;

import java.util.List;
import java.util.logging.Logger;
import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.script.ExecutionException;
import net.neilcsmith.praxis.script.Namespace;
import net.neilcsmith.praxis.script.Variable;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class VariableNode extends Node {

    private final static Logger log = Logger.getLogger(VariableNode.class.getName());
    
    private String id;
    private Namespace namespace;

    public VariableNode(String id) {
        if (id == null) {
            throw new NullPointerException();
        }
        this.id = id;
        log.finest("Created VariableNode with id : " + id);
    }

    @Override
    public void init(Namespace namespace) {
        super.init(namespace);
        this.namespace = namespace;
    }

    @Override
    public void reset() {
        super.reset();
        this.namespace = null;
    }

    @Override
    public void writeResult(List<Argument> args) throws ExecutionException {
        if (namespace == null) {
            throw new IllegalStateException();
        }
        Variable var = namespace.getVariable(id);
        if (var == null) {
            log.finest("VARIABLE NODE : Can't find variable " + id + " in namespace " + namespace);
            throw new ExecutionException();
        }
        args.add(var.getValue());
    }



}
