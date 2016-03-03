package org.pdtyph.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class RiwayatJabatan {
	@Id @GeneratedValue
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private Pegawai nmPegawai;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private Instansi nmInstansi;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private Jabatan nmJabatan;
	private int mulai;
	private int selesai;
	private String noSK="";
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Pegawai getNmPegawai() {
		return nmPegawai;
	}


	public void setNmPegawai(Pegawai nmPegawai) {
		this.nmPegawai = nmPegawai;
	}


	public Instansi getNmInstansi() {
		return nmInstansi;
	}


	public void setNmInstansi(Instansi nmInstansi) {
		this.nmInstansi = nmInstansi;
	}


	public Jabatan getNmJabatan() {
		return nmJabatan;
	}


	public void setNmJabatan(Jabatan nmJabatan) {
		this.nmJabatan = nmJabatan;
	}


	public int getMulai() {
		return mulai;
	}


	public void setMulai(int mulai) {
		this.mulai = mulai;
	}


	public int getSelesai() {
		return selesai;
	}


	public void setSelesai(int selesai) {
		this.selesai = selesai;
	}


	public String getNoSK() {
		return noSK;
	}


	public void setNoSK(String noSK) {
		this.noSK = noSK;
	}

	
}
