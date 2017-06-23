package zupkeyvault.crypt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;
import com.microsoft.azure.keyvault.models.SecretBundle;
import com.microsoft.azure.storage.core.Base64;

//@Component
public class AzureKeyVaultService  implements KeyVaultService {

	private final KeyVaultClient keyVaultClient;

	public AzureKeyVaultService() {
		KeyVaultCredentials credentials = new ClientSecretKeyVaultCredential();
		//-- 1.0.0 lib
		this.keyVaultClient = new KeyVaultClient(credentials);
		//--
/*		//--0.8.0 lib
		Configuration config = KeyVaultConfiguration.configure(null, credentials);
		this.keyVaultClient = KeyVaultClientService.create(config);
		//--
*/
	}

	@Override
	public String encrypt(final byte[] raw, final String keyId) {
		try {
			//TODO: encryptAsync em keyVault só aceita plaintext (v 0.8.0)
			// v 0.8
			//Future<KeyOperationResult> opResult = keyVaultClient.encryptAsync(resolveKey(keyId), JsonWebKeyEncryptionAlgorithm.RSA_OAEP, raw);
			//--
			// v1.0
			Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, keyId);
			byte[] es = cipher.doFinal(raw);
			return Base64.encode(es);
		} catch (Exception e) {
			// TODO: pensar no que fazer de bom
			throw new KeyVaultException("Unable to encript current content", e);
		}
	}

	@Override
	public String encrypt(MultipartFile rawFile, String keyId) {
		try {
			return encrypt(StreamUtils.copyToByteArray(rawFile.getInputStream()), keyId);
		} catch (IOException e) {
			// TODO: pensar no que fazer de bom
			throw new KeyVaultException("Unable to encript current content", e);
		}
	}

	public byte[] decrypt(final byte[] encrypted, final String keyId) {
			Cipher cipher = initCipher(Cipher.DECRYPT_MODE, keyId);
			try {
				return cipher.doFinal(encrypted);
			} catch (IllegalBlockSizeException | BadPaddingException e) {
				// TODO: pensar no que fazer de bom
				throw new KeyVaultException("Unable to encript current content", e);
			}
	}

	@Override
	public String getSecret(String vault, String kid) {
		SecretBundle secretBundle = keyVaultClient.getSecret(vault, kid);
		return secretBundle.value();
	}
	
	public String getSecret(String kid){
		SecretBundle secretBundle = keyVaultClient.getSecret(kid);
		return secretBundle.value();
	}

	@Override
	public List<String> getEncryptationKeys() {
		throw new KeyVaultException("This method was not implemented into azure key vault yet");
	}

	private Cipher initCipher(int encryptMode,String keyId){
		String secretKey = getSecret(keyId);
		byte[] decodedKey;
		try {
			decodedKey = secretKey.getBytes("UTF-8");
			// rebuild key using SecretKeySpec
			SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(encryptMode, originalKey);
			return cipher;
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			// TODO: pensar no que fazer de bom
			throw new KeyVaultException("Unable to resolve encript/decript module", e);
		}
	}

	@Override
	public String setSecret(String vault, String kid, String secretValue) {
		// TODO Auto-generated method stub
		return null;
	}
}
