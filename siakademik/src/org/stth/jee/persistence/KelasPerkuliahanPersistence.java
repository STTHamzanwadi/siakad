package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;

public class KelasPerkuliahanPersistence {
	public static List<KelasPerkuliahan> getKelasPerkuliahanByDosen(DosenKaryawan d){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("dosenPengampu", d));
		List<KelasPerkuliahan> l = GenericPersistence.findList(KelasPerkuliahan.class, lc);
		return l;
	}

}
