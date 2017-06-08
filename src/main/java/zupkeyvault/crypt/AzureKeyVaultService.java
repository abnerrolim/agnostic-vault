package zupkeyvault.crypt;

//@Component
public class AzureKeyVaultService {/* implements KeyVaultService {

	private final KeyVaultClient keyVaultClient;
	private final KeyVaultProperties props;

	public AzureKeyVaultService(KeyVaultProperties props) {
		this.props = props;
		KeyVaultCredentials credentials = new ClientSecretKeyVaultCredential();
		//-- 1.0.0 lib
		this.keyVaultClient = new KeyVaultClient(credentials);
		//--
		//--0.8.0 lib
		Configuration config = KeyVaultConfiguration.configure(null, credentials);
		this.keyVaultClient = KeyVaultClientService.create(config);
		//--
	}

	@Override
	public byte[] encrypt(final byte[] raw, final String keyId) {
		try {
			//TODO: encryptAsync em keyVault só aceita plaintext (v 0.8.0)
			byte[] toString = raw;
			// v 0.8
			Future<KeyOperationResult> opResult = keyVaultClient.encryptAsync(resolveKey(keyId), JsonWebKeyEncryptionAlgorithm.RSA_OAEP,
					toString);
			return opResult.get().result();
			//--
			// v1.0
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			AzureHelper.encrypt(null, output, getKeyAsJWK(keyId));
			return output.toByteArray();
		} catch (Exception e) {
			// TODO: pensar no que fazer de bom
			throw new KeyVaultException("Unable to encript current content", e);
		}
	}

	@Override
	public byte[] encrypt(MultipartFile rawFile, String keyId) {
		try {
			return encrypt(StreamUtils.copyToByteArray(rawFile.getInputStream()), keyId);
		} catch (IOException e) {
			// TODO: pensar no que fazer de bom
			throw new KeyVaultException("Unable to encript current content", e);
		}
	}

	@Override
	public JWK getKey(String keyId) {
		KeyBundle keyBundle = keyVaultClient.getKey(resolveKey(keyId));
		// "https://billing-key-vault.vault.azure.net/keys/billing-poc-cripfy/3a7ca86ee9e14138ab825d00e8e2a259"
		JsonWebKey key = keyBundle.key();
		return AzureHelper.convertToRSAKey(key);
	}
	
	private JsonWebKey getKeyAsJWK(String keyId){
		KeyBundle keyBundle = keyVaultClient.getKey(resolveKey(keyId));
		return keyBundle.key();
	}

	private String resolveKey(String key) {
		Assert.notNull(key, "Key identifier can't be null");
		if (key.startsWith(props.getBaseUrl()))
			return key;
		return props.getBaseUrl() + "keys/" + key;
	}

	@Override
	public List<Key> listKeys() {
// v 0.8.0		
		try {
			ListKeysResponseMessage keys = keyVaultClient.getKeysAsync(props.getBaseUrl(), null).get();
			KeyItem[] keyItens = keys.getValue();
			List<Key> ks = new ArrayList<Key>();
			for (KeyItem keyItem : keyItens) {
				Key k = null;//getKey(keyItem.getKid());
				ks.add(k);
			}
			return ks;
		} catch (InterruptedException | ExecutionException e) {
			// TODO: pensar no que fazer de bom
			throw new KeyVaultException("Unable to list keys of key vault", e);
		}
//---
// v 1.0.0
		PagedList<KeyItem> keyItens = keyVaultClient.listKeys(props.getBaseUrl());
		List<Key> ks = new ArrayList<Key>();
		keyItens.forEach( k -> ks.add(getKey(k.kid()))) ;
		return ks;
	}

	public Path decrypt(Path encriptedFile, String keyId) {
		try {
			KeyOperationResult result = keyVaultClient.decrypt(resolveKey(keyId), JsonWebKeyEncryptionAlgorithm.RSA_OAEP, Files.readAllBytes(encriptedFile));
			Path out = Files.createTempFile("decript", "dec");
			OutputStream decripted = Files.newOutputStream(out);
			decripted.write(result.result());
			decripted.flush();
			decripted.close();
			return out;
		} catch (IOException e) {
			// TODO: pensar no que fazer de bom
			throw new KeyVaultException("Unable to encript current content", e);
		}
	}
	
	public String decryptAsString(Path encriptedFile, String keyId) {
		try {
			KeyOperationResult result = keyVaultClient.decrypt(resolveKey(keyId), JsonWebKeyEncryptionAlgorithm.RSA1_5, Files.readAllBytes(encriptedFile));
			Path out = Files.createTempFile("decript", "dec");
			OutputStream decripted = Files.newOutputStream(out);
			decripted.write(result.result());
			decripted.flush();
			decripted.close();
			return "";
		} catch (IOException e) {
			// TODO: pensar no que fazer de bom
			throw new KeyVaultException("Unable to encript current content", e);
		}
	}
*/
}
