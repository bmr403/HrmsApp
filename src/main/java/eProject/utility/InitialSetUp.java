package eProject.utility;

public class InitialSetUp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try
		{
			
			
			HrKeyStoreUtility hrKeyStoreUtility = new HrKeyStoreUtility();		
			String userPassWord = "BpaMyCompany";

			String encryptedPasswordString = hrKeyStoreUtility.getEncryptedStringValue(userPassWord,
					"E");
			System.out.println(encryptedPasswordString);
		}
		catch(Exception ex)
		{
			
		}
		


	}

}
