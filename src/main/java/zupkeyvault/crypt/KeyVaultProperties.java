package zupkeyvault.crypt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeyVaultProperties {
	
	@Value("${zup.keyvault.baseurl}")
    private String baseUrl;
	
	@Value("${zup.keyvault.port}")
    private int port;
	
	@Value("${zup.keyvault.auth.token}")
    private String token;


	public int getPort() {
		return port;
	}

	public String getToken() {
		return token;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

}
