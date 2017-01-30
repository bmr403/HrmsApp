package eProject.service.customer;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eProject.dao.customer.CustomerDao;
import eProject.domain.customer.GemsCustomerContact;
import eProject.domain.customer.GemsCustomerDocument;
import eProject.domain.customer.GemsCustomerMaster;

@Service("customerService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDao customerDao;

	public CustomerServiceImpl() {
	}

	/*
	 * Gems Customer Master Methods
	 */

	public int getGemsCustomerMasterFilterCount(GemsCustomerMaster gemsCustomerMaster) {
		return customerDao.getGemsCustomerMasterFilterCount(gemsCustomerMaster);
	}

	public List getGemsCustomerMasterList(int start, int limit, GemsCustomerMaster gemsCustomerMaster) {
		return customerDao.getGemsCustomerMasterList(start, limit, gemsCustomerMaster);
	}

	public void saveGemsCustomerMaster(GemsCustomerMaster gemsCustomerMaster) {
		customerDao.saveGemsCustomerMaster(gemsCustomerMaster);
	}

	public void removeGemsCustomerMaster(GemsCustomerMaster gemsCustomerMaster) {
		customerDao.removeGemsCustomerMaster(gemsCustomerMaster);
	}

	public GemsCustomerMaster getGemsCustomerMaster(Integer Id) {
		return customerDao.getGemsCustomerMaster(Id);
	}

	public GemsCustomerMaster getGemsCustomerMasterByCode(GemsCustomerMaster gemsCustomerMaster) {
		return customerDao.getGemsCustomerMasterByCode(gemsCustomerMaster);
	}
	public List getAllGemsCustomerMasterList(GemsCustomerMaster gemsCustomerMaster) {
		return customerDao.getAllGemsCustomerMasterList(gemsCustomerMaster);
	}

	/*
	 * End of Gems Customer Master Methods
	 */

	/*
	 * Gems Customer Contact Methods
	 */
	public int getGemsCustomerContactFilterCount(GemsCustomerContact gemsCustomerContact) {
		return customerDao.getGemsCustomerContactFilterCount(gemsCustomerContact);
	}

	public List getGemsCustomerContactList(int start, int limit, GemsCustomerContact gemsCustomerContact) {
		return customerDao.getGemsCustomerContactList(start, limit, gemsCustomerContact);
	}

	public void saveGemsCustomerContact(GemsCustomerContact gemsCustomerContact) {
		customerDao.saveGemsCustomerContact(gemsCustomerContact);
	}

	public void removeGemsCustomerContact(GemsCustomerContact gemsCustomerContact) {
		customerDao.removeGemsCustomerContact(gemsCustomerContact);
	}

	public GemsCustomerContact getGemsCustomerContact(Integer Id) {
		return customerDao.getGemsCustomerContact(Id);
	}

	public List getAllGemsCustomerContactList(GemsCustomerContact gemsCustomerContact) {
		return customerDao.getAllGemsCustomerContactList(gemsCustomerContact);
	}

	/*
	 * Gems Customer Document Methods
	 */

	public int getGemsCustomerDocumentFilterCount(GemsCustomerDocument gemsCustomerDocument) {
		return customerDao.getGemsCustomerDocumentFilterCount(gemsCustomerDocument);
	}

	public List getGemsCustomerDocumentList(int start, int limit, GemsCustomerDocument gemsCustomerDocument) {
		return customerDao.getGemsCustomerDocumentList(start, limit, gemsCustomerDocument);
	}

	public void saveGemsCustomerDocument(GemsCustomerDocument gemsCustomerDocument) {
		customerDao.saveGemsCustomerDocument(gemsCustomerDocument);
	}

	public void removeGemsCustomerDocument(GemsCustomerDocument gemsCustomerDocument) {
		customerDao.removeGemsCustomerDocument(gemsCustomerDocument);
	}

	public GemsCustomerDocument getGemsCustomerDocument(Integer Id) {
		return customerDao.getGemsCustomerDocument(Id);
	}

	public List getAllGemsCustomerDocumentList(GemsCustomerDocument gemsCustomerDocument) {
		return customerDao.getAllGemsCustomerDocumentList(gemsCustomerDocument);
	}
}
