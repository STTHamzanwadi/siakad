package org.pdtyph.jee.ui.oprlembaga;



import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum OprLembagaMenuItems {
	DASHBOARD("Dashboard ", Dashboard.class, FontAwesome.DASHBOARD, true),
	INSTANSINVIEW("Instansi", DaftarInstansi.class, FontAwesome.TASKS, false),
	PEGAWAIVIEW("Pegawai", DaftarPegawai.class, FontAwesome.TROPHY, false),
	JABATANVIEW("Jabatan", DaftarJabatan.class, FontAwesome.TASKS, false),
	PRODIVIEW("Prodi", DaftarProdi.class, FontAwesome.STACK_OVERFLOW, false),
	
	RIWAYATJABATAN("Riwayat Jabatan", DaftarRiwayatJabatan.class, FontAwesome.ARCHIVE, false),
	RIWAYATPENDIDIKAN("Riwayat Pendidikan", DaftarRiwayatPendidikan.class, FontAwesome.STACK_OVERFLOW, false),
	UPLOADPHOTO("Upload Photo Pegawai", DaftarGambar.class, FontAwesome.PHOTO, false),
	CARIDATA("Pencarian", DaftarPencarian.class, FontAwesome.SEARCH, false);
	
	
	private final String viewName;
	private final Class<? extends View> viewClass;
	private final Resource icon;
	private final boolean stateful;

	private OprLembagaMenuItems(String viewName, Class<? extends View> viewClass,
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
