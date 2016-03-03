package org.pdtyph.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.pdtyph.entity.Admin;
import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.UserOprInstansi;
import org.pdtyph.entity.UserOprYayasan;
import org.yph.jee.persistence.GenericPersistence;
import org.yph.jee.util.PasswordEncryptionService;

public class UserAuthenticationService {
	
	public static Admin getValidMyAdmin(String userName, String password) {
		List<Criterion> critList = new ArrayList<Criterion>();
    	critList.add(Restrictions.eq("userName", userName));
    	critList.add(Restrictions.eq("nama",true));
		List<?> l = GenericPersistence.findList( Admin.class, critList);
		if (l.size()>0){
			Admin o = ( Admin) l.get(0);
			if (o.getPassword()==null||o.getSalt()==null){
				return null;
			}
			try {
				boolean isValidUser = PasswordEncryptionService.authenticate(password, 
						o.getPassword(), o.getSalt());
				if (isValidUser) return o;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static Admin getValidAdmin(String alias, String password) {
		List<Criterion> critList = new ArrayList<Criterion>();
    	critList.add(Restrictions.eq("userName", alias));
    	List<?> l = GenericPersistence.findList(Admin.class, critList);
		if (l.size()>0){
			Admin o = (Admin) l.get(0);
			if (o.getPassword()==null||o.getSalt()==null){
				return null;
			}
			try {
				boolean isValidUser = PasswordEncryptionService.authenticate(password, 
						o.getPassword(), o.getSalt());
				if (isValidUser) return o;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	public static UserOprInstansi getValidBackOfficeUser(String username, String password) {
		List<Criterion> critList = new ArrayList<Criterion>();
    	critList.add(Restrictions.eq("userName", username));
    	critList.add(Restrictions.eq("nama",true));
		List<?> l = GenericPersistence.findList(Instansi.class, critList);
		UserOprInstansi p = (UserOprInstansi) l.get(0);
		try {
			boolean isValidUser = PasswordEncryptionService.authenticate(password,p.getPassword(), p.getSalt());
			if (isValidUser) return p;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static UserOprInstansi getValidInstansi(String alias, String password) {
		List<Criterion> critList = new ArrayList<Criterion>();
    	critList.add(Restrictions.eq("userName", alias));
    	List<?> l = GenericPersistence.findList(UserOprInstansi.class, critList);
		if (l.size()>0){
			UserOprInstansi o = (UserOprInstansi) l.get(0);
			if (o.getPassword()==null||o.getSalt()==null){
				return null;
			}
			try {
				boolean isValidUser = PasswordEncryptionService.authenticate(password, 
						o.getPassword(), o.getSalt());
				if (isValidUser) return o;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static UserOprYayasan getValidOperator(String userName, String password) {
		List<Criterion> critList = new ArrayList<Criterion>();
    	critList.add(Restrictions.eq("userName", userName));
    	critList.add(Restrictions.eq("nama",true));
		List<?> l = GenericPersistence.findList( UserOprYayasan.class, critList);
		if (l.size()>0){
			UserOprYayasan o = ( UserOprYayasan) l.get(0);
			if (o.getPassword()==null||o.getSalt()==null){
				return null;
			}
			try {
				boolean isValidUser = PasswordEncryptionService.authenticate(password, 
						o.getPassword(), o.getSalt());
				if (isValidUser) return o;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static UserOprYayasan getValidOpryayasan(String alias, String password) {
		List<Criterion> critList = new ArrayList<Criterion>();
    	critList.add(Restrictions.eq("userName", alias));
    	List<?> l = GenericPersistence.findList(UserOprYayasan.class, critList);
		if (l.size()>0){
			UserOprYayasan o = (UserOprYayasan) l.get(0);
			if (o.getPassword()==null||o.getSalt()==null){
				return null;
			}
			try {
				boolean isValidUser = PasswordEncryptionService.authenticate(password, 
						o.getPassword(), o.getSalt());
				if (isValidUser) return o;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
}
