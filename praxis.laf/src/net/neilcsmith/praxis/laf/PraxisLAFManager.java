/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009 Neil C Smith. All rights reserved.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details.
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please visit http://neilcsmith.net if you need additional information or
 * have any questions.
 *
 */

package net.neilcsmith.praxis.laf;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Neil C Smith
 */
public class PraxisLAFManager {

    private static PraxisLAFManager instance = new PraxisLAFManager();

    private PraxisLAFManager() {}

    public void installUI() {
        if (EventQueue.isDispatchThread()) {
            doInstall();
        } else {
            try {
                EventQueue.invokeAndWait(new Runnable() {

                    public void run() {
                        doInstall();
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void doInstall() {
        boolean useSystemTheme = false;
        try {
            useSystemTheme = Boolean.parseBoolean(System.getProperty("praxis.useSystemTheme", "false"));
        } catch (Exception ex) {
            useSystemTheme = false;
        }
        if (useSystemTheme) {
            return;
        }
        try {
//            NimRODLookAndFeel laf = new NimRODLookAndFeel();
//            laf.setCurrentTheme(new PraxisTheme
            LookAndFeel laf = new PraxisLookAndFeel();
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(PraxisLAFManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static PraxisLAFManager getInstance() {
        return instance;
    }

}
