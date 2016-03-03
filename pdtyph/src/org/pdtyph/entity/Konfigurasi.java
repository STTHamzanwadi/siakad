package org.pdtyph.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity
public class Konfigurasi {
	
	@Id @GeneratedValue
	private int id;
	private String namaKonfigurasi;
	private String nilaiKonfigurasi;
	public Konfigurasi(){
		
	}
	public Konfigurasi(String nama, String nilai) {
		namaKonfigurasi = nama;
		nilaiKonfigurasi = nilai;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNamaKonfigurasi() {
		return namaKonfigurasi;
	}
	public void setNamaKonfigurasi(String namaKonfigurasi) {
		this.namaKonfigurasi = namaKonfigurasi;
	}
	public String getNilaiKonfigurasi() {
		return nilaiKonfigurasi;
	}
	public void setNilaiKonfigurasi(String nilaiKonfigurasi) {
		this.nilaiKonfigurasi = nilaiKonfigurasi;
	}
	
	
}
