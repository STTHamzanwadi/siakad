package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;

public class MahasiswaPersistence {
	public static List<Mahasiswa> getListByPembimbingAkademik(DosenKaryawan d){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("pembimbingAkademik", d));
		List<Mahasiswa> rslt = GenericPersistence.findList(Mahasiswa.class,lc);
		return rslt;
	}

}
