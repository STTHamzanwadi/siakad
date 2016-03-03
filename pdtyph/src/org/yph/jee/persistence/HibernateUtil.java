package org.yph.jee.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.hibernate4.encryptor.HibernatePBEEncryptorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil
{
	private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	private static Session session;
	static void configureSessionFactory()
	{
		try
		{
			logger.debug("Initializing HibernateUtil");
			
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword("yph");
			encryptor.setAlgorithm("PBEWITHMD5ANDDES");
//			encryptor.setKeyObtentionIterations(379);
			Configuration configuration = new Configuration();
			configuration.configure();
			String pass="";
			try {
				pass = encryptor.decrypt(configuration.getProperty("hibernate.connection.password"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			configuration.setProperty("hibernate.connection.password",pass);
			HibernatePBEEncryptorRegistry registry =
				      HibernatePBEEncryptorRegistry.getInstance();
			registry.registerPBEStringEncryptor("configurationHibernateEncryptor", encryptor);
			serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);

			
		}
		catch (Throwable e)
		{
			logger.error(e.toString());
			throw new ExceptionInInitializerError(e);
		}
	}

	public static SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}
	
	public static Session getSession(){
		if (sessionFactory==null) {
			configureSessionFactory();
			logger.debug(" "+sessionFactory);
		}
		if (session == null) {
			session = sessionFactory.getCurrentSession();
		}
		if (!session.isOpen()){
			session = sessionFactory.openSession();
		}
		return session;
	}
	
	
}
