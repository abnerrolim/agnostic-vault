package zupkeyvault.crypt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import zupkeyvault.helper.ConvertUtils;
import zupkeyvault.storage.StorageException;

@Component
public class VaultProjectService implements KeyVaultService {
	
	private VaultOperations vaultOperations;
	
	public VaultProjectService(VaultProjectConfig vaultProjectConfig) {
		//TODO: injection of  VaultOperations like doc says does not work here, dont know why
		this.vaultOperations = new VaultTemplate(vaultProjectConfig.vaultEndpoint(), vaultProjectConfig.clientAuthentication());
	}

	@Override
	public String encrypt(byte[] raw, String kid) {
		return vaultOperations.opsForTransit().encrypt(kid, raw, null);
	}

	@Override
	public String encrypt(MultipartFile rawFile, String kid) {
		try {
			return encrypt(rawFile.getBytes(), kid);
		} catch (IOException e) {
			throw new KeyVaultException("Unable to encript",e);
		}
	}

	@Override
	public byte[] decrypt(byte[] encriptedBlob, String kid) {
		try {
			return vaultOperations.opsForTransit().decrypt(kid, new String(encriptedBlob, "UTF-8"), null);
		} catch (UnsupportedEncodingException e) {
			throw new StorageException("UTF-8 is not supported? Really?",e);
		}
	}

	@Override
	public String getSecret(String vault, String kid) {
		VaultResponse vaultResponse = vaultOperations.read(formatPath(vault, kid));
		try {
			return ConvertUtils.objectToJsonString(vaultResponse);
		} catch (JsonProcessingException e) {
			throw new KeyVaultException("Unable to encript",e);
		}
	}

	private String formatPath(String vault, String kid) {
		return "secret/"+ vault + "/" + kid;
	}

	@Override
	public List<String> getEncryptationKeys() {
		return vaultOperations.opsForTransit().getKeys();
	}

	@Override
	public String setSecret(String vault, String kid, String secretValue) {
		VaultResponse vaultResponse = vaultOperations.write(formatPath(vault, kid), secretValue);
		return (vaultResponse != null ? vaultResponse.toString() : "null");
	}

}
