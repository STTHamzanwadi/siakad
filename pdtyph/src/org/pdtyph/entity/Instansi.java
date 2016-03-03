package org.pdtyph.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Instansi {
	@Id @GeneratedValue
	private int id;
	private String noTelp="";
	private String nama="";
	private String kode="";
	private String alamat="";
	private String jenisLembaga;
	private String email="";
	private int thnBerdiri;
	private String stsAkreditasi="";
	private String pimpinan="";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNoTelp() {
		return noTelp;
	}
	public void setNoTelp(String noTelp) {
		this.noTelp = noTelp;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getJenisLembaga() {
		return jenisLembaga;
	}
	public void setJenisLembaga(String jenisLembaga) {
		this.jenisLembaga = jenisLembaga;
	}
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public int getThnBerdiri() {
		return thnBerdiri;
	}
	public void setThnBerdiri(int thnBerdiri) {
		this.thnBerdiri = thnBerdiri;
	}
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStsAkreditasi() {
		return stsAkreditasi;
	}
	public void setStsAkreditasi(String stsAkreditasi) {
		this.stsAkreditasi = stsAkreditasi;
	}
	public String getPimpinan() {
		return pimpinan;
	}
	public void setPimpinan(String pimpinan) {
		this.pimpinan = pimpinan;
	}
	
	@Override
	public String toString() {
		return nama;
	}
	
}
