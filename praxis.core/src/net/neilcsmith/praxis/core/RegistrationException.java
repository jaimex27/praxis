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
package net.neilcsmith.praxis.core;

/**
 *
 * @author Neil C Smith
 */
public class RegistrationException extends Exception {

    /**
     * Creates a new instance of
     * <code>PortRegistrationException</code> without detail message.
     */
    public RegistrationException() {
    }

    /**
     * Constructs an instance of
     * <code>PortRegistrationException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RegistrationException(String msg) {
        super(msg);
    }

    public RegistrationException(Throwable cause) {
        super(cause);
    }

    public RegistrationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
