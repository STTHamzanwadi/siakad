package org.stth.siak.jee.ui.dosen;

import org.stth.siak.ui.util.AppMenuItems;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum DosenMenuItems implements AppMenuItems{
	 DASHBOARD("Profil ", DosenProfil.class, FontAwesome.HOME, true),
	 PERKULIAHAN("Riwayat Mengajar", DosenKelasDiampu.class, FontAwesome.UNIVERSITY, false),
	 MAHASISWA("Bimbingan Akademik", DosenStatusBimbinganAkademik.class, FontAwesome.USERS, true),
	 RENCANA_STUDI("Verifikasi Rencana Studi", DosenVerifikasiRencanaStudi.class, FontAwesome.USERS, true),
	 INFO_KURIKULUM("Struktur Kurikulum", DosenDetailKurikulum.class, FontAwesome.TABLE, true);
	 //,DOSEN("Misc", DosenProfil.class, FontAwesome.MALE, false);
	 
	 private final String viewName;
	 private final Class<? extends View> viewClass;
	 private final Resource icon;
	 private final boolean stateful;
	 
	 private DosenMenuItems(String viewName, Class<? extends View> viewClass,
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
