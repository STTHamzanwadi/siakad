package org.stth.siak.rpt;

import org.stth.siak.entity.PesertaKuliah;

public class PesertaKuliahReportElement {
	private String nim;
	private String nama;
	private String nilai;
	public PesertaKuliahReportElement(String nim, String nama, String nilai) {
		super();
		this.nim = nim;
		this.nama = nama;
		this.nilai = nilai;
	}
	public PesertaKuliahReportElement(PesertaKuliah p){
		this.nim = p.getMahasiswa().getNpm();
		this.nama = p.getMahasiswa().getNama();
		this.nilai = p.getNilai();
	}
	public String getNim() {
		return nim;
	}
	public void setNim(String nim) {
		this.nim = nim;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getNilai() {
		return nilai;
	}
	public void setNilai(String nilai) {
		this.nilai = nilai;
	}

}
