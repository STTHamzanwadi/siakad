package org.stth.siak.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.jee.persistence.RencanaStudiPilihanMataKuliahPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;
import org.stth.siak.enumtype.Semester;

public class MonevKRSMataKuliah {
	private List<RencanaStudiPilihanMataKuliah> lrspmkRaw = new ArrayList<>();
	private List<RencanaStudiPilihanMataKuliah> lrspmk = new ArrayList<>();
	private List<Mahasiswa> mahasiswa;
	private List<ProgramStudi> listProdi;
	private Semester semester;
	private String tahunAjaran;
	private int angkatan=0;
	private ProgramStudi prodi = null;
	private Map<Integer, ProgramStudi> mapProdi;
	private Map<Integer, DosenKaryawan> mapDosenPa;
	private Map<String, RekapPengambilanMataKuliah> mapRekapMatkul;
	
	public MonevKRSMataKuliah(Semester sem, String tahunAjaran){
		this.semester = sem;
		this.tahunAjaran = tahunAjaran;
		angkatan = 0;
		prodi = null;
		perform();
	}
	
	public MonevKRSMataKuliah(Semester sem, String tahunAjaran, int angkatan, ProgramStudi prodi){
		this.semester = sem;
		this.tahunAjaran = tahunAjaran;
		this.angkatan = angkatan;
		this.prodi = prodi;
		perform();
	}
	
	void perform(){
		filter();
		loadDataMataKuliahTerpilih();
		calculate();
		
	}

	private void filter() {
		List<RencanaStudiPilihanMataKuliah> lrspmktemp= new ArrayList<>();
		List<RencanaStudiPilihanMataKuliah> lrspmktemp2= new ArrayList<>();
		//filter by prodi
		if (prodi!=null){
			for (RencanaStudiPilihanMataKuliah r : lrspmkRaw){
				Mahasiswa m = r.getRencanaStudi().getMahasiswa();
				ProgramStudi p = m.getProdi();
				int angkatan = m.getAngkatan();
				if (p.getId()==prodi.getId()){
					lrspmktemp.add(r);
				}
			}
		} else {
			lrspmktemp.addAll(lrspmkRaw);
		}
		//remove by angkatan
		if (angkatan>0){
			for (RencanaStudiPilihanMataKuliah r : lrspmktemp){
				Mahasiswa m = r.getRencanaStudi().getMahasiswa();
				ProgramStudi p = m.getProdi();
				int a = m.getAngkatan();
				if (angkatan==a){
					lrspmktemp2.add(r);
				}
			}
		}
		lrspmk.addAll(lrspmktemp2);
	}

	private void calculate() {
		for (RencanaStudiPilihanMataKuliah r : lrspmk){
			String key = getElementPengambilanKey(r);
			RekapPengambilanMataKuliah rpmk;
			if (mapRekapMatkul.containsKey(key)){
				rpmk = mapRekapMatkul.get(key);
				rpmk.ambil++;
				
			} else {
				rpmk = new RekapPengambilanMataKuliah(r);
				mapRekapMatkul.put(key, rpmk);
			}
			
		}
		
	}



	private void loadDataMataKuliahTerpilih() {
		//load data matakuliah terpilih berdasarkan semester dan angkatan
		lrspmkRaw = RencanaStudiPilihanMataKuliahPersistence.getBySemesterTahunAjaran(semester, tahunAjaran);
		//load mahasiswa aktif berdasarkan kriteria
		Mahasiswa m = new Mahasiswa();
		if (angkatan>0){
			m.setAngkatan(angkatan);
		}
		if (prodi!=null){
			m.setProdi(prodi);
		}
		mahasiswa = MahasiswaPersistence.getListByExample(m);
		//load seluruh prodi
		listProdi = GenericPersistence.findList(ProgramStudi.class);
	}

	
	
	public class RekapPengambilanMataKuliah{
		String kodeMataKuliah;
		String namaMataKuliah;
		int sks;
		String prodi;
		int angkatan;
		int ambil;
		public RekapPengambilanMataKuliah(RencanaStudiPilihanMataKuliah rspmk) {
			MataKuliah mk = rspmk.getMataKuliah();
			Mahasiswa m = rspmk.getRencanaStudi().getMahasiswa();
			ProgramStudi p = m.getProdi();
			this.kodeMataKuliah = mk.getKode();
			this.namaMataKuliah = mk.getNama();
			this.sks = mk.getSks();
			this.prodi = p.getNama();
			this.angkatan = m.getAngkatan();
			this.ambil = 1;
		}
		public String getKodeMataKuliah() {
			return kodeMataKuliah;
		}
		public void setKodeMataKuliah(String kodeMataKuliah) {
			this.kodeMataKuliah = kodeMataKuliah;
		}
		public String getNamaMataKuliah() {
			return namaMataKuliah;
		}
		public void setNamaMataKuliah(String namaMataKuliah) {
			this.namaMataKuliah = namaMataKuliah;
		}
		public int getSks() {
			return sks;
		}
		public void setSks(int sks) {
			this.sks = sks;
		}
		public String getProdi() {
			return prodi;
		}
		public void setProdi(String prodi) {
			this.prodi = prodi;
		}
		public int getAngkatan() {
			return angkatan;
		}
		public void setAngkatan(int angkatan) {
			this.angkatan = angkatan;
		}
		public int getAmbil() {
			return ambil;
		}
		public void setAmbil(int ambil) {
			this.ambil = ambil;
		}
				
	}
	
	private String getElementPengambilanKey(RencanaStudiPilihanMataKuliah rspmk){
		String s = "";
		if (rspmk!=null){
			Mahasiswa m = rspmk.getRencanaStudi().getMahasiswa();
			ProgramStudi p = m.getProdi();
			int angkatan = m.getAngkatan();
			String matkul = rspmk.getMataKuliah().toString();
			return p+"a"+angkatan+"m"+matkul;
		}
		return s;
	}

}
