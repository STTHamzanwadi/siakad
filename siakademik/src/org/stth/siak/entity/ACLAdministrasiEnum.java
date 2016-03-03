package org.stth.siak.entity;

import java.util.List;

public enum ACLAdministrasiEnum {
	LOG_PERKULIAHAN,LOG_PERKULIAHAN_EDIT,LOG_PERKULIAHAN_DELETE,
	ABSENSI_KULIAH_PRINT,
	KRS_PRINT,
	TRANSKRIP_PRINT,
	ADD_MAHASISWA,EDIT_MAHASISWA,ADD_KELASPERKULIAHAN,DELETE_KELASPERKULIAHAN,ADD_NILAIPERKULIAHAN,TOLAK_KRS;
	
	public static boolean isEligibleTo(List<UserAccessRightsAdministrasi> lacl, ACLAdministrasiEnum acl){
		boolean b=false;
		for (UserAccessRightsAdministrasi userAccessRightsAdministrasi : lacl) {
			ACLAdministrasiEnum aclu = userAccessRightsAdministrasi.getAcl();
			if (aclu.equals(acl)){
				b = true;
				break;
			}
		}
		return b;
	}
}
