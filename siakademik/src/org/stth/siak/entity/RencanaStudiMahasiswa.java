package org.stth.siak.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.stth.siak.enumtype.RencanaStudiCreationMethod;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.enumtype.StatusRencanaStudi;
@Entity
public class RencanaStudiMahasiswa {
	@Id @GeneratedValue
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private Mahasiswa mahasiswa;
	@Enumerated(EnumType.STRING)
	private Semester semester;
	private String tahunAjaran;
	private double ipk;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private DosenKaryawan pembimbingAkademik;
	private Date submitted;//
	private Date approved;
	private Date created;
	@Enumerated(EnumType.STRING)
	private RencanaStudiCreationMethod creationMethod;
	@Enumerated(EnumType.STRING)
	private StatusRencanaStudi status;//if rejected, mahasiswa can rearrange and resubmit after needed modifications
	private String remarks;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Mahasiswa getMahasiswa() {
		return mahasiswa;
	}
	public void setMahasiswa(Mahasiswa mahasiswa) {
		this.mahasiswa = mahasiswa;
	}
	
	public Semester getSemester() {
		return semester;
	}
	public void setSemester(Semester semester) {
		this.semester = semester;
	}
	public String getTahunAjaran() {
		return tahunAjaran;
	}
	public void setTahunAjaran(String tahun) {
		this.tahunAjaran = tahun;
	}
	public double getIpk() {
		return ipk;
	}
	public void setIpk(double ipk) {
		this.ipk = ipk;
	}
	public DosenKaryawan getPembimbingAkademik() {
		return pembimbingAkademik;
	}
	public void setPembimbingAkademik(DosenKaryawan pembimbingAkademik) {
		this.pembimbingAkademik = pembimbingAkademik;
	}
	public Date getSubmitted() {
		return submitted;
	}
	public void setSubmitted(Date submitted) {
		this.submitted = submitted;
	}
	public Date getApproved() {
		return approved;
	}
	public void setApproved(Date approved) {
		this.approved = approved;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public RencanaStudiCreationMethod getCreationMethod() {
		return creationMethod;
	}
	public void setCreationMethod(RencanaStudiCreationMethod creationMethod) {
		this.creationMethod = creationMethod;
	}
	public StatusRencanaStudi getStatus() {
		return status;
	}
	public void setStatus(StatusRencanaStudi status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
