package org.stth.siak.jee.ui.administrasi;

import org.stth.siak.entity.ACLAdministrasiEnum;
import org.stth.siak.ui.util.AppMenuItems;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum AdministrasiControlledAccessMenuItems implements AppMenuItems{
	 
	 LOG_PERKULIAHAN("Log Perkuliahan", AdministrasiDaftarLogPerkuliahan.class, FontAwesome.TABLE, false, ACLAdministrasiEnum.LOG_PERKULIAHAN),
	 REKAP_HADIR_DOSEN("Rekapitulasi Kehadiran Dosen", AdministrasiRekapKehadiranDosen.class, FontAwesome.TABLE, false, ACLAdministrasiEnum.LOG_PERKULIAHAN),
	 JADWAL_PERKULIAHAN("Jadwal Perkuliahan", AdministrasiDaftarJadwalKuliah.class, FontAwesome.TABLE, false, ACLAdministrasiEnum.JADWAL_KULIAH),
	 CETAK_ABSENSI("Cetak Absensi", AdministrasiCetakAbsensi.class, FontAwesome.PRINT, false, ACLAdministrasiEnum.LOG_PERKULIAHAN),
	 CETAK_TRANSKRIP("Cetak Transkrip", AdministrasiCetakTranskripMahasiswa.class, FontAwesome.PRINT, false, ACLAdministrasiEnum.TRANSKRIP_PRINT),
	 DATA_MAHASISWA("Administrasi Data Mahasiswa", AdministrasiDataMahasiswa.class, FontAwesome.USERS, false, ACLAdministrasiEnum.EDIT_MAHASISWA),
	 DATA_DOSEN("Administrasi Data Dosen", AdministrasiDataDosen.class, FontAwesome.USERS, false, ACLAdministrasiEnum.EDIT_MAHASISWA);
	 
	 
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
