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
 *
 */
package net.neilcsmith.praxis.video.gstreamer.components;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.neilcsmith.praxis.video.InvalidVideoResourceException;
import net.neilcsmith.praxis.video.gstreamer.GStreamerLibraryLoader;
import net.neilcsmith.praxis.video.gstreamer.delegates.DV1394Delegate;
import net.neilcsmith.praxis.video.gstreamer.delegates.IPCamDelegate;
import net.neilcsmith.praxis.video.gstreamer.delegates.KSDelegate;
import net.neilcsmith.praxis.video.gstreamer.delegates.PlaybinDelegate;
import net.neilcsmith.praxis.video.gstreamer.delegates.QTKitDelegate;
import net.neilcsmith.praxis.video.gstreamer.delegates.V4LDelegate;
import org.openide.util.Lookup;

/**
 *
 * @author Neil C Smith
 */
class GStreamerFactory {
    
    private final static Logger LOG = Logger.getLogger(GStreamerFactory.class.getName());

    private static GStreamerFactory instance = new GStreamerFactory();

    private GStreamerFactory() {
        preloadLibs();
    }

    private void preloadLibs() {
        try {
            for (GStreamerLibraryLoader loader : Lookup.getDefault().lookupAll(GStreamerLibraryLoader.class)) {
                
                try {
                    loader.load();
                } catch (Exception ex) {
                    LOG.log(Level.WARNING, "Exception thrown while trying to load GStreamer libraries with loader " + loader, ex);
                }
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Exception thrown while trying to load GStreamer library loaders", ex);
        }
    }
    
    public static GStreamerFactory getInstance() {
        return instance;
    }

    public VideoDelegate create(URI resource) throws InvalidVideoResourceException, InstantiationException {
        String scheme = resource.getScheme();
        if (scheme == null) {
            throw new InvalidVideoResourceException();
        }
        if (scheme.equals("file") || scheme.equals("http")) {
            return createPlaybinDelegate(resource);
        } else if (scheme.equals("v4l") || scheme.equals("v4l2")) {
            return createV4LDelegate(resource);
        } else if (scheme.equals("ks")) {
            return createKSDelegate(resource);
        } else if (scheme.equals("qtkit")) {
            return createQTKitDelegate(resource);        
        } else if (scheme.equals("ipcam")) {
            return createIPCamDelegate(resource);
        } else if (scheme.equals("dv1394")) {
            return createDV1394Delegate(resource);
        }
        throw new InvalidVideoResourceException();
    }

    private VideoDelegate createDV1394Delegate(URI resource) {
        return DV1394Delegate.create(resource);
    }

    private VideoDelegate createPlaybinDelegate(URI resource) {
        return PlaybinDelegate.create(resource);
    }

    private VideoDelegate createIPCamDelegate(URI resource) {
        if (resource.getScheme().equals("ipcam")) {
            try {
                resource = new URI("http", resource.getSchemeSpecificPart(), resource.getFragment());
            } catch (URISyntaxException ex) {
                Logger.getLogger(GStreamerFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return IPCamDelegate.create(resource);
    }
    
    private VideoDelegate createV4LDelegate(URI resource) {
        return V4LDelegate.create(resource);
    }
    
    
    private VideoDelegate createKSDelegate(URI resource) {
        return KSDelegate.create(resource);
    }
    
    private VideoDelegate createQTKitDelegate(URI resource) {
        return QTKitDelegate.create(resource);
    }
    
}
