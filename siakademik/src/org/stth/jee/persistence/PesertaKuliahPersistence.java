package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.PesertaKuliah;

public class PesertaKuliahPersistence {
	public static List<PesertaKuliah> getPesertaKuliahByKelasPerkuliahan(KelasPerkuliahan kp){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("kelasPerkuliahan", kp));
		List<PesertaKuliah> l = GenericPersistence.findList(PesertaKuliah.class, lc);
		return l;
	}
	public static PesertaKuliah getPesertaKuliahByKelasPerkuliahanMahasiswa(KelasPerkuliahan kp, Mahasiswa mhs){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("kelasPerkuliahan", kp));
		lc.add(Restrictions.eq("mahasiswa", mhs));
		List<PesertaKuliah> l = GenericPersistence.findList(PesertaKuliah.class, lc);
		if (l.size()>0) return l.get(0);
		return null;
	}

}
