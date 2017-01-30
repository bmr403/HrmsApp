package eProject.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class HrKeyStoreUtility {

	public String getEncryptedStringValue(String inputString, String enCryptDeCryptFlag) {
		String returnedString = "";
		try {
			KeyStore keyStore = KeyStore.getInstance("JCEKS");
			String uploadDirectoryBase = System.getProperty("catalina.base");
			String keyLocation = uploadDirectoryBase + File.separator + "webapps" + File.separator + "upload"	+ File.separator;
			// String keyLocation = "E:\\Tomcat 8.0\\webapps\\upload\\";

			FileInputStream stream = new FileInputStream(keyLocation + "mykeystore.jks");
			keyStore.load(stream, "bpahrms".toCharArray());
			Key key = keyStore.getKey("mykey", "bpahrms".toCharArray());

			if (enCryptDeCryptFlag.equalsIgnoreCase("E")) {
				// Encrypt Data
				returnedString = encryptWithAESKey(inputString, key.getEncoded());
				System.out.println("Encrypted Data : " + returnedString);
			} else {
				// Decrypt Data
				returnedString = decryptWithAESKey(inputString, key.getEncoded());
				System.out.println("Decrypted Data : " + decryptWithAESKey(inputString, key.getEncoded()));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnedString;
	}

	public static String encryptWithAESKey(String data, byte[] key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		SecretKey secKey = new SecretKeySpec(key, "AES");

		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.ENCRYPT_MODE, secKey);
		byte[] newData = cipher.doFinal(data.getBytes());

		return Base64.encodeBase64String(newData);
	}

	public static String decryptWithAESKey(String inputData, byte[] key) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES");
		SecretKey secKey = new SecretKeySpec(key, "AES");

		cipher.init(Cipher.DECRYPT_MODE, secKey);
		byte[] newData = cipher.doFinal(Base64.decodeBase64(inputData.getBytes()));
		return new String(newData);

	}

}
