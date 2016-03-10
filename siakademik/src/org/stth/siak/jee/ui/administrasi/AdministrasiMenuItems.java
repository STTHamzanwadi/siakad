package org.stth.siak.jee.ui.administrasi;

import org.stth.siak.jee.ui.generalview.ProfilUmumDosenKaryawan;
import org.stth.siak.ui.util.AppMenuItems;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum AdministrasiMenuItems implements AppMenuItems{
	 DASHBOARD("Profil ", ProfilUmumDosenKaryawan.class, FontAwesome.HOME, true);
	 //,DOSEN("Misc", DosenProfil.class, FontAwesome.MALE, false);
	 
	 private final String viewName;
	 private final Class<? extends View> viewClass;
	 private final Resource icon;
	 private final boolean stateful;
	 
	 private AdministrasiMenuItems(String viewName, Class<? extends View> viewClass,
	            Resource icon, boolean stateful) {
	        this.viewName = viewName;
	        this.viewClass = viewClass;
	        this.icon = icon;
	        this.stateful = stateful;
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
	 


}
