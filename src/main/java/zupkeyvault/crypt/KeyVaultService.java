package zupkeyvault.crypt;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface KeyVaultService {
	
	String encrypt(final byte[] raw, final String kid);
	String encrypt(final MultipartFile rawFile, final String kid);
	byte[] decrypt(final byte[] encriptedBlob, final String kid);
	String getSecret(final String vault, final String kid);
	String setSecret(final String vault, final String kid, final String secretValue);
	List<String> getEncryptationKeys();
}
