package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogKehadiranPesertaKuliah;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.PesertaKuliah;

public class LogKehadiranPerkuliahanPersistence {
	public static List<LogKehadiranPesertaKuliah> getByLogPerkuliahan(LogPerkuliahan log){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("logPerkuliahan", log));
		List<LogKehadiranPesertaKuliah> l = GenericPersistence.findList(LogKehadiranPesertaKuliah.class, lc);
		return l;
	}
	public static List<LogKehadiranPesertaKuliah> getByPesertaKuliah(PesertaKuliah p){
		List<Criterion> lc = new ArrayList<>();
		String[] alias = {"logPerkuliahan"};
		lc.add(Restrictions.eq("logPerkuliahan.kelasPerkuliahan", p.getKelasPerkuliahan()));
		lc.add(Restrictions.eq("mahasiswa", p.getMahasiswa()));
		lc.add(Restrictions.eq("isHadir", true));
		List<LogKehadiranPesertaKuliah> l = GenericPersistence.findList(LogKehadiranPesertaKuliah.class, lc, alias);
		return l;
	}
}
