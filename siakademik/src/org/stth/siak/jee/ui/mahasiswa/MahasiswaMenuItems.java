package org.stth.siak.jee.ui.mahasiswa;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum MahasiswaMenuItems {
	 DASHBOARD("Profil Anda", MahasiswaProfil.class, FontAwesome.USER, true),
	 PERKULIAHAN("Kelas Perkuliahan", KelasPerkuliahanView.class, FontAwesome.UNIVERSITY, false),
	 INDEKSPRESTASI("Indeks Prestasi", IPKView.class, FontAwesome.TABLE, true),
	 RENCANASTUDI("Rencana Studi", RencanaStudiView.class, FontAwesome.LIST, false);
	 
	 private final String viewName;
	 private final Class<? extends View> viewClass;
	 private final Resource icon;
	 private final boolean stateful;
	 
	 private MahasiswaMenuItems(String viewName, Class<? extends View> viewClass,
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
