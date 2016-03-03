package org.pdtyph.entity;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;


public abstract class BasicEntity {
	@ManyToOne (cascade= CascadeType.REFRESH)
	private UserOprInstansi createdBy;
	private Date created;
	@ManyToOne (cascade= CascadeType.REFRESH)
	private UserOprInstansi modifiedBy;
	private Date modified;
	private String address;
	public UserOprInstansi getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserOprInstansi createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public UserOprInstansi getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(UserOprInstansi modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	@PrePersist
	protected void onCreate(){
		created = new Date();
		//createdBy = Utama.getSVars().currentUser;
	}
	@PreUpdate
	protected void onUpdate(){
		modified = new Date();
		//modifiedBy = Utama.getSVars().currentUser;
	}
	
	private PropertyChangeSupport changeSupport = 
		      new PropertyChangeSupport(this);

		  public void addPropertyChangeListener(PropertyChangeListener 
		      listener) {
		    changeSupport.addPropertyChangeListener(listener);
		  }

		  public void removePropertyChangeListener(PropertyChangeListener 
		      listener) {
		    changeSupport.removePropertyChangeListener(listener);
		  }

		  public void addPropertyChangeListener(String propertyName,
		      PropertyChangeListener listener) {
		    changeSupport.addPropertyChangeListener(propertyName, listener);
		  }

		  public void removePropertyChangeListener(String propertyName,
		      PropertyChangeListener listener) {
		    changeSupport.removePropertyChangeListener(propertyName, listener);
		  }

		  protected void firePropertyChange(String propertyName, 
		      Object oldValue,
		      Object newValue) {
		    changeSupport.firePropertyChange(propertyName, oldValue, newValue);
		  }

}
