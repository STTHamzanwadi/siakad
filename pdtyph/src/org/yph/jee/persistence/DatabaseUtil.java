package org.yph.jee.persistence;

import java.util.Calendar;
import java.util.Random;

import org.hibernate.Session;
import org.pdtyph.entity.Type;
import org.pdtyph.entity.Workout;

public class DatabaseUtil
{
	private static Type defaultType;

	public static void insertExampleTypes()
	{
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();

		if (!session.getTransaction().isActive())
			session.beginTransaction();

		Type type;

		type = new Type();
		type.setTitle("Running");
		session.save(type);

		defaultType = type;
		System.err.println("Default type id : " + defaultType.getId());

		type = new Type();
		type.setTitle("MTB");
		session.save(type);

		type = new Type();
		type.setTitle("Trecking");
		session.save(type);

		type = new Type();
		type.setTitle("Swimming");
		session.save(type);

		type = new Type();
		type.setTitle("Orienteering");
		session.save(type);

		type = new Type();
		type.setTitle("Football");
		session.save(type);

	}

	public static void insertExampleData(int trainingsToLoad)
	{
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();

		if (!session.getTransaction().isActive())
			session.beginTransaction();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);

		String[] titles = new String[] { "A short easy one", "intervals", "very long", "just shaking legs after work", "long one with Paul", "test run" };

		c.add(Calendar.DATE, -trainingsToLoad);

		Random rnd = new Random();

		Workout r;

		for (int i = 0; i < trainingsToLoad; i++)
		{
			r = new Workout();
			c.set(Calendar.HOUR_OF_DAY, 12 + (rnd.nextInt(11) - rnd.nextInt(11)));
			r.setDate(c.getTime());
			r.setTitle(titles[rnd.nextInt(titles.length)]);
			r.setKilometers(Math.round(rnd.nextFloat() * 30));
			r.setTrainingType(defaultType);
			session.save(r);
			c.add(Calendar.DATE, 1);
		}

	}
}
