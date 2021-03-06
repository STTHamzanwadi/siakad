package org.stth.siak.jee.ui.mahasiswa;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum MahasiswaMenuItems {
	DASHBOARD("Profil Anda", MahasiswaProfil.class, FontAwesome.USER, true),
	RENCANASTUDI("Rencana Studi", RencanaStudiView.class, FontAwesome.LIST, false),
	KULIAH_SEMESTER("Kelas Semester Berjalan", MahasiswaKelasDiikuti.class, FontAwesome.UNIVERSITY, false),
	PERKULIAHAN("Riwayat Kuliah", KelasPerkuliahanView.class, FontAwesome.UNIVERSITY, false),
	INDEKSPRESTASI("Indeks Prestasi", IPKView.class, FontAwesome.TABLE, true),
	KURIKULUM("Struktur Kurikulum", MahasiswaDetailKurikulum.class, FontAwesome.TABLE, true);

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
