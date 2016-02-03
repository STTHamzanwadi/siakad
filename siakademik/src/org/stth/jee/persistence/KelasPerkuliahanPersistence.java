package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.enumtype.Semester;

public class KelasPerkuliahanPersistence {
	public static List<KelasPerkuliahan> getKelasPerkuliahanByDosen(DosenKaryawan d){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("dosenPengampu", d));
		List<KelasPerkuliahan> l = GenericPersistence.findList(KelasPerkuliahan.class, lc);
		return l;
	}
	
	public static List<KelasPerkuliahan> getKelasPerkuliahanMahasiswaSemester(Mahasiswa mhs, Semester sem, String ta){
		List<Criterion> lc = new ArrayList<>();
		List<KelasPerkuliahan> lkp = new ArrayList<>();
		String[] alias = {"kelasPerkuliahan"};
		lc.add(Restrictions.eq("mahasiswa", mhs));
		lc.add(Restrictions.eq("kelasPerkuliahan.semester", sem));
		lc.add(Restrictions.eq("kelasPerkuliahan.tahunAjaran", ta));
		List<PesertaKuliah> lpk = GenericPersistence.findList(PesertaKuliah.class, lc, alias); 
		if (lpk!=null) {
			for (PesertaKuliah pesertaKuliah : lpk) {
				lkp.add(pesertaKuliah.getKelasPerkuliahan());
			}
		}
		return lkp;
	}

}
