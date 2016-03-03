package org.pdtyph.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;


@Entity
public class UserOprInstansi {
	@Id @GeneratedValue
	private int id;
	private String userName="";
	private String nama="";
	@ManyToOne(cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private Instansi instansi;
	private byte[] password;
	private byte[] salt;
	private String realName="";
	private Date registerDate;
	private String email="";
	private Date lastSuccessfulLogin;
	public UserOprInstansi() {
		registerDate=new Date();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Instansi getInstansi() {
		return instansi;
	}
	public void setInstansi(Instansi instansi) {
		this.instansi = instansi;
	}
	public byte[] getPassword() {
		return password;
	}
	public void setPassword(byte[] password) {
		this.password = password;
	}
	public byte[] getSalt() {
		return salt;
	}
	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getLastSuccessfulLogin() {
		return lastSuccessfulLogin;
	}
	public void setLastSuccessfulLogin(Date lastSuccessfulLogin) {
		this.lastSuccessfulLogin = lastSuccessfulLogin;
	}
	public boolean equals(Object o){
		if (!(o instanceof UserOprInstansi)) return false;
		if (o==null) return false;
		UserOprInstansi u = (UserOprInstansi) o;
		if (this.id==u.id) return true;
		return false;
	}
	public int hashCode(){
		return id;
	}
	
}
