package eProject.utility;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public final class ConstantVariables {

	private ConstantVariables() {
	}

	public static final String APPLIED = "Applied";
	public static final String APPROVED = "Approved";
	public static final String REJECTED = "Rejected";
	public static final String PUBLISHED = "Published";

	public static final String IN_PROGRESS = "In Progress";
	public static final String EMPLOYEE = "Employee";
	public static final String HR = "HR";
	public static final String UPDATED_SUCCESS = "Record Updated Successfully";

	public static final String PASSPORT_VALIDITY = "Your passport validity is going to expired";
	public static final String VISA_VALIDITY = "Your visa validity is going to expired";

	/**
	 * gets the AES encryption key. In your actual programs, this should be
	 * safely stored.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static SecretKey getSecretEncryptionKey() throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(128); // The AES key size in number of bits
		SecretKey secKey = generator.generateKey();
		return secKey;
	}

}
