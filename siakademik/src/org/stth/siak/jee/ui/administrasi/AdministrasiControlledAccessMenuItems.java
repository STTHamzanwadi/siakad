package org.stth.siak.jee.ui.administrasi;

import org.stth.siak.entity.ACLAdministrasiEnum;
import org.stth.siak.ui.util.AppMenuItems;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum AdministrasiControlledAccessMenuItems implements AppMenuItems{
	 
	 LOG_PERKULIAHAN("Log Perkuliahan", DaftarLogPerkuliahan.class, FontAwesome.TABLE, false, ACLAdministrasiEnum.LOG_PERKULIAHAN),
	 CETAK_ABSENSI("Cetak Absensi", CetakAbsensi.class, FontAwesome.TABLE, false, ACLAdministrasiEnum.LOG_PERKULIAHAN);
	 
	 
	 private final String viewName;
	 private final Class<? extends View> viewClass;
	 private final Resource icon;
	 private final boolean stateful;
	 private final ACLAdministrasiEnum acl;
	 
	 private AdministrasiControlledAccessMenuItems(String viewName, Class<? extends View> viewClass,
	            Resource icon, boolean stateful, ACLAdministrasiEnum acl) {
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

	public ACLAdministrasiEnum getAccessControlList(){
		return acl;
	}

}