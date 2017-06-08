package zupkeyvault.crypt;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;

import zupkeyvault.helper.AzureHelper;

public class ClientSecretKeyVaultCredential extends KeyVaultCredentials {


	@Override
	public String doAuthenticate(String pAuthorization, String pResource, String scope) {
		String authorization = pAuthorization;
		String resource = pResource;
		String token = null;
		try {
			token = AzureHelper.getAccessTokenAsString(authorization, resource);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return token;
	}

}
