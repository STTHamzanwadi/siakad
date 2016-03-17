package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.JadwalKuliah;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;

public class JadwalKuliahPersistence {
	public static List<JadwalKuliah> getJadwalByDosenProdi(DosenKaryawan dosen, ProgramStudi prodi, Semester semester, String tahunAjaran){
		List<Criterion> lc = new ArrayList<>();
		String[] alias = new String[] {"kelasPerkuliahan"};
		if (dosen!=null){
			lc.add(Restrictions.eq("kelasPerkuliahan.dosenPengampu", dosen));
		}
		if (prodi!=null){
			lc.add(Restrictions.eq("kelasPerkuliahan.prodi", prodi));
		}
		lc.add(Restrictions.eq("kelasPerkuliahan.semester", semester));
		lc.add(Restrictions.eq("kelasPerkuliahan.tahunAjaran", tahunAjaran));
		return GenericPersistence.findList(JadwalKuliah.class, lc, alias);
		
	}
	public static List<JadwalKuliah> getJadwalByKelasPerkuliahan(KelasPerkuliahan kp){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("kelasPerkuliahan", kp));
		return GenericPersistence.findList(JadwalKuliah.class,lc);
		
	}

	public static List<JadwalKuliah> getLogSimilarOnDate(JadwalKuliah log) {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}
}
