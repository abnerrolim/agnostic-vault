package zupkeyvault.crypt;

import org.springframework.stereotype.Component;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.CubbyholeAuthentication;
import org.springframework.vault.authentication.CubbyholeAuthenticationOptions;
import org.springframework.vault.client.VaultClients;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;
import org.springframework.vault.support.VaultToken;

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
        CubbyholeAuthenticationOptions options = CubbyholeAuthenticationOptions
                .builder()
                .initialToken(VaultToken.of(properties.getToken()))
                .wrapped()
                .build();
        return new CubbyholeAuthentication(options, VaultClients.createRestTemplate(vaultEndpoint(),clientHttpRequestFactoryWrapper().getClientHttpRequestFactory()));
	}

	@Override
	public VaultEndpoint vaultEndpoint() {
		VaultEndpoint vaultEndPoint = new VaultEndpoint();
		vaultEndPoint.setHost(properties.getBaseUrl());
		vaultEndPoint.setPort(properties.getPort());
		vaultEndPoint.setScheme("https");
		return vaultEndPoint;
	}

}
