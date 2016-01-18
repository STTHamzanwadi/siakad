package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.ProgramStudi;

public class KurikulumPersistence {

	public static List<Kurikulum> getListByProdi(ProgramStudi prodi){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("prodi", prodi));
		List<Kurikulum> rslt = GenericPersistence.findList(Kurikulum.class,lc);
		return rslt;
	}

}
