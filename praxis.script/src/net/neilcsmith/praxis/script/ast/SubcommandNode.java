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

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class SubcommandNode extends CompositeNode {

    public SubcommandNode(List<LineNode> children) {
        super(children);
    }

    @Override
    protected boolean isThisDone() {
        return true;
    }

    @Override
    protected void writeThisNextCommand(List<Argument> args) {
        throw new IllegalStateException();
    }

    @Override
    protected void postThisResponse(List<Argument> args) {
        throw new IllegalStateException();
    }

    @Override
    public void writeResult(List<Argument> args)
            throws ExecutionException {
        Node[] children = getChildren();
        children[children.length - 1].writeResult(args);
    }
}
