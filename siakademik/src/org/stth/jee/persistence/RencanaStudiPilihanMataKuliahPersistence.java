package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;
import org.stth.siak.enumtype.Semester;

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
	
	public static List<RencanaStudiPilihanMataKuliah> getBySemesterTahunAjaran(Semester semester, String tahunAjaran){
		List<Criterion> lc = new ArrayList<>();
		String[] alias = {"rencanaStudi"};
		lc.add(Restrictions.eq("rencanaStudi.semester", semester));
		lc.add(Restrictions.eq("rencanaStudi.tahunAjaran", tahunAjaran));
		List<RencanaStudiPilihanMataKuliah> rslt = GenericPersistence.findList(RencanaStudiPilihanMataKuliah.class, lc,alias);
		if (rslt.size()>0){
			return rslt;
		}
		return null;
	}
}
