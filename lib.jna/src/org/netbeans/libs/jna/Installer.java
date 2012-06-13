package org.netbeans.libs.jna;

import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void validate() throws IllegalStateException {
        if (System.getProperty("jna.nosys") == null) {
            System.setProperty("jna.nosys", "true");
        }
    }
    
    

    @Override
    public void restored() {
        // no op
    }
}
