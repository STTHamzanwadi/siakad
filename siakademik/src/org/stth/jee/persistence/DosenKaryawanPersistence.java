package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.DosenKaryawan;

public class DosenKaryawanPersistence {
	public static List<DosenKaryawan> getDosen(){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("dosen", true));
		List<DosenKaryawan> l = GenericPersistence.findList(DosenKaryawan.class, lc);
		return l;
	}
	

}
