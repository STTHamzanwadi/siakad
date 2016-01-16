package org.stth.siak.entity;

import java.util.Comparator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
@Entity
public class MataKuliah implements Comparable<MataKuliah>, Comparator<MataKuliah>{
	@Id @GeneratedValue
	private int id;
	private String kode;
	private String nama;
	private int sks;
	private int semesterBuka;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private ProgramStudi prodiPemilik;
	private boolean aktif;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public int getSks() {
		return sks;
	}
	public void setSks(int sks) {
		this.sks = sks;
	}
	public int getSemesterBuka() {
		return semesterBuka;
	}
	public void setSemesterBuka(int semesterBuka) {
		this.semesterBuka = semesterBuka;
	}
	public ProgramStudi getProdiPemilik() {
		return prodiPemilik;
	}
	public void setProdiPemilik(ProgramStudi prodi) {
		this.prodiPemilik = prodi;
	}

	public boolean isAktif() {
		return aktif;
	}
	public void setAktif(boolean aktif) {
		this.aktif = aktif;
	}
	public String toString(){
		return this.kode+" "+this.nama;
	}
	@Override
	public int compare(MataKuliah o1, MataKuliah o2) {
		return o1.getKode().compareTo(o2.getKode());
	}
	@Override
	public int compareTo(MataKuliah o) {
		return kode.compareTo(o.getKode());
	}
	

}
