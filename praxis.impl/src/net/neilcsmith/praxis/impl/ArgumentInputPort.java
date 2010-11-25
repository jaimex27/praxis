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

import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.core.Component;
import net.neilcsmith.praxis.core.types.PNumber;

/**
 *
 * @author Neil C Smith
 */
public class ArgumentInputPort extends AbstractControlInputPort {

    private Binding binding;

    private ArgumentInputPort(Binding binding) {
        this.binding = binding;
    }

//    @Override
//    public void receive(int value) {
//        binding.receive(PNumber.valueOf(value));
//    }

    @Override
    public void receive(long time, double value) {
        binding.receive(time, PNumber.valueOf(value));
    }

    @Override
    public void receive(long time, Argument value) {
        binding.receive(time, value);
    }

    public static ArgumentInputPort create( Binding binding) {
        if (binding == null) {
            throw new NullPointerException();
        }
        return new ArgumentInputPort(binding);

    }

    public static interface Binding {

        public void receive(long time, Argument arg);
    }
}
