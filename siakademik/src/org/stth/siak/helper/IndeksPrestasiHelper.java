package org.stth.siak.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliahKurikulum;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.rpt.TranskripReportElement;
import org.stth.siak.rpt.TranskripWisudaElement;

public class IndeksPrestasiHelper {
	private ArrayList<PesertaKuliah> nilai;
	private Mahasiswa mhs;
	private double ipk=0.0;
	private int totsks=0;
	private int totsksD=0;
	private double kumNilai;
	private Map<String, PesertaKuliah> m;
	private Map<String, PesertaKuliah> nilaiD;
	private Map<String,Integer> semMatKuKur;
	private Kurikulum kur;

	public IndeksPrestasiHelper(Mahasiswa mhs){
		this.mhs = mhs;
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("prodi", mhs.getProdi()));
		List<?> l = GenericPersistence.findList(Kurikulum.class, lc);
		Kurikulum k=null;
		if (!l.isEmpty()){
			k = (Kurikulum) l.get(0);
		}
		List<Criterion> lc2 = new ArrayList<>();
		lc2.add(Restrictions.eq("mahasiswa", mhs));
		List<?> nilaiDirty = GenericPersistence.findList(PesertaKuliah.class, lc2);
		cekMatkulDiambilUlang(nilaiDirty);
		hitungIPK();
		setKur(k);
	}
	public IndeksPrestasiHelper(Mahasiswa mhs, Kurikulum k){
		this.mhs = mhs;
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("mahasiswa", mhs));
		List<?> nilaiDirty = GenericPersistence.findList(PesertaKuliah.class, lc);
		cekMatkulDiambilUlang(nilaiDirty);
		hitungIPK();
		setKur(k);
	}
	public IndeksPrestasiHelper(List<?> nilaiDirty, Mahasiswa mhs){
		this.mhs = mhs;
		cekMatkulDiambilUlang(nilaiDirty);
		hitungIPK();
	}
	public IndeksPrestasiHelper(List<?> nilaiDirty, Mahasiswa mhs, Kurikulum k) {
		this.mhs = mhs;
		cekMatkulDiambilUlang(nilaiDirty);
		hitungIPK();
		setKur(k);
	}
	public Map<String, PesertaKuliah> getNilaiCleanMapped(){
		return m;
	}
	private int convertNilai(String nl){
		int n=0;
		if (nl!=null) {
			switch (nl) {
			case "A":
				n = 4;
				break;
			case "B":
				n = 3;
				break;
			case "C":
				n = 2;
				break;
			case "D":
				n = 1;
				break;
			default:
				break;
			}
		}
		return n;
	}
	private void hitungIPK(){
		kumNilai = 0.0;
		if (nilai.size()>0){
			for (PesertaKuliah p : nilai) {
				totsks += p.getCopiedSKSMatkul();
				if (p.getNilai()!=null) {
					if (p.getNilai().equals("D")) {
						nilaiD.put(p.getCopiedKodeMatkul(), p);
						totsksD += p.getCopiedSKSMatkul();
					}
				}
				int bobot= convertNilai(p.getNilai());
				kumNilai += (p.getCopiedSKSMatkul()*bobot);
			}
			ipk= kumNilai/totsks;
		}
	}
	private void cekMatkulDiambilUlang(List<?> nilaiDirty){
		m = new HashMap<>();
		nilaiD = new HashMap<>();
		for (Object o : nilaiDirty) {
			PesertaKuliah p = (PesertaKuliah) o;
			if (p.getNilai()!=null) {
				if (!(p.getNilai().equals("") || p.getNilai().equals("0"))) {
					if (m.containsKey(p.getCopiedNamaMatkul())) {
						PesertaKuliah p1 = m.get(p.getCopiedNamaMatkul());
						int p1n = convertNilai(p1.getNilai());
						int pn = convertNilai(p.getNilai());
						if (pn > p1n) {
							m.remove(p1);
							m.put(p.getCopiedNamaMatkul(), p);
							//m.put(p.getCopiedNamaMatkul(), p);
						}
					} else {
						m.put(p.getCopiedNamaMatkul(), p);
						//m.put(p.getCopiedNamaMatkul(), p);
					}
				}
			}
		}
		nilai = new ArrayList<>(m.values());
		Collections.sort(nilai);
	}
	public Mahasiswa getMhs() {
		return mhs;
	}
	public double getIpk() {
		return ipk;
	}
	public int getTotsks() {
		return totsks;
	}
	public ArrayList<PesertaKuliah> getNilaiClean(){
		return nilai;
	}
	public Map<String, PesertaKuliah> getNilaiD() {
		return nilaiD;
	}

	public double getTotnilai() {
		return kumNilai;
	}
	public double getSKStotal(){
		return totsks;
	}
	public double getSKSD(){
		return totsksD;
	}
	public String getPredikat() {
		if (ipk>=3.5){
			return "Cum Laude";
		} else if (ipk>=3.0){
			return "Sangat Memuaskan";
		} else if (ipk>=2.5){
			return "Memuaskan";
		} else if (ipk>=2){
			return "Cukup";
		} else if (ipk>=2){
			return "Kurang Memuaskan";
		} else {
			return "Tidak Memuaskan";
		}
	}
	public Kurikulum getKur() {
		return kur;
	}
	public void setKur(Kurikulum kur) {
		this.kur = kur;
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("kurikulum", kur));
		List<?> l = GenericPersistence.findList(MataKuliahKurikulum.class, lc);
		semMatKuKur = new HashMap<>();
		for (Object object : l) {
			MataKuliahKurikulum mkk = (MataKuliahKurikulum) object;
			semMatKuKur.put(mkk.getMataKuliah().getKode(), mkk.getSemesterBuka());
		}
	}
	private int getSem(String mkkode){
		if (semMatKuKur!=null) {
			Integer sem = semMatKuKur.get(mkkode);
			if (sem!=null){
				return sem;
			}
			System.out.println(mkkode +" "+ sem);
		}
		return 0;
	}
	public ArrayList<TranskripReportElement> getTranskripReportElements(){
		ArrayList<TranskripReportElement> rslt = new ArrayList<>();
		for (PesertaKuliah p : nilai) {
			rslt.add(new TranskripReportElement(p,getSem(p.getCopiedKodeMatkul())));

		}
		Collections.sort(rslt);
		if (this.mhs.getProdi().getNama().equals("Manajemen Informatika")) {
			TranskripReportElement tre = rslt.get(0);
			if(tre.getNamamk().equals("Tugas Akhir")){
				rslt.remove(0);
				rslt.add(tre);
			}
			
		}
		return rslt;
	}
	public ArrayList<TranskripWisudaElement> getTranskripWisudaReportElements(){
		ArrayList<TranskripWisudaElement> rslt = new ArrayList<>();
		ArrayList<TranskripReportElement> listTransKiri = new ArrayList<>();
		ArrayList<TranskripReportElement> listTransKanan= new ArrayList<>();
		int no =1;
		int batas = getTranskripReportElements().size()/2 +2;
		
		TranskripWisudaElement twe = new TranskripWisudaElement();
		for (TranskripReportElement tre : getTranskripReportElements()) {
			if (no<=batas) {
				listTransKiri.add(tre);		
			}else{
				listTransKanan.add(tre);
			}
			no++;
		}
		twe.setListTransKiri(listTransKiri);
		twe.setListTransKanan(listTransKanan);
		rslt.add(twe);
		return rslt;
	}
}
