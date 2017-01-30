package eProject.service.customer;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import eProject.domain.customer.GemsCustomerContact;
import eProject.domain.customer.GemsCustomerDocument;
import eProject.domain.customer.GemsCustomerMaster;

public interface CustomerService {

	/*
	 * Gems Customer Master Methods
	 */

	public int getGemsCustomerMasterFilterCount(GemsCustomerMaster gemsCustomerMaster);

	public List getGemsCustomerMasterList(int start, int limit, GemsCustomerMaster gemsCustomerMaster);

	public void saveGemsCustomerMaster(GemsCustomerMaster gemsCustomerMaster);

	public void removeGemsCustomerMaster(GemsCustomerMaster gemsCustomerMaster);

	public GemsCustomerMaster getGemsCustomerMaster(Integer Id);

	public GemsCustomerMaster getGemsCustomerMasterByCode(GemsCustomerMaster gemsCustomerMaster);
	
	public List getAllGemsCustomerMasterList(GemsCustomerMaster gemsCustomerMaster);

	/*
	 * End of Gems Customer Master Methods
	 */

	/*
	 * Gems Customer Contact Methods
	 */
	public int getGemsCustomerContactFilterCount(GemsCustomerContact gemsCustomerContact);

	public List getGemsCustomerContactList(int start, int limit, GemsCustomerContact gemsCustomerContact);

	public void saveGemsCustomerContact(GemsCustomerContact gemsCustomerContact);

	public void removeGemsCustomerContact(GemsCustomerContact gemsCustomerContact);

	public GemsCustomerContact getGemsCustomerContact(Integer Id);

	public List getAllGemsCustomerContactList(GemsCustomerContact gemsCustomerContact);

	/*
	 * Gems Customer Document Methods
	 */

	public int getGemsCustomerDocumentFilterCount(GemsCustomerDocument gemsCustomerDocument);

	public List getGemsCustomerDocumentList(int start, int limit, GemsCustomerDocument gemsCustomerDocument);

	public void saveGemsCustomerDocument(GemsCustomerDocument gemsCustomerDocument);

	public void removeGemsCustomerDocument(GemsCustomerDocument gemsCustomerDocument);

	public GemsCustomerDocument getGemsCustomerDocument(Integer Id);

	public List getAllGemsCustomerDocumentList(GemsCustomerDocument gemsCustomerDocument);

}
