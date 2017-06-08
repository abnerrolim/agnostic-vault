package zupkeyvault.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageProperties {

	@Value("${zup.keyvault.storage.container}")
    private String conteiner;

	@Value("${zup.keyvault.storage.account}")
	private String account;
	
	@Value("${zup.keyvault.storage.key}")
	private String key;

	public String getConteiner() {
		return conteiner;
	}

	public void setConteiner(String conteiner) {
		this.conteiner = conteiner;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
