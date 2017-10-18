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
	 DATA_MAHASISWA("Administrasi Data Mahasiswa", AdministrasiCetakTranskripMahasiswa.class, FontAwesome.USER, false, ACLAdministrasiEnum.TRANSKRIP_PRINT),
	 //DATA_MAHASISWA("Administrasi Data Mahasiswa", AdministrasiDataMahasiswa.class, FontAwesome.USERS, false, ACLAdministrasiEnum.EDIT_MAHASISWA),
	 DATA_DOSEN("Administrasi Data Dosen", AdministrasiDataDosen.class, FontAwesome.USERS, false, ACLAdministrasiEnum.EDIT_DOSEN),
	 PRODI("Program Studi", AdministrasiProgramStudi.class, FontAwesome.UNIVERSITY, false, ACLAdministrasiEnum.EDIT_DOSEN),
	 DATA_ALUMNI("Administrasi Data Alumni", AdministrasiAlumniMahasiswa.class, FontAwesome.UNIVERSITY, false, ACLAdministrasiEnum.EDIT_MAHASISWA),
	 KELAS_PERKULIAHAN("Kelas Perkuliahan", AdministrasiKelasPerkuliahan.class, FontAwesome.UNIVERSITY, false, ACLAdministrasiEnum.ADD_KELASPERKULIAHAN),
	 MAHASISWA_AKTIF("Daftar Mahasiswa Aktif", AdministrasiKartudanHasilStudi.class, FontAwesome.PRINT, false, ACLAdministrasiEnum.EDIT_MAHASISWA),
	 RENCANA_STUDI("Daftar Rencana Studi", AdministrasiDaftarRencanaStudi.class, FontAwesome.TABLE, false, ACLAdministrasiEnum.TOLAK_KRS),
	 MATA_KULIAH("Administrasi MataKuliah", AdministrasiMataKuliah.class, FontAwesome.TABLE, false, ACLAdministrasiEnum.MATAKULIAH),
	 KURIKULUM("Administrasi Kurikulum", AdministrasiKurikulum.class, FontAwesome.TABLE, false, ACLAdministrasiEnum.MATAKULIAH),
	 KONFIGURASI("Konfigurasi", KonfigurasiAkademik.class, FontAwesome.TICKET, false, ACLAdministrasiEnum.KONFIGURASI),
	 MATAKULIAH_RENCANASTUDI("Mata Kuliah Rencana Studi", AdministrasiMataKuliahRencanaStudi.class, FontAwesome.TABLE, false, ACLAdministrasiEnum.TOLAK_KRS);
	
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

	@Override
	public String getViewName() {
		return viewName;
	}

	@Override
	public Class<? extends View> getViewClass() {
		return viewClass;
	}

	@Override
	public Resource getIcon() {
		return icon;
	}

	@Override
	public boolean isStateful() {
		return stateful;
	}

	public ACLAdministrasiEnum getAccessControlList(){
		return acl;
	}

}
