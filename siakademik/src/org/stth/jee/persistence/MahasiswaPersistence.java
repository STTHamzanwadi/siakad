package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
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
	public static List<Mahasiswa> getListByExample(Mahasiswa example){
		List<Criterion> lc = new ArrayList<>();
		if (example.getPembimbingAkademik()!=null){
			lc.add(Restrictions.eq("pembimbingAkademik", example.getPembimbingAkademik()));
		}
		if (example.getNama()!=null){
			lc.add(Restrictions.like("nama", example.getNama(), MatchMode.ANYWHERE));
		}
		if (example.getNpm()!=null){
			lc.add(Restrictions.like("npm", example.getNpm(), MatchMode.START));
		}
		if (example.getAngkatan()>0){
			lc.add(Restrictions.eq("angkatan", example.getAngkatan()));
		}
		if (example.getProdi()!=null){
			lc.add(Restrictions.eq("prodi", example.getProdi()));
		}
		List<Mahasiswa> rslt = GenericPersistence.findList(Mahasiswa.class,lc);
		return rslt;
	}

}
