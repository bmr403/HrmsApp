package eProject.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import eProject.domain.master.GemsUserMaster;
import eProject.utility.DataAccessLayerException;

import java.util.List;

public abstract class ErpAbstractDao {

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected final Log logger = LogFactory.getLog(ErpAbstractDao.class);

	public GemsUserMaster getLoginVerificationByEmail(GemsUserMaster gemsUserMaster) {
		Session session = sessionFactory.openSession();
		List list = null;
		try {

			Criteria criteria = session.createCriteria(GemsUserMaster.class);

			if ((gemsUserMaster.getUserName() != null) && (gemsUserMaster.getUserPassword() != null)) {
				criteria.add(Restrictions.eq("userName", gemsUserMaster.getUserName()).ignoreCase());
				criteria.add(Restrictions.eq("userPassword", gemsUserMaster.getUserPassword()));

			}

			list = criteria.list();

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (HibernateException e) {
					e.printStackTrace();
				}
			}
		}

		if (list.size() != 0)
			return (GemsUserMaster) list.get(0);
		else
			return null;
	}

	protected void saveOrUpdate(Object obj) {
		Session session = sessionFactory.openSession();
		try {

			session.saveOrUpdate(obj);
			session.flush();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
	}

	protected void delete(Object obj) {
		Session session = sessionFactory.openSession();
		try {

			session.delete(obj);
			session.flush();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
	}

	protected Object find(Class clazz, Integer id) {
		Object obj = null;
		Session session = sessionFactory.openSession();
		try {

			obj = session.get(clazz, id);

		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return obj;
	}

	protected Object checkUniqueCode(Class clazz, DetachedCriteria objectCriteria) {
		Object obj = null;

		Session session = sessionFactory.openSession();
		try {

			Criteria crit = objectCriteria.getExecutableCriteria(session);
			List list = crit.list();
			if (!list.isEmpty()) {
				obj = list.get(0);
			}

		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}

		return obj;
	}

	protected Object getMaxCode(Class clazz, String maxColumnName) {
		Object obj = null;

		Session session = sessionFactory.openSession();
		try {

			Criteria cr = session.createCriteria(clazz);
			cr.setProjection(Projections.max(maxColumnName));
			List list = cr.list();
			if (!list.isEmpty()) {
				obj = list.get(0);
			}

		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}

		return obj;
	}

	protected Object checkObjectByParameter(Class clazz, DetachedCriteria objectCriteria) {
		Object obj = null;

		Session session = sessionFactory.openSession();
		try {

			Criteria crit = objectCriteria.getExecutableCriteria(session);
			List list = crit.list();
			obj = list.get(0);
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}

		return obj;
	}

	protected List findAll(Class clazz) {
		List objects = null;
		Session session = sessionFactory.openSession();
		try {

			Query query = session.createQuery("from " + clazz.getName());
			objects = query.list();

		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return objects;
	}

	protected int getObjectListCount(Class clazz, DetachedCriteria objectCriteria) {
		Integer resultTotal = 0;
		List rowlist = null;
		Session session = sessionFactory.openSession();
		try {

			Criteria crit = objectCriteria.getExecutableCriteria(session);
			crit.setProjection(Projections.rowCount());
			rowlist = crit.list();
			if (!rowlist.isEmpty()) {
				Long resultTotalValue = (Long) rowlist.get(0);
				resultTotal = new Integer(resultTotalValue.intValue());
				logger.info("Total records: " + resultTotal);
			}

		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return resultTotal.intValue();

	}

	protected List getObjectList(Class clazz, DetachedCriteria objectCriteria) {
		List list = null;
		Session session = sessionFactory.openSession();
		try {
			Criteria crit = objectCriteria.getExecutableCriteria(session);
			list = crit.list();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return list;

	}

	protected List getObjectListByRange(Class clazz, DetachedCriteria objectCriteria, int start, int limit) {
		List list = null;
		Session session = sessionFactory.openSession();
		try {

			Criteria crit = objectCriteria.getExecutableCriteria(session);
			crit.setFirstResult(start);
			crit.setMaxResults(limit);
			list = crit.list();

		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return list;

	}

	protected List getAllObjectList(Class clazz, DetachedCriteria objectCriteria) {
		List list = null;
		Session session = sessionFactory.openSession();
		try {
			logger.info("In abstract DAO getAllObjectList method");
			Criteria crit = objectCriteria.getExecutableCriteria(session);
			list = crit.list();

		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return list;

	}

	protected void handleException(HibernateException e) throws DataAccessLayerException {

		throw new DataAccessLayerException(e);
	}

	protected Object saveOrUpdatewithReturn(Object obj) {
		Session session = sessionFactory.openSession();
		try {
			logger.info("In abstract DAO saveOrUpdatewithReturn method");
			session.saveOrUpdate(obj);
			session.flush();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return obj;
	}

	public void saveAll(List item) throws DataAccessLayerException {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		try {
			for (int i = 0; i < item.size(); i++) {
				session.saveOrUpdate(item.get(i));
				if (i % 20 == 0) {
					session.flush();
					session.clear();
				}
			}
			transaction.commit();
		} catch (final HibernateException e) {
			transaction.rollback();
			throw new DataAccessLayerException(e);
		} finally {
			session.close();
		}
	}

	/*
	 * protected void startOperation() throws HibernateException { Session
	 * session = sessionFactory.openSession(); Transaction tx =
	 * session.beginTransaction(); }
	 */
}
