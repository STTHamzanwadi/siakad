package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogPerkuliahan;

public class LogPerkuliahanPersistence {
	public static List<LogPerkuliahan> getByDosenOnPeriod(DosenKaryawan dosen, Date start, Date end){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("diisiOleh", dosen));
		lc.add(Restrictions.between("tanggalPertemuan", start, end));
		List<LogPerkuliahan> l = GenericPersistence.findList(LogPerkuliahan.class, lc);
		return l;
	}
	public static List<LogPerkuliahan> getByKelasOnPeriod(KelasPerkuliahan kp, Date start, Date end){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("kelasPerkuliahan", kp));
		lc.add(Restrictions.between("tanggalPertemuan", start, end));
		List<LogPerkuliahan> l = GenericPersistence.findList(LogPerkuliahan.class, lc);
		return l;
	}
	public static List<LogPerkuliahan> getLogOnPeriod(Date start, Date end){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.between("tanggalPertemuan", start, end));
		List<LogPerkuliahan> l = GenericPersistence.findList(LogPerkuliahan.class, lc);
		return l;
	}
}
