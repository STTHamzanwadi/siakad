package org.pdtyph.util;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.yph.jee.persistence.HibernateUtil;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

public class GenericUtilities {
	public static final Locale LOCALE = new Locale("id");
	/**
	 * 
	 * 
	 * public method to get the database Date and Time
	 */

	public static Timestamp getCurrentDBTime() {
		SQLQuery sqlQuery = null;
		String queryString = null;
		Timestamp dbTimeStamp = null;
		queryString = "SELECT now() param FROM DUAL";
		//SessionFactory sessionFactory = HibernateUtil.configureSessionFactory();
	    Session session = HibernateUtil.getSession();
	    session.beginTransaction();
		sqlQuery = session.createSQLQuery(queryString);
		sqlQuery.addScalar("param",
					org.hibernate.type.TimestampType.INSTANCE);
		dbTimeStamp = (Timestamp) sqlQuery.uniqueResult();
		return dbTimeStamp;
	}
	public static Date getCurrentDBDate() {
		SQLQuery sqlQuery = null;
		String queryString = null;
		Date dbDate = null;
		queryString = "SELECT curdate() param FROM DUAL";
		Session session = HibernateUtil.getSession();
	    session.beginTransaction();
		sqlQuery = session.createSQLQuery(queryString);
		sqlQuery.addScalar("param",
					org.hibernate.type.DateType.INSTANCE);
		dbDate = (Date) sqlQuery.uniqueResult();
		return dbDate;

	}
	public static String getCurrentDBDateStr(){
		//GregorianCalendar c = new GregorianCalendar();
		//c.setTime(getCurrentDBDate());
		DateFormat f = new SimpleDateFormat("dd MMM YYYY", LOCALE);
		String s = f.format(getCurrentDBDate());
		return s;
	}
	public static String getCurrentDBTimeStamp(){
		//GregorianCalendar c = new GregorianCalendar();
		//c.setTime(getCurrentDBDate());
		DateFormat f = new SimpleDateFormat("dd MMMM YYYY HH:mm", LOCALE);
		String s = f.format(getCurrentDBTime());
		return s;
	}
	public static int getCurrentYear(){
		Calendar c  = Calendar.getInstance();
		c.setTime(getCurrentDBDate());
		int thn = c.get(java.util.Calendar.YEAR);
		return thn;
	}
	public static int getCurrentMonth(){
		Calendar c  = Calendar.getInstance();
		c.setTime(getCurrentDBDate());
		int mth = c.get(java.util.Calendar.MONTH);
		return mth;
	}
	public static String getLongFormattedDate(Date dt){
		if (dt!=null) {
			DateFormat f = new SimpleDateFormat("dd MMMM YYYY", LOCALE);
			return f.format(dt);
		}
		return "-";
	}
	public static String getMediumFormattedDate(Date dt){
		DateFormat f = new SimpleDateFormat("dd MMM YYYY", LOCALE);
		return f.format(dt);
	}
	public static String getDay(Date dt){
		DateFormat f = new SimpleDateFormat("EEEE", LOCALE);
		return f.format(dt);
	}
	public static String getDateHrMi(Date dt){
		DateFormat f = new SimpleDateFormat("dd MMM YYYY HH:mm", LOCALE);
		return f.format(dt);
	}
	public static String getHrMi(Date dt){
		DateFormat f = new SimpleDateFormat("HH:mm", LOCALE);
		return f.format(dt);
	}
	public static String getCurrentTA(){
		Calendar c  = Calendar.getInstance();
		c.setTime(getCurrentDBDate());
		int thn = c.get(java.util.Calendar.YEAR);
		String thns = String.valueOf(thn)+"-"+(String.valueOf(thn+1));
		return thns;
	}
	
	public static String genapGanjilFromInt(int i){
		if ((i % 2) == 0){
			return "GENAP";
		}
		return "GANJIL";
	}
	public static int genapGanjil12FromInt(int i){
		if ((i % 2) == 0){
			return 2;
		}
		return 1;
	}
	public static int genapGanjilToInt(String s){
		if (s == "GENAP"){
			return 2;
		}
		if (s == "GANJIL"){
			return 1;
		}
		return 0;
	}
	public boolean isGenap(int i){
		if ((i % 2) == 0){
			return true;
		}
		return false;
	}
	
	
	public static List<Date> getDates(Date tglMulai, Date tglAkhir){
		List<Date> dates = new ArrayList<>();
		Calendar c,e;
		c = Calendar.getInstance();
		c.setTime(tglMulai);
		e = Calendar.getInstance();
		e.setTime(tglAkhir);
		e.add(Calendar.DATE, 1);
		while (c.before(e)){
			dates.add(c.getTime());
			c.add(Calendar.DATE, 1);
		}
		return dates;
	}
	
}
