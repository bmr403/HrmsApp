/**
 * 
 */
package eProject.dao.customer;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import eProject.dao.ErpAbstractDao;
import eProject.domain.customer.GemsCustomerContact;
import eProject.domain.customer.GemsCustomerDocument;
import eProject.domain.customer.GemsCustomerMaster;

@Repository("CustomerDao")
public class CustomerDao extends ErpAbstractDao {

	public CustomerDao() {
		super();
	}

	/*
	 * Gems Customer Master Methods
	 */

	private DetachedCriteria createGemsCustomerMasterCriteria(GemsCustomerMaster gemsCustomerMaster,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsCustomerMaster.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsCustomerMaster.getGemsCustomerCode() != null) {
				criteria.add(Restrictions.eq("gemsCustomerCode",
						"" + gemsCustomerMaster.getGemsCustomerCode().toUpperCase() + ""));
			}

		} else {
			if (gemsCustomerMaster.getGemsCustomerCode() != null) {
				criteria.add(Restrictions.like("gemsCustomerCode", "%" + gemsCustomerMaster.getGemsCustomerCode() + "%")
						.ignoreCase());
			}
			if (gemsCustomerMaster.getGemsCustomerName() != null) {
				criteria.add(Restrictions.like("gemsCustomerName", "%" + gemsCustomerMaster.getGemsCustomerName() + "%")
						.ignoreCase());
			}

		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsCustomerMaster.getGemsOrganisation()));

		return criteria;

	}

	public int getGemsCustomerMasterFilterCount(GemsCustomerMaster gemsCustomerMaster) {
		DetachedCriteria criteria = createGemsCustomerMasterCriteria(gemsCustomerMaster, "");
		return super.getObjectListCount(GemsCustomerMaster.class, criteria);
	}

	public List getGemsCustomerMasterList(int start, int limit, GemsCustomerMaster gemsCustomerMaster) {
		DetachedCriteria criteria = createGemsCustomerMasterCriteria(gemsCustomerMaster, "");
		criteria.addOrder(Order.desc("gemsCustomerMasterId"));
		return super.getObjectListByRange(GemsCustomerMaster.class, criteria, start, limit);
	}
	public List getAllGemsCustomerMasterList(GemsCustomerMaster gemsCustomerMaster) {
		DetachedCriteria criteria = createGemsCustomerMasterCriteria(gemsCustomerMaster, "");
		criteria.addOrder(Order.desc("gemsCustomerMasterId"));
		return super.getAllObjectList(GemsCustomerMaster.class, criteria);
	}

	public void saveGemsCustomerMaster(GemsCustomerMaster gemsCustomerMaster) {
		super.saveOrUpdate(gemsCustomerMaster);
	}

	public void removeGemsCustomerMaster(GemsCustomerMaster gemsCustomerMaster) {
		super.delete(gemsCustomerMaster);
	}

	public GemsCustomerMaster getGemsCustomerMaster(Integer Id) {
		return (GemsCustomerMaster) super.find(GemsCustomerMaster.class, Id);
	}

	public GemsCustomerMaster getGemsCustomerMasterByCode(GemsCustomerMaster gemsCustomerMaster) {
		DetachedCriteria criteria = createGemsCustomerMasterCriteria(gemsCustomerMaster, "exact");
		return (GemsCustomerMaster) super.checkUniqueCode(GemsCustomerMaster.class, criteria);
	}

	/*
	 * End of Gems Customer Master Methods
	 */

	/*
	 * Gems Customer Contact Methods
	 */

	private DetachedCriteria createGemsCustomerContactCriteria(GemsCustomerContact gemsCustomerContact,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsCustomerContact.class);
		criteria.add(Restrictions.eq("gemsCustomerMaster", gemsCustomerContact.getGemsCustomerMaster()));

		return criteria;

	}

	public int getGemsCustomerContactFilterCount(GemsCustomerContact gemsCustomerContact) {
		DetachedCriteria criteria = createGemsCustomerContactCriteria(gemsCustomerContact, "");
		return super.getObjectListCount(GemsCustomerContact.class, criteria);
	}

	public List getGemsCustomerContactList(int start, int limit, GemsCustomerContact gemsCustomerContact) {
		DetachedCriteria criteria = createGemsCustomerContactCriteria(gemsCustomerContact, "");
		return super.getObjectListByRange(GemsCustomerContact.class, criteria, start, limit);
	}

	public List getAllGemsCustomerContactList(GemsCustomerContact gemsCustomerContact) {
		DetachedCriteria criteria = createGemsCustomerContactCriteria(gemsCustomerContact, "");
		return super.getAllObjectList(GemsCustomerContact.class, criteria);
	}

	public void saveGemsCustomerContact(GemsCustomerContact gemsCustomerContact) {
		super.saveOrUpdate(gemsCustomerContact);
	}

	public void removeGemsCustomerContact(GemsCustomerContact gemsCustomerContact) {
		super.delete(gemsCustomerContact);
	}

	public GemsCustomerContact getGemsCustomerContact(Integer Id) {
		return (GemsCustomerContact) super.find(GemsCustomerContact.class, Id);
	}

	/*
	 * Gems Customer Document Methods
	 */

	private DetachedCriteria createGemsCustomerDocumentCriteria(GemsCustomerDocument gemsCustomerDocument,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsCustomerDocument.class);
		criteria.add(Restrictions.eq("gemsCustomerMaster", gemsCustomerDocument.getGemsCustomerMaster()));

		return criteria;

	}

	public int getGemsCustomerDocumentFilterCount(GemsCustomerDocument gemsCustomerDocument) {
		DetachedCriteria criteria = createGemsCustomerDocumentCriteria(gemsCustomerDocument, "");
		return super.getObjectListCount(GemsCustomerDocument.class, criteria);
	}

	public List getGemsCustomerDocumentList(int start, int limit, GemsCustomerDocument gemsCustomerDocument) {
		DetachedCriteria criteria = createGemsCustomerDocumentCriteria(gemsCustomerDocument, "");
		return super.getObjectListByRange(GemsCustomerDocument.class, criteria, start, limit);
	}

	public List getAllGemsCustomerDocumentList(GemsCustomerDocument gemsCustomerDocument) {
		DetachedCriteria criteria = createGemsCustomerDocumentCriteria(gemsCustomerDocument, "");
		return super.getAllObjectList(GemsCustomerDocument.class, criteria);
	}

	public void saveGemsCustomerDocument(GemsCustomerDocument gemsCustomerDocument) {
		super.saveOrUpdate(gemsCustomerDocument);
	}

	public void removeGemsCustomerDocument(GemsCustomerDocument gemsCustomerDocument) {
		super.delete(gemsCustomerDocument);
	}

	public GemsCustomerDocument getGemsCustomerDocument(Integer Id) {
		return (GemsCustomerDocument) super.find(GemsCustomerDocument.class, Id);
	}

}
