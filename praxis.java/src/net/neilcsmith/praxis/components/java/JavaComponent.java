/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2012 Neil C Smith.
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
package net.neilcsmith.praxis.components.java;

import java.util.logging.Logger;
import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.core.CallArguments;
import net.neilcsmith.praxis.core.info.ArgumentInfo;
import net.neilcsmith.praxis.core.interfaces.TaskService;
import net.neilcsmith.praxis.core.types.PMap;
import net.neilcsmith.praxis.core.types.PReference;
import net.neilcsmith.praxis.core.types.PString;
import net.neilcsmith.praxis.impl.AbstractAsyncProperty;
import net.neilcsmith.praxis.java.CodeDelegate;
import net.neilcsmith.praxis.java.impl.AbstractJavaComponent;
import org.codehaus.janino.ClassBodyEvaluator;

/**
 *
 * @author Neil C Smith <http://neilcsmith.net>
 */
public class JavaComponent extends AbstractJavaComponent {
    
    private static final String[] IMPORTS = {
        "java.util.*",
        "net.neilcsmith.praxis.java.*",
        "static net.neilcsmith.praxis.java.Constants.*"
    };

    public JavaComponent() {
        setupCodeControl();
        buildParams("p", 16, 8);
        buildTriggers("t", 4, 4);
        buildOutputs("out-", 4);
    }

    private void setupCodeControl() {
        registerControl("code", new CodeProperty());
    }
    
    private class CodeProperty extends AbstractAsyncProperty<CodeDelegate> {

        private CodeProperty() {
            super(ArgumentInfo.create(
                    PString.class, PMap.create(PString.KEY_MIME_TYPE, "text/x-praxis-java")), 
                    CodeDelegate.class, PString.EMPTY);
        }

        @Override
        protected TaskService.Task createTask(CallArguments keys) throws Exception {
            Argument code;
            if (keys.getSize() < 1 || (code = keys.get(0)).isEmpty()) {
                return null;
            } else {
                return new CompilerTask(code.toString());
            }

        }

        @Override
        protected void valueChanged(long time) {
            setDelegate(getValue());
        }

        @Override
        protected void taskError(long time) {
            Logger.getLogger(getClass().getName()).warning("Error loading class");
        }
    }

    private class CompilerTask implements TaskService.Task {

        private final String code;

        private CompilerTask(String code) {
            this.code = code;
        }

        public Argument execute() throws Exception {
            ClassBodyEvaluator compiler = new ClassBodyEvaluator();
            compiler.setExtendedType(CodeDelegate.class);
            compiler.setDefaultImports(IMPORTS.clone());
            compiler.cook(code);
            CodeDelegate delegate = (CodeDelegate) compiler.getClazz().newInstance();
            return PReference.wrap(delegate);
        }
    }
    
}
