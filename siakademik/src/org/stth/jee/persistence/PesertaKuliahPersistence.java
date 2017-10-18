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
		return getPesertaKuliahByKelasPerkuliahanMhs(kp, null);
	}
	public static List<PesertaKuliah> getPesertaKuliahByMahasiswa(Mahasiswa mhs){
		return getPesertaKuliahByKelasPerkuliahanMhs(null, mhs);
	}
	public static List<PesertaKuliah> getPesertaKuliahByKelasPerkuliahanMhs(KelasPerkuliahan kp, Mahasiswa mhs){
		List<Criterion> lc = new ArrayList<>();
		if (kp!=null) {
			lc.add(Restrictions.eq("kelasPerkuliahan", kp));
		}
		if (mhs!=null) {
			lc.add(Restrictions.eq("mahasiswa", mhs));
		}
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
	public static List<PesertaKuliah> getPesertaKuliahByExample(PesertaKuliah pk){
		List<Criterion> lc =new ArrayList<>();
		String[] map = {"kelasPerkuliahan"}; 
		if(pk.getMahasiswa()!=null){
			lc.add(Restrictions.eq("mahasiswa", pk.getMahasiswa()));
		}
		if (!pk.getCopiedKodeMatkul().isEmpty()) {
			lc.add(Restrictions.eq("copiedKodeMatkul", pk.getCopiedKodeMatkul()));
		}
		if (pk.getKelasPerkuliahan()!=null) {
			if (pk.getKelasPerkuliahan().getDosenPengampu() != null) {
				lc.add(Restrictions.eq("kelasPerkuliahan.dosenPengampu", pk.getKelasPerkuliahan().getDosenPengampu()));
			}
			if (pk.getKelasPerkuliahan().getSemester() != null) {
				lc.add(Restrictions.eq("kelasPerkuliahan.semester", pk.getKelasPerkuliahan().getSemester()));
			}
			if (pk.getKelasPerkuliahan().getProdi() != null) {
				lc.add(Restrictions.eq("kelasPerkuliahan.prodi", pk.getKelasPerkuliahan().getProdi()));
			}
			if (!pk.getKelasPerkuliahan().getTahunAjaran().isEmpty()) {
				lc.add(Restrictions.eq("kelasPerkuliahan.tahunAjaran", pk.getKelasPerkuliahan().getTahunAjaran()));
			} 
		}
		List<PesertaKuliah> l = GenericPersistence.findList(PesertaKuliah.class, lc, map);
		return l;
	}

}
