package org.yph.jee.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;

public class GenericPersistence {
	
	public static <T> List<T> findList(Class<T> arg0){
		return findList(arg0, null, null);
	}
	public static <T> List<T> findList(Class<T> arg0, List<Criterion> critList){
	
	    return findList(arg0, critList, null);
	}
	public static <T> List<T> findList(Class<T> arg0, List<Criterion> critList,String[] map){
	    Session session = HibernateUtil.getSession();
	    if (!session.getTransaction().isActive()){
	    	session.beginTransaction();
	    }
	    Criteria c = session.createCriteria(arg0);
	    if (critList!=null){
	    	for (Criterion criterion : critList) {
				c.add(criterion);
			}
	    }
	    if (map!=null){
	    	for (String string : map) {
				c.createAlias(string, string);
			}
	    }
	    @SuppressWarnings("unchecked")
		List<T> l = c.list();
	    session.close();
	    return l;
	}
	
	public static void saveAndFlush(Object a){
		Session session = HibernateUtil.getSession();
	    //Transaction tx = session.beginTransaction();
		session.save(a);
		//tx.commit();
	    session.flush();
	    //session.refresh(a);
	   
	}
	
	public static void merge(Object a){
		Session session = HibernateUtil.getSession();
	    Transaction tx = session.beginTransaction();
	    session.merge(a);
	    tx.commit();
	}
	
	public static void delete(Object a){
		Session session = HibernateUtil.getSession();
	    Transaction tx = session.beginTransaction();
	    session.delete(a);
	    tx.commit();
	}
	public static void closeSession(){
		Session session = HibernateUtil.getSession();
		session.flush();
		session.close();
		
	}
	
	
}
