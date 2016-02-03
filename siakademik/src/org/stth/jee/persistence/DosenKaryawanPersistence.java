package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.siak.entity.BerkasFotoDosen;
import org.stth.siak.entity.DosenKaryawan;

public class DosenKaryawanPersistence {
	public static List<DosenKaryawan> getDosen(){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("dosen", true));
		List<DosenKaryawan> l = GenericPersistence.findList(DosenKaryawan.class, lc);
		return l;
	}
	public static void updatePicture(DosenKaryawan d, byte[] image){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("dosen", d));
		List<BerkasFotoDosen> lg = GenericPersistence.findList(BerkasFotoDosen.class, lc);
		BerkasFotoDosen bfd;
		if (lg.size()>0){
			bfd = lg.get(0);
		} else {
			bfd = new BerkasFotoDosen();
			bfd.setDosen(d);
		}
		bfd.setFile(image);
		GenericPersistence.merge(bfd);
	}
	public static BerkasFotoDosen getFotoDosen(DosenKaryawan d){
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("dosen", d));
		List<BerkasFotoDosen> lg = GenericPersistence.findList(BerkasFotoDosen.class, lc);
		BerkasFotoDosen bfd = null;
		if (lg.size()>0){
			bfd = lg.get(0);
		}
		return bfd;
	}
	

}
