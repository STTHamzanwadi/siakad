package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.Calendar;
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
	public static List<LogPerkuliahan> getLogSimilarOnDate(LogPerkuliahan log){
		List<Criterion> lc = new ArrayList<>();
		Date date = log.getTanggalPertemuan();
		Calendar cal = Calendar.getInstance(); // locale-specific
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date minDate =  cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		Date maxDate = cal.getTime();
		lc.add(Restrictions.eq("kelasPerkuliahan", log.getKelasPerkuliahan()));
		lc.add(Restrictions.ge("tanggalPertemuan", minDate));
		lc.add(Restrictions.le("tanggalPertemuan", maxDate));
		List<LogPerkuliahan> l = GenericPersistence.findList(LogPerkuliahan.class, lc);
		return l;
	}
}
