package org.yph.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.Jabatan;
import org.pdtyph.entity.Pegawai;
import org.pdtyph.entity.Prodi;
import org.pdtyph.entity.RiwayatJabatan;
import org.pdtyph.entity.RiwayatPendidikan;
import org.pdtyph.entity.UserOprInstansi;

import com.vaadin.server.VaadinSession;

public class PegawaiPersistence extends GenericPersistence{

	public static List<Pegawai> cariPegawai(Pegawai m){
		List<Criterion>lc=new ArrayList<>();
		if (m.getNamaPegawai()!=null){
			lc.add(Restrictions.like("namaPegawai", m.getNamaPegawai(), MatchMode.ANYWHERE));
		}
		if  (m.getInstansi()!=null){
			lc.add(Restrictions.eq("instansi", m.getInstansi()));
		}
		if (m.getJenjangPendTerakhir()!=null){
			lc.add(Restrictions.eq("jenjangPendTerakhir", m.getJenjangPendTerakhir()));
		}
		if (m.getKelamin()!=null){
			lc.add(Restrictions.eq("kelamin", m.getKelamin()));
		}
		if (m.getNamaKepegawaian()!=null) {
			lc.add(Restrictions.like("namaKepegawaian", m.getNamaKepegawaian(), MatchMode.ANYWHERE));
		}
		if (m.getStatusKepegawaian()!=null) {
			lc.add(Restrictions.like("statusKepegawaian", m.getStatusKepegawaian(), MatchMode.ANYWHERE));
		}
		List<Pegawai> dp=GenericPersistence.findList(Pegawai.class, lc);
		return dp;
	}
	
	public static List<Jabatan> cariJabatan(Jabatan j){
		List<Criterion>jb=new ArrayList<>();
		if(j.getNamaJabatan()!=null){
			jb.add(Restrictions.like("namaJabatan", j.getNamaJabatan(), MatchMode.ANYWHERE));
		}
		if (j.getInstansi()!=null) {
			jb.add(Restrictions.eq("instansi", j.getInstansi()));
		}
		if (j.getJnsJabatan()!=null) {
			jb.add(Restrictions.eq("jnsJabatan", j.getJnsJabatan()));
		}
		List<Jabatan>je=GenericPersistence.findList(Jabatan.class,jb);
		return je;
	}
	public static List<Instansi> cariInstansi(Instansi i){
		List<Criterion> in=new ArrayList<>();
		if (i.getNama()!=null) {
			in.add(Restrictions.like("nama", i.getNama(), MatchMode.ANYWHERE));
		}
		if(i.getJenisLembaga()!=null){
			in.add(Restrictions.like("jenisLembaga", i.getJenisLembaga(),MatchMode.ANYWHERE));
		}
		if (i.getPimpinan()!=null) {
			in.add(Restrictions.like("pimpinan", i.getPimpinan(),MatchMode.ANYWHERE));
		}
		if (i.getStsAkreditasi()!=null) {
			in.add(Restrictions.like("stsAkreditasi", i.getStsAkreditasi(),MatchMode.ANYWHERE));
		}
		List<Instansi>ins=GenericPersistence.findList(Instansi.class, in);
		return ins;
	}
	
	public static List<RiwayatJabatan> cariRiwayatJabatan(RiwayatJabatan rj){
		List<Criterion> rjb=new ArrayList<>();
		if (rj.getNmPegawai()!=null) {
			rjb.add(Restrictions.like("nmPegawai", rj.getNmPegawai()));
		}
		if (rj.getNmJabatan()!=null){
			rjb.add(Restrictions.eq("nmJabatan", rj.getNmJabatan()));
		}
		
		List<RiwayatJabatan>r=GenericPersistence.findList(RiwayatJabatan.class,rjb);
		return r;

	}
	
	public static List<RiwayatPendidikan> cariRiwayatPendidikan(RiwayatPendidikan rp){
		List<Criterion> rjp=new ArrayList<>();
		if(rp.getNmaPegawai()!=null){
			rjp.add(Restrictions.like("nmaPegawai", rp.getNmaPegawai()));
		}
		if (rp.getJenjangPendidikan()!=null) {
			rjp.add(Restrictions.like("jenjangPendidikan", rp.getJenjangPendidikan()));
		}

		List<RiwayatPendidikan>r=GenericPersistence.findList(RiwayatPendidikan.class,rjp);
		return r;

	}
	
