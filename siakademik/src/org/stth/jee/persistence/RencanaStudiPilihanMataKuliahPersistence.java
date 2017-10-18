package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.enumtype.StatusRencanaStudi;

public class RencanaStudiPilihanMataKuliahPersistence {
	
	
	
	public static List<RencanaStudiPilihanMataKuliah> getByRencanaStudi(RencanaStudiMahasiswa rsm){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("rencanaStudi", rsm));
		List<RencanaStudiPilihanMataKuliah> rslt = GenericPersistence.findList(RencanaStudiPilihanMataKuliah.class, lc);
		if (rslt.size()>0){
			return rslt;
		}
		return null;
	}
	
	public static List<RencanaStudiPilihanMataKuliah> getValidBySemesterTahunAjaran(Semester semester, String tahunAjaran, MataKuliah mk){
		List<Criterion> lc = new ArrayList<>();
		String[] alias = {"rencanaStudi"};
		if (mk!=null) {
			lc.add(Restrictions.eq("mataKuliah", mk));
		}
		lc.add(Restrictions.eq("rencanaStudi.semester", semester));
		lc.add(Restrictions.eq("rencanaStudi.tahunAjaran", tahunAjaran));
		lc.add(Restrictions.eq("rencanaStudi.status", StatusRencanaStudi.DISETUJUI));
		List<RencanaStudiPilihanMataKuliah> rslt = GenericPersistence.findList(RencanaStudiPilihanMataKuliah.class, lc,alias);
		if (rslt.size()>0){
			return rslt;
		}
		return null;
	}
}
