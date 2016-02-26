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

public class LogKehadiranPerkuliahanPersistence {
	public static List<LogKehadiranPesertaKuliah> getByLogPerkuliahan(LogPerkuliahan log){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("logPerkuliahan", log));
		List<LogKehadiranPesertaKuliah> l = GenericPersistence.findList(LogKehadiranPesertaKuliah.class, lc);
		return l;
	}
	
}
