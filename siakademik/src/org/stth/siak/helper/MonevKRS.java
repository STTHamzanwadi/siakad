package org.stth.siak.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.jee.persistence.RencanaStudiPersistence;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.enumtype.Semester;

public class MonevKRS {
	private List<RekapMonevKRS> rekap = new ArrayList<>();
	private List<Mahasiswa> mahasiswa;
	private List<ProgramStudi> listProdi;
	private Semester semester;
	private String ta;
	private int angkatan=0;
	private ProgramStudi prodi = null;
	private Map<Integer, ProgramStudi> mapProdi;
	
	public MonevKRS(Semester sem, String tahunAjaran){
		this.semester = sem;
		this.ta = tahunAjaran;
		angkatan = 0;
		prodi = null;
		loadData();
		prepareRekap();
		calculate();
	}
	
	public MonevKRS(Semester sem, String tahunAjaran, int angkatan, ProgramStudi prodi){
		this.semester = sem;
		this.ta = tahunAjaran;
		this.angkatan = angkatan;
		loadData();
		prepareRekap();
		calculate();
	}
	
	

	private void calculate() {
		for (Mahasiswa mahasiswa2 : mahasiswa) {
			ProgramStudi pm = mahasiswa2.getProdi();
			RencanaStudiMahasiswa rsm = RencanaStudiPersistence.getByMhsSemTa(mahasiswa2, semester, ta);
			for (RekapMonevKRS e : rekap) {
				if (e.prodi.equals(pm)){
					if (rsm!=null){
						switch (rsm.getStatus()) {
						case DRAFT:
							e.draft++;
							break;
						case DIAJUKAN:
							e.diajukan++;
							break;
						case DISETUJUI:
							e.disetujui++;
							break;
						case DITOLAK:
							e.ditolak++;
							break;
						case FINAL:
							e.finaL++;
							break;
						default:
							break;
						}
						
					} else {
						e.belumSusun++;
					}
					break;
				}
				
			}
			
		}
		
	}



	private void prepareRekap() {
		for (ProgramStudi p : listProdi) {
			RekapMonevKRS e = new RekapMonevKRS();
			e.prodi = p;
			rekap.add(e);
		}
		
	}

	public List<RekapMonevKRS> getRekap(){
		return rekap;
	}

	private void loadData() {
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

	public class RekapMonevKRS{
		ProgramStudi prodi;
		
		int belumSusun=0;
		int draft=0;
		int diajukan=0;
		int disetujui=0;
		int ditolak=0;
		int finaL=0;
		public ProgramStudi getProdi() {
			return prodi;
		}
		public void setProdi(ProgramStudi prodi) {
			this.prodi = prodi;
		}
		public int getBelumSusun() {
			return belumSusun;
		}
		public void setBelumSusun(int belumSusun) {
			this.belumSusun = belumSusun;
		}
		public int getDraft() {
			return draft;
		}
		public void setDraft(int draft) {
			this.draft = draft;
		}
		public int getDiajukan() {
			return diajukan;
		}
		public void setDiajukan(int diajukan) {
			this.diajukan = diajukan;
		}
		public int getDisetujui() {
			return disetujui;
		}
		public void setDisetujui(int disetujui) {
			this.disetujui = disetujui;
		}
		public int getDitolak() {
			return ditolak;
		}
		public void setDitolak(int ditolak) {
			this.ditolak = ditolak;
		}
		public int getFinaL() {
			return finaL;
		}
		public void setFinaL(int finaL) {
			this.finaL = finaL;
		}
	
	}

}
