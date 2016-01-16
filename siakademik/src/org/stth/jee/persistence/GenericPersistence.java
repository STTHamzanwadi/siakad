package org.stth.jee.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;

public class GenericPersistence
{
  public static <T> List<T> findList(Class<T> arg0)
  {
    return findList(arg0, null, null);
  }
  
  public static <T> List<T> findList(Class<T> arg0, List<Criterion> critList) {
    return findList(arg0, critList, null);
  }
  
  @SuppressWarnings("unchecked")
public static <T> List<T> findList(Class<T> arg0, List<Criterion> critList, String[] map) {
    Session session = HibernateUtil.getSession();
    List<T> l = null;
    try { 
      Transaction tx;
      if ((session.getTransaction() != null) && 
        (session.getTransaction().isActive())) {
        tx = session.getTransaction();
      } else {
        tx = session.beginTransaction();
      }
      try {
        Criteria c = session.createCriteria(arg0);
        if (critList != null) {
          for (Criterion criterion : critList) {
            c.add(criterion);
          }
        }
        if (map != null) { String[] arrayOfString;
          int j = (arrayOfString = map).length; for (int i = 0; i < j; i++) { String string = arrayOfString[i];
            c.createAlias(string, string);
          }
        }
        l = c.list();
      } catch (Exception e) {
        tx.rollback();
        e.printStackTrace();
        throw e;
      }
    } finally {
      HibernateUtil.closeSession(); } HibernateUtil.closeSession();
    
    return l;
  }
  
  public static void saveAndFlush(Object a) {
    Session session = HibernateUtil.getSession();
    try { 
      Transaction tx;
      if ((session.getTransaction() != null) && 
        (session.getTransaction().isActive())) {
        tx = session.getTransaction();
      } else {
        tx = session.beginTransaction();
      }
      try {
        session.save(a);
        tx.commit();
        session.flush();
      } catch (Exception e) {
        tx.rollback();
        e.printStackTrace();
        throw e;
      }
    } finally {
      HibernateUtil.closeSession(); } HibernateUtil.closeSession();
  }
  

  public static void merge(Object a)
  {
    Session session = HibernateUtil.getSession();
    try { 
      Transaction tx;
      if ((session.getTransaction() != null) && 
        (session.getTransaction().isActive())) {
        tx = session.getTransaction();
      } else {
        tx = session.beginTransaction();
      }
      try {
        session.merge(a);
        tx.commit();
      } catch (Exception e) {
        tx.rollback();
        e.printStackTrace();
        throw e;
      }
    } finally {
      HibernateUtil.closeSession(); } HibernateUtil.closeSession();
  }
  
  public static void delete(Object a)
  {
    Session session = HibernateUtil.getSession();
    try { 
      Transaction tx;
      if ((session.getTransaction() != null) && 
        (session.getTransaction().isActive())) {
        tx = session.getTransaction();
      } else {
        tx = session.beginTransaction();
      }
      try {
        session.delete(a);
        tx.commit();
      } catch (Exception e) {
        tx.rollback();
        e.printStackTrace();
        throw e;
      }
    } finally {
      HibernateUtil.closeSession(); } HibernateUtil.closeSession();
  }
  
  public static void closeSession() {
    Session session = HibernateUtil.getSession();
    session.flush();
    session.close();
  }
}
