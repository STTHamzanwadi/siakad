package org.stth.siak.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.util.PasswordEncryptionService;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.User;

public class UserAuthenticationService {
	public static User getValidBackOfficeUser(String username, String password) {
		List<Criterion> critList = new ArrayList<Criterion>();
    	critList.add(Restrictions.eq("userName", username));
		List<?> l = GenericPersistence.findList(User.class, critList);
    	User p = (User) l.get(0);
		try {
			boolean isValidUser = PasswordEncryptionService.authenticate(password, 
					p.getPassword(), p.getSalt());
			if (isValidUser) return p;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static DosenKaryawan getValidKaryawan(String alias, String password) {
		List<Criterion> critList = new ArrayList<Criterion>();
    	critList.add(Restrictions.eq("alias", alias));
		List<?> l = GenericPersistence.findList(DosenKaryawan.class, critList);
		if (l.size()>0){
			DosenKaryawan o = (DosenKaryawan) l.get(0);
			if (o.getPassword()==null||o.getSalt()==null||password==null){
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
	public static DosenKaryawan getValidDosen(String alias, String password) {
		List<Criterion> critList = new ArrayList<Criterion>();
    	critList.add(Restrictions.eq("alias", alias));
    	critList.add(Restrictions.eq("dosen",true));
		List<?> l = GenericPersistence.findList(DosenKaryawan.class, critList);
		if (l.size()>0){
			DosenKaryawan o = (DosenKaryawan) l.get(0);
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
	public static Mahasiswa getValidMahasiswa(String nim, String password) {
		List<Criterion> critList = new ArrayList<Criterion>();
    	critList.add(Restrictions.eq("npm", nim));
    	//critList.add(Restrictions.eq("dosen",true));
		List<?> l = GenericPersistence.findList(Mahasiswa.class, critList);
		if (l.size()>0){
			Mahasiswa o = (Mahasiswa) l.get(0);
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
