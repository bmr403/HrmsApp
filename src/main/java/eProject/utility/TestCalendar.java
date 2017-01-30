package eProject.utility;

import java.nio.charset.Charset;

import org.springframework.security.crypto.codec.Base64;

public class TestCalendar {

	public static void main(String[] args) {

		String token = "BpaMyCompany";
		HrKeyStoreUtility hrKeyStore = new HrKeyStoreUtility();
		String encryptedPassword = hrKeyStore.getEncryptedStringValue(token, "E");
		System.out.println(encryptedPassword);
	}

}
