package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.MataKuliahKurikulum;

public class MataKuliahKurikulumPersistence {
	
	
	public static List<MataKuliahKurikulum> getByKurikulum(Kurikulum kr){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("kurikulum", kr));
		List<MataKuliahKurikulum> rslt = GenericPersistence.findList(MataKuliahKurikulum.class,lc);
		return rslt;
	}
	public static List<MataKuliahKurikulum> get(MataKuliahKurikulum mk){
		List<Criterion> c = new ArrayList<>();
		String[] map=null;
		if (mk.getKurikulum()!=null) {
			c.add(Restrictions.eq("kurikulum", mk.getKurikulum()));
		}
		if (mk.getMataKuliah()!=null) {
			map= new String[]{"mataKuliah"};
			if (mk.getMataKuliah().getKode()!=null) {
				c.add(Restrictions.like("mataKuliah.kode", mk.getMataKuliah().getKode(), MatchMode.ANYWHERE));
			}
			if (mk.getMataKuliah().getNama()!=null) {
				c.add(Restrictions.like("mataKuliah.nama", mk.getMataKuliah().getNama(), MatchMode.ANYWHERE));
			}
			
		}
		List<MataKuliahKurikulum> l = GenericPersistence.findList(MataKuliahKurikulum.class, c, map);
		return l;
	}
	

}
