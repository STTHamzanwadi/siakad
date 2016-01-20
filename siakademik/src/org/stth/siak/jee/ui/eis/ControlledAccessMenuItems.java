package org.stth.siak.jee.ui.eis;

import org.stth.siak.ui.util.AppMenuItems;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum ControlledAccessMenuItems implements AppMenuItems{
	 MHS_STAT("StatMahasiswa ", CariMahasiswa.class, FontAwesome.USERS, false, AccessControlList.MHS_STAT),
	 MHS_CARI("PencarianMahasiswa ", CariMahasiswa.class, FontAwesome.USERS, false, AccessControlList.MHS_CARI),
	 DOSEN_STAT("Statistik Dosen ", StatistikMahasiswa.class, FontAwesome.USERS, false, AccessControlList.DSN_STAT),
	 DOSEN_CARI("Pencarian Dosen ", StatistikMahasiswa.class, FontAwesome.USERS, false, AccessControlList.DSN_CARI);
	 
	 
	 private final String viewName;
	 private final Class<? extends View> viewClass;
	 private final Resource icon;
	 private final boolean stateful;
	 private final AccessControlList acl;
	 
	 private ControlledAccessMenuItems(String viewName, Class<? extends View> viewClass,
	            Resource icon, boolean stateful, AccessControlList acl) {
	        this.viewName = viewName;
	        this.viewClass = viewClass;
	        this.icon = icon;
	        this.stateful = stateful;
	        this.acl = acl;
	    }

	public String getViewName() {
		return viewName;
	}

	public Class<? extends View> getViewClass() {
		return viewClass;
	}

	public Resource getIcon() {
		return icon;
	}

	public boolean isStateful() {
		return stateful;
	}

	public AccessControlList getAccessControlList(){
		return acl;
	}

}
