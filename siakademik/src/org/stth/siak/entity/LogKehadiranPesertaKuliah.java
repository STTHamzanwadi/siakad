package org.stth.siak.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class LogKehadiranPesertaKuliah {
	@Id @GeneratedValue
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private LogPerkuliahan logPerkuliahan;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private Mahasiswa mahasiswa;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LogPerkuliahan getLogPerkuliahan() {
		return logPerkuliahan;
	}
	public void setLogPerkuliahan(LogPerkuliahan logPerkuliahan) {
		this.logPerkuliahan = logPerkuliahan;
	}
	public Mahasiswa getMahasiswa() {
		return mahasiswa;
	}
	public void setMahasiswa(Mahasiswa mahasiswa) {
		this.mahasiswa = mahasiswa;
	}
	

}
