package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.enumtype.Semester;

public class RencanaStudiPersistence {
	
	public static RencanaStudiMahasiswa getByMhsSemTa(Mahasiswa mhs, Semester sem, String tahunAjaran){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("mahasiswa", mhs));
		lc.add(Restrictions.eq("semester", sem));
		lc.add(Restrictions.eq("tahunAjaran", tahunAjaran));
		List<RencanaStudiMahasiswa> rslt = GenericPersistence.findList(RencanaStudiMahasiswa.class, lc);
		if (rslt.size()>0){
			RencanaStudiMahasiswa rsm = rslt.get(0);
			return rsm;
		}
		return null;
	}
}
