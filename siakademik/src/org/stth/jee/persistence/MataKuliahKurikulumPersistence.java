package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.MataKuliahKurikulum;

public class MataKuliahKurikulumPersistence {
	
	public static List<MataKuliahKurikulum> getByKurikulum(Kurikulum kr){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("kurikulum", kr));
		List<MataKuliahKurikulum> rslt = GenericPersistence.findList(MataKuliahKurikulum.class,lc);
		return rslt;
	}
	

}
