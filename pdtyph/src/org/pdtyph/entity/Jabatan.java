package org.pdtyph.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class Jabatan {
	@Id @GeneratedValue
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private Instansi instansi;
	private String namaJabatan="";
	private String jnsJabatan="";
	private int urut;
	
	

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



	public String getNamaJabatan() {
		return namaJabatan;
	}



	public void setNamaJabatan(String namaJabatan) {
		this.namaJabatan = namaJabatan;
	}



	public String getJnsJabatan() {
		return jnsJabatan;
	}



	public void setJnsJabatan(String jnsJabatan) {
		this.jnsJabatan = jnsJabatan;
	}



	public int getUrut() {
		return urut;
	}



	public void setUrut(int urut) {
		this.urut = urut;
	}



	@Override
	public String toString() {
		return namaJabatan;
	}


}
