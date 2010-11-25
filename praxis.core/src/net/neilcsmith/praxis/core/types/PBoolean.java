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

package net.neilcsmith.praxis.core.types;

import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.core.ArgumentFormatException;
import net.neilcsmith.praxis.core.info.ArgumentInfo;

/**
 *
 * @author Neil C Smith
 */
public final class PBoolean extends Argument {
    
    public static final PBoolean TRUE = new PBoolean(true);
    public static final PBoolean FALSE = new PBoolean(false);
    
    private boolean value;
    private String str;
    
    private PBoolean(boolean value) {
        this.value = value;
    }
    
    public boolean value() {
        return value;
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }

    @Override
    public int hashCode() {
        return value ? 1231 : 1237;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PBoolean) {
            return value == ((PBoolean) obj).value;
        }
        return false;
    }
    
    public static PBoolean valueOf(boolean value) {
        return value ? TRUE : FALSE;
    }
    
    public static PBoolean valueOf(String str) throws ArgumentFormatException {
        if (str.equals("true")) {
            return TRUE;
        } else if (str.equals("false")) {
            return FALSE;
        } else {
            throw new ArgumentFormatException();
        }
    }
    
//    public static boolean booleanValueOf(Argument arg) throws ArgumentFormatException {
//        if (arg instanceof PBoolean) {
//            return ((PBoolean) arg).value();
//        } else {
//            if (arg.toString().equalsIgnoreCase("true")) {
//                return true;
//            } else if (arg.toString().equalsIgnoreCase("false")) {
//                return false;
//            } else {
//                throw new ArgumentFormatException();
//            }
//        }
//    }
    
    public static PBoolean coerce(Argument arg) throws ArgumentFormatException {
        if (arg instanceof PBoolean) {
            return (PBoolean) arg;
        } else {
            return valueOf(arg.toString());
        }
    }
    
    public static ArgumentInfo info() {
        return ArgumentInfo.create(PBoolean.class, null);
    }

}
