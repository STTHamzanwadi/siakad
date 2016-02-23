package org.stth.siak.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class LogPerkuliahan {
	@Id @GeneratedValue
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private KelasPerkuliahan kelasPerkuliahan;
	private Date tanggalPertemuan;
	private String ruangPertemuan;
	private String materiPertemuan;
	private boolean olehDosen;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
    private DosenKaryawan diisiOleh;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private DosenKaryawan entryOleh;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public KelasPerkuliahan getKelasPerkuliahan() {
		return kelasPerkuliahan;
	}
	public void setKelasPerkuliahan(KelasPerkuliahan kelasPerkuliahan) {
		this.kelasPerkuliahan = kelasPerkuliahan;
	}
	public Date getTanggalPertemuan() {
		return tanggalPertemuan;
	}
	public void setTanggalPertemuan(Date tanggalPertemuan) {
		this.tanggalPertemuan = tanggalPertemuan;
	}
	public String getRuangPertemuan() {
		return ruangPertemuan;
	}
	public void setRuangPertemuan(String ruangPertemuan) {
		this.ruangPertemuan = ruangPertemuan;
	}
	public String getMateriPertemuan() {
		return materiPertemuan;
	}
	public void setMateriPertemuan(String materiPertemuan) {
		this.materiPertemuan = materiPertemuan;
	}
	public boolean isOlehDosen() {
		return olehDosen;
	}
	public void setOlehDosen(boolean olehDosen) {
		this.olehDosen = olehDosen;
	}
	public DosenKaryawan getEntryOleh() {
		return entryOleh;
	}
	public void setEntryOleh(DosenKaryawan entryOleh) {
		this.entryOleh = entryOleh;
	}
	public DosenKaryawan getDiisiOleh() {
		return diisiOleh;
	}
	public void setDiisiOleh(DosenKaryawan diisiOleh) {
		this.diisiOleh = diisiOleh;
	}
	

}
