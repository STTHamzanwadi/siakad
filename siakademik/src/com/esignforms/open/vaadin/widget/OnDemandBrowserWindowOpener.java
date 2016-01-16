// Copyright (C) 2014 Yozons, Inc.
// Open eSignForms - Web-based electronic contracting software
//
// This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License
// as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
// without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
// See the GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License along with this program.  
// If not, see <http://open.esignforms.com/agpl.txt> or <http://www.gnu.org/licenses/>.
// Contact information is via the public forums at http://open.esignforms.com or via private email to open-esign@yozons.com.
//
package com.esignforms.open.vaadin.widget;

import java.io.IOException;
import java.util.HashMap;

import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.shared.ui.BrowserWindowOpenerState;
import com.vaadin.ui.Button;


/**
 * OnDemandBrowserWindowOpener is based OnDemandFileDownloader.
 *
 * @author Yozons Inc.
 */
public class OnDemandBrowserWindowOpener extends BrowserWindowOpener {
    private static final long serialVersionUID = -8352749340084392927L;

    /**
     * Provide the {@link StreamSource} in an on-demand way. Keeping in case we need to add methods like getFileName() for file downloader.
     */
    public interface OnDemandStreamSource extends StreamSource {
    }
    
    public static class OnDemandStreamResource extends StreamResource {
        private static final long serialVersionUID = 4582308475950400910L;

        protected HashMap<String,String> parameterMap;
        
        public OnDemandStreamResource(OnDemandStreamSource streamSource, String filename) {
            super(streamSource,filename);
        }
        
        public DownloadStream getStream() {
            DownloadStream ds = super.getStream();
            
            if ( parameterMap != null ) {
                for( String key : parameterMap.keySet() ) {
                    ds.setParameter(key, parameterMap.get(key));
                }
            }
            
            // If the cache time is still the default, let's reduce for our dynamic content that shouldn't be cached for a day.
            if ( ds.getCacheTime() == DownloadStream.DEFAULT_CACHETIME ) {
                ds.setCacheTime(10L * 60L * 1000L); // 10 minutes in msecs
            }
            
            return ds;
        }
        
        public void setParameterMap(HashMap<String,String> parameterMap) {
            this.parameterMap = parameterMap;
        }
    }

    protected final OnDemandStreamSource onDemandStreamSource;
    protected HashMap<String,String> parameterMap;
    protected Button buttonToEnableWhenDone;
    protected String contentType;
    protected Integer cacheTime;

    public OnDemandBrowserWindowOpener(String filename, OnDemandStreamSource onDemandStreamSource) {
        super(new OnDemandStreamResource(onDemandStreamSource,filename));
        this.onDemandStreamSource = onDemandStreamSource;
    }

    public OnDemandBrowserWindowOpener(Button buttonToEnableWhenDone, String filename, OnDemandStreamSource onDemandStreamSource) {
        this(filename,onDemandStreamSource);
        this.buttonToEnableWhenDone = buttonToEnableWhenDone;
    }

    @Override
    public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path) throws IOException {
        try {
            OnDemandStreamResource sr = getResource();
            if ( contentType != null ) {
                sr.setMIMEType(contentType);
            }
            if ( cacheTime != null ) {
                sr.setCacheTime(cacheTime);
            }
            if ( parameterMap != null ) {
                sr.setParameterMap(parameterMap);
            }
            return super.handleConnectorRequest(request, response, path);
        } finally {
            if ( buttonToEnableWhenDone != null ) {
                buttonToEnableWhenDone.setEnabled(true);
            }
        }
    }

    public OnDemandStreamResource getResource() {
        return (OnDemandStreamResource)this.getResource(BrowserWindowOpenerState.locationResource);
    }
    
    public void setContentType(String v) {
        contentType = v;
    }
    
    public void setCacheTime(int msecs) {
        cacheTime = msecs;
    }
    
    public void setStreamSourceParameter(String name, String value) {
        if ( parameterMap == null )
            parameterMap = new HashMap<String,String>();
        parameterMap.put(name, value);
    }
}