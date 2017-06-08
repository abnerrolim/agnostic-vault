package zupkeyvault.crypt;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nimbusds.jose.jwk.JWK;

public interface KeyVaultService {
	
	byte[] encrypt(byte[] raw, String kid);
	byte[] encrypt(MultipartFile rawFile, String kid);
	byte[] decrypt(byte[] encriptedBlob, String kid);
	JWK getKey(String kid);
	List<JWK> listKeys();
}
