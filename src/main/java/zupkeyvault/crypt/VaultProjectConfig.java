package zupkeyvault.crypt;

import org.springframework.stereotype.Component;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;

@Component
public class VaultProjectConfig extends AbstractVaultConfiguration {
	
	
	private KeyVaultProperties properties;

	public VaultProjectConfig(KeyVaultProperties props) {
		this.properties = props;
	}
    /**
     * Configure a client authentication.
     * Please consider a more secure authentication method
     * for production use.
     */
	@Override
	public ClientAuthentication clientAuthentication() {
        return new TokenAuthentication(properties.getToken());
	}

	@Override
	public VaultEndpoint vaultEndpoint() {
		VaultEndpoint vaultEndPoint = new VaultEndpoint();
		vaultEndPoint.setHost(properties.getBaseUrl());
		vaultEndPoint.setPort(properties.getPort());
		vaultEndPoint.setScheme("http");
		return vaultEndPoint;
	}

}
