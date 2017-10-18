package org.stth.siak.util;


import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.stth.jee.persistence.HibernateUtil;
import org.stth.siak.enumtype.Semester;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;

public class GeneralUtilities {
	public static final Locale LOCALE = new Locale("id");
	public static final String[] AGAMA = new String[] {"ISLAM","HINDU","BUDDHA","KATOLIK","PROTESTAN"};
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
		try {

			session.beginTransaction();
			sqlQuery = session.createSQLQuery(queryString);
			sqlQuery.addScalar("param",
					org.hibernate.type.TimestampType.INSTANCE);
			dbTimeStamp = (Timestamp) sqlQuery.uniqueResult();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return dbTimeStamp;
	}
	public static Date getCurrentDBDate() {
		SQLQuery sqlQuery = null;
		String queryString = null;
		Date dbDate = null;
		queryString = "SELECT curdate() param FROM DUAL";
		Session session = HibernateUtil.getSession();
		try {
			session.beginTransaction();
			sqlQuery = session.createSQLQuery(queryString);
			sqlQuery.addScalar("param",
					org.hibernate.type.DateType.INSTANCE);
			dbDate = (Date) sqlQuery.uniqueResult();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
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
	public static int getCurrentYearLocal(){
		Calendar c  = Calendar.getInstance();
		c.setTime(new Date());
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
	public static String getLongFormattedDate2(Date dt){
		if (dt!=null) {
			DateFormat f = new SimpleDateFormat("dd MMMM yyyy", LOCALE);
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
	public static String getCurTA(){
		Calendar c  = Calendar.getInstance();
		c.setTime(new Date());
		int thn = c.get(java.util.Calendar.YEAR);
		String thns;
		if (c.get(java.util.Calendar.MONTH)<7) {
			thns = String.valueOf(thn-1)+"-"+(String.valueOf(thn));
		}else{
			thns = String.valueOf(thn)+"-"+(String.valueOf(thn+1));
		}
		
		return thns;
	}
	public static Semester getCurSMS(){
		Calendar c  = Calendar.getInstance();
		c.setTime(new Date());
		int bulan = c.get(java.util.Calendar.MONTH);
		System.out.println(bulan);
		if (bulan<7) {
			return Semester.GENAP;
		}
		return Semester.GANJIL;
	}
	
	
	public static Semester genapGanjilEnumFromInt(int i){
		if ((i % 2) == 0){
			return Semester.GENAP;
		}
		return Semester.GANJIL;
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
	
	public static Date truncateDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	public static Date truncateNextDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	public static int getBit(int n, int pos) {
		return (n >> pos) & 1;
	}
	
	public static Container createContainerFromEnumClass(Class<? extends Enum<?>> enumClass) {
		LinkedHashMap<Enum<?>, String> enumMap = new LinkedHashMap<Enum<?>, String>();
		for (Object enumConstant : enumClass.getEnumConstants()) {
			enumMap.put((Enum<?>) enumConstant, enumConstant.toString());
		}

		return createContainerFromMap(enumMap);
	}
	public static String CAPTION_PROPERTY_NAME = "caption";

	public static Container createContainerFromMap(Map<?, String> hashMap) {
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(CAPTION_PROPERTY_NAME, String.class, "");

		Iterator<?> iter = hashMap.keySet().iterator();
		while(iter.hasNext()) {
			Object itemId = iter.next();
			container.addItem(itemId);
			container.getItem(itemId).getItemProperty(CAPTION_PROPERTY_NAME).setValue(hashMap.get(itemId));
		}

		return container;
	}
	
	public static void writeSheet(Workbook wb, String tabName, String[][] data) 
	{
		//Create new workbook and tab

		Sheet sheet = wb.createSheet(tabName);

		//Create 2D Cell Array
		Row[] row = new Row[data.length];
		Cell[][] cell = new Cell[row.length][];

		//Define and Assign Cell Data from Given
		for(int i = 0; i < row.length; i ++)
		{
			row[i] = sheet.createRow(i);
			cell[i] = new Cell[data[i].length];

			for(int j = 0; j < cell[i].length; j ++)
			{
				cell[i][j] = row[i].createCell(j);
				cell[i][j].setCellValue(data[i][j]);
			}

		}

	}
	public static void createFileExcel(Workbook wb, String fileName) throws IOException{
		FileOutputStream fileOut = new FileOutputStream(fileName);
		wb.write(fileOut);
		fileOut.close();
		System.out.println("File exported successfully");
	}

}
