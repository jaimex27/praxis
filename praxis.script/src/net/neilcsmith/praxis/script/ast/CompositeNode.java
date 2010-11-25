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
import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.script.ExecutionException;
import net.neilcsmith.praxis.script.Namespace;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public abstract class CompositeNode extends Node {
    
    private Node[] children;
    private int active;
    private Namespace namespace;

    public CompositeNode(List<? extends Node> children) {
        this.children = children.toArray(new Node[children.size()]);
    }

      @Override
    public void init(Namespace namespace) {
        if (namespace == null) {
            throw new NullPointerException();
        }
        if (this.namespace != null) {
            throw new IllegalStateException();
        }
        this.namespace = namespace;
        active = 0;
        for (Node child : children) {
            child.init(namespace);
        }
    }

    @Override
    public boolean isDone() {
        if (namespace == null) {
            throw new IllegalStateException();
        }
        if (active < 0) {
            return isThisDone();
        } else {
            while (active < children.length) {
                if (!children[active].isDone()) {
                    return false;
                }
                active++;
            }
            active = -1;
            return isThisDone();
        }
    }

    protected abstract boolean isThisDone();

    @Override
    public void writeNextCommand(List<Argument> args) 
            throws ExecutionException {
        if (namespace == null) {
            throw new IllegalStateException();
        }
        if (active >= 0) {
            children[active].writeNextCommand(args);
        } else {
            writeThisNextCommand(args);
        }
    }

    protected abstract void writeThisNextCommand(List<Argument> args)
            throws ExecutionException;


    @Override
    public void postResponse(List<Argument> args) 
            throws ExecutionException {
        if (namespace == null) {
            throw new IllegalStateException();
        }
        if (active >= 0) {
            children[active].postResponse(args);
        } else {
            postThisResponse(args);
        }
    }

    protected abstract void postThisResponse(List<Argument> args)
            throws ExecutionException;

    @Override
    public void reset() {
        for (Node child : children) {
            child.reset();
        }
        namespace = null;
    }
    
    protected Node[] getChildren() {
        return children;
    }



}
