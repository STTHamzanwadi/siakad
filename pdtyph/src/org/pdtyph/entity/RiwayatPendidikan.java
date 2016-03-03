package org.pdtyph.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class RiwayatPendidikan {
	@Id @GeneratedValue
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private Pegawai nmaPegawai;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private Instansi nmInstansi;
	private String jenjangPendidikan="";
	private String namaInstansi="";
	private int tahunLulus;
	private String jurusan="";
	private String gelarAkademis="";
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Pegawai getNmaPegawai() {
		return nmaPegawai;
	}
	public void setNmaPegawai(Pegawai nmaPegawai) {
		this.nmaPegawai = nmaPegawai;
	}
	public Instansi getNmInstansi() {
		return nmInstansi;
	}
	public void setNmInstansi(Instansi nmInstansi) {
		this.nmInstansi = nmInstansi;
	}
	public String getJenjangPendidikan() {
		return jenjangPendidikan;
	}
	public void setJenjangPendidikan(String jenjangPendidikan) {
		this.jenjangPendidikan = jenjangPendidikan;
	}
	public String getNamaInstansi() {
		return namaInstansi;
	}
	public void setNamaInstansi(String namaInstansi) {
		this.namaInstansi = namaInstansi;
	}
	public int getTahunLulus() {
		return tahunLulus;
	}
	public void setTahunLulus(int tahunLulus) {
		this.tahunLulus = tahunLulus;
	}
	public String getJurusan() {
		return jurusan;
	}
	public void setJurusan(String jurusan) {
		this.jurusan = jurusan;
	}
	public String getGelarAkademis() {
		return gelarAkademis;
	}
	public void setGelarAkademis(String gelarAkademis) {
		this.gelarAkademis = gelarAkademis;
	}
	
	
	
	
}
