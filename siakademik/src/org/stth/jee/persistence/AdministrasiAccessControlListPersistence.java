package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.UserAccessRightsAdministrasi;
import org.stth.siak.entity.DosenKaryawan;

public class AdministrasiAccessControlListPersistence {

	public static List<UserAccessRightsAdministrasi> getListByUser(DosenKaryawan user){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("user", user));
		List<UserAccessRightsAdministrasi> rslt = GenericPersistence.findList(UserAccessRightsAdministrasi.class,lc);
		return rslt;
	}

}
