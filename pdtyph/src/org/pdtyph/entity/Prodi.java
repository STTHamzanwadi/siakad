package org.pdtyph.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class Prodi {
	@Id @GeneratedValue
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private Instansi instansi;
	private String namaProdi="";
	private String kodeProdi="";
	private int tahunBerdiri;
	private String stsAkreditasi="";
	private String kaprodi="";
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Instansi getInstansi() {
		return instansi;
	}
	public void setInstansi(Instansi instansi) {
		this.instansi = instansi;
	}
	public String getNamaProdi() {
		return namaProdi;
	}
	public void setNamaProdi(String namaProdi) {
		this.namaProdi = namaProdi;
	}
	public String getKodeProdi() {
		return kodeProdi;
	}
	public void setKodeProdi(String kodeProdi) {
		this.kodeProdi = kodeProdi;
	}
	public int getTahunBerdiri() {
		return tahunBerdiri;
	}
	public void setTahunBerdiri(int tahunBerdiri) {
		this.tahunBerdiri = tahunBerdiri;
	}
	public String getStsAkreditasi() {
		return stsAkreditasi;
	}
	public void setStsAkreditasi(String stsAkreditasi) {
		this.stsAkreditasi = stsAkreditasi;
	}
	public String getKaprodi() {
		return kaprodi;
	}
	public void setKaprodi(String kaprodi) {
		this.kaprodi = kaprodi;
	}

	@Override
	public String toString() {
		return namaProdi;
	}
}
