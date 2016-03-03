package org.pdtyph.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UserGroup {
	@Id @GeneratedValue
	private int id;
	private String groupName;
	private boolean adminYayasan;
	private boolean operatorYayasan;
	private boolean adminInstansi;
	private boolean operatorInstansi;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public boolean isOperatorYayasan() {
		return operatorYayasan;
	}
	public void setOperatorYayasan(boolean operatorAkademik) {
		if (operatorYayasan){
			this.adminYayasan = false;
		}
		this.operatorYayasan = operatorAkademik;
	}
	public boolean isOperatorInstansi() {
		return operatorInstansi;
	}
	public void setOperatorInstansi(boolean operatorInstansi) {
		if (operatorInstansi){
			this.adminInstansi = false;
		}
		this.operatorInstansi = operatorInstansi;
	}

	public boolean isAdminYayasan() {
		return adminYayasan;
	}
	public void setAdminYayasan(boolean adminYayasan) {
		if (adminYayasan){
			operatorYayasan = false;
		}
		this.adminYayasan = adminYayasan;
	}
	public boolean isAdminInstansi() {
		return adminInstansi;
	}
	public void setAdminInstansi(boolean adminInstansi) {
		if (adminInstansi){
			operatorInstansi = false;
		}
		this.adminInstansi = adminInstansi;
	}
	public String toString(){
		return groupName;
	}

}