	public static List<Prodi> cariProdi(Prodi p){
		List<Criterion>pr=new ArrayList<>();
		if (p.getNamaProdi()!=null) {
			pr.add(Restrictions.like("namaProdi", p.getNamaProdi(),MatchMode.ANYWHERE));
		}
		if(p.getStsAkreditasi()!=null){
			pr.add(Restrictions.like("stsAkreditasi", p.getStsAkreditasi(), MatchMode.ANYWHERE));
		}
		if (p.getInstansi()!=null) {
			pr.add(Restrictions.eq("instansi", p.getInstansi()));
		}
		List<Prodi>pe=GenericPersistence.findList(Prodi.class,pr);
		return pe;
	}

	public static  List<Pegawai> getByLembaga(Instansi i){
		List<Criterion> l=new ArrayList<>();
		l.add(Restrictions.eq("instansi", i));
		return findList(Pegawai.class, l);
	}
	public static List<Jabatan> getJabatanByLembaga(Instansi i){
		List<Criterion>l=new ArrayList<>();
		l.add(Restrictions.eq("instansi", i));
		return findList(Jabatan.class, l);
	}
	public static List<Prodi> getProdiByLembaga(Instansi i){
		List<Criterion>l=new ArrayList<>();
		l.add(Restrictions.eq("instansi", i));
		return findList(Prodi.class, l);
	}
	public static List<Instansi> getInstansi(Instansi i){
		List<Criterion> l=new ArrayList<>();
		l.add(Restrictions.eq("instansi",i));
		return findList(Instansi.class,l);
	}

	public static List<RiwayatJabatan> getRiwayatJabatanByLembaga(Instansi i){
		List<Criterion>l=new ArrayList<>();
		l.add(Restrictions.eq("nmInstansi", i));
		return findList(RiwayatJabatan.class, l);
	}
	public static List<RiwayatPendidikan> getRiwayatPendidikanByLembaga(Instansi i){
		List<Criterion>l=new ArrayList<>();
		l.add(Restrictions.eq("nmInstansi", i));
		return findList(RiwayatPendidikan.class, l);
	}

	public static List<Pegawai> getPegawaiByDosen(Pegawai p){		
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.like("namaKepegawaian", "Dosen",MatchMode.ANYWHERE));
		return findList(Pegawai.class, lc);
	}
	public static List<Pegawai> getPegawaiByGuru(Pegawai p){		
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.like("namaKepegawaian", "Guru",MatchMode.ANYWHERE));
		return findList(Pegawai.class, lc);
	}
	public static List<Pegawai> getPegawaiByKaryawan(Pegawai p){		
		List<Criterion> lc = new ArrayList<>();

		if (p.getNamaKepegawaian()=="Karyawan") {
			lc.add(Restrictions.eq("namaKepegawaian", p.getNamaKepegawaian()));
			//
			//			if (p.getNamaPegawai()!=null){
			//				lc.add(Restrictions.like("namaPegawai", p.getNamaPegawai(), MatchMode.ANYWHERE));
			//			}
			//			if (p.getInstansi()!=null){
			//				lc.add(Restrictions.eq("instansi", p.getInstansi()));
			//			}
			//			if (p.getJenjangPendTerakhir()!=null){
			//				lc.add(Restrictions.eq("jenjangPendTerakhir", p.getJenjangPendTerakhir()));
			//			}
		}
		List<Pegawai> dp=GenericPersistence.findList(Pegawai.class, lc);
		return dp;
	}
	public static List<Pegawai> searchPegawai(Instansi i){		
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("instansi", i));
		return findList(Pegawai.class, lc);
	}
	public static List<Pegawai> getPegawaiStruktural(Pegawai p){		
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.isNotNull("jbtnStruktural"));
		return findList(Pegawai.class, lc);
	}
	public static List<Pegawai> getPegawaiFungsional(Pegawai p){		
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.isNotNull("jbtnFungsional"));
		return findList(Pegawai.class, lc);
	}

}
