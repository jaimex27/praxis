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

package net.neilcsmith.praxis.script;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class ExecutionException extends Exception {

    /**
     * Creates a new instance of <code>ExecutionException</code> without detail message.
     */
    public ExecutionException() {
    }


    /**
     * Constructs an instance of <code>ExecutionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ExecutionException(String msg) {
        super(msg);
    }

    public ExecutionException(Throwable cause) {
        super(cause);
    }

    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
