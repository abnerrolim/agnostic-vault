package zupkeyvault.crypt;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.ClientCredential;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;

public class ClientSecretKeyVaultCredential extends KeyVaultCredentials {


	private final static String AUTH_URI = "https://login.windows.net/common/oauth2/authorize";

	@Value("${zup.keyvault.auth.azure.clientid}")
	private String clientId;

	@Value("${zup.keyvault.auth.azure.secret}")
	private String clientSecret;
	
	@Override
	public String doAuthenticate(String pAuthorization, String pResource, String scope) {
		String authorization = pAuthorization;
		String resource = pResource;
		String token = null;
		try {
			token = getAccessTokenAsString(authorization, resource);
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

	private String getAccessTokenAsString(String authorization, String resource) throws InterruptedException, ExecutionException, MalformedURLException {
		authorization = authorization != null ? authorization : AUTH_URI;
		ExecutorService service = Executors.newFixedThreadPool(1);
		AuthenticationContext authContext = new AuthenticationContext(authorization, true, service);
		return authContext.acquireToken(resource, new ClientCredential(clientId, clientSecret), null).get().getAccessToken();
	}

}
