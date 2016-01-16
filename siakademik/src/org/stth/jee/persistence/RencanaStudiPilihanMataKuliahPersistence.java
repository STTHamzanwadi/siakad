package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;

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
}
