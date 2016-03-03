package org.pdtyph.entity;

import java.util.Comparator;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;



@Entity
public class Pegawai implements Comparable<Pegawai>, Comparator<Pegawai> {
	@Id @GeneratedValue
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private Instansi instansi;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private Jabatan jbtnStruktural;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private Jabatan jbtnFungsional;
	private String namaPegawai="";
	private String kelamin;
	private String alias="";
	private String niy="";
	private String nidn="";
	private String nis="";
	private String tempatLahir="";
	private Date tanggalLahir;	
	private String agama;
	private String email="";
	private String alamatRumah="";
	private String nomorTelepon="";
	private String nomorKtp="";
	private String namaKepegawaian;
	private String statusKepegawaian;
	private String golongan="";
	private int thnMasuk; 
	private String jenjangPendTerakhir;
	private String prodiPendTerakhir="";
	private String institusiPendTerakhir="";
	
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
	public String getNamaKepegawaian() {
		return namaKepegawaian;
	}
	public void setNamaKepegawaian(String namaKepegawaian) {
		this.namaKepegawaian = namaKepegawaian;
	}
	public Jabatan getJbtnStruktural() {
		return jbtnStruktural;
	}
	public void setJbtnStruktural(Jabatan jbtnStruktural) {
		this.jbtnStruktural = jbtnStruktural;
	}
	public Jabatan getJbtnFungsional() {
		return jbtnFungsional;
	}
	public void setJbtnFungsional(Jabatan jbtnFungsional) {
		this.jbtnFungsional = jbtnFungsional;
	}
	public String getNamaPegawai() {
		return namaPegawai;
	}
	public void setNamaPegawai(String namaPegawai) {
		this.namaPegawai = namaPegawai;
	}
	public String getKelamin() {
		return kelamin;
	}
	public void setKelamin(String kelamin) {
		this.kelamin = kelamin;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getNiy() {
		return niy;
	}
	public void setNiy(String niy) {
		this.niy = niy;
	}
	public String getNidn() {
		return nidn;
	}
	public void setNidn(String nidn) {
		this.nidn = nidn;
	}
	public String getNis() {
		return nis;
	}
	public void setNis(String nis) {
		this.nis = nis;
	}
	public String getTempatLahir() {
		return tempatLahir;
	}
	public void setTempatLahir(String tempatLahir) {
		this.tempatLahir = tempatLahir;
	}
	public Date getTanggalLahir() {
		return tanggalLahir;
	}
	public void setTanggalLahir(Date tanggalLahir) {
		this.tanggalLahir = tanggalLahir;
	}
	public String getAgama() {
		return agama;
	}
	public void setAgama(String agama) {
		this.agama = agama;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAlamatRumah() {
		return alamatRumah;
	}
	public void setAlamatRumah(String alamatRumah) {
		this.alamatRumah = alamatRumah;
	}
	public String getNomorTelepon() {
		return nomorTelepon;
	}
	public void setNomorTelepon(String nomorTelepon) {
		this.nomorTelepon = nomorTelepon;
	}
	public String getNomorKtp() {
		return nomorKtp;
	}
	public void setNomorKtp(String nomorKtp) {
		this.nomorKtp = nomorKtp;
	}
	public String getStatusKepegawaian() {
		return statusKepegawaian;
	}
	public void setStatusKepegawaian(String statusKepegawaian) {
		this.statusKepegawaian = statusKepegawaian;
	}
	public String getGolongan() {
		return golongan;
	}
	public void setGolongan(String golongan) {
		this.golongan = golongan;
	}
	public int getThnMasuk() {
		return thnMasuk;
	}
	public void setThnMasuk(int thnMasuk) {
		this.thnMasuk = thnMasuk;
	}
	public String getJenjangPendTerakhir() {
		return jenjangPendTerakhir;
	}
	public void setJenjangPendTerakhir(String jenjangPendTerakhir) {
		this.jenjangPendTerakhir = jenjangPendTerakhir;
	}
	public String getProdiPendTerakhir() {
		return prodiPendTerakhir;
	}
	public void setProdiPendTerakhir(String prodiPendTerakhir) {
		this.prodiPendTerakhir = prodiPendTerakhir;
	}
	public String getInstitusiPendTerakhir() {
		return institusiPendTerakhir;
	}
	public void setInstitusiPendTerakhir(String institusiPendTerakhir) {
		this.institusiPendTerakhir = institusiPendTerakhir;
	}
	@Override
	public int compare(Pegawai arg0, Pegawai arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int compareTo(Pegawai arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String toString() {
		return namaPegawai;
	}
}
