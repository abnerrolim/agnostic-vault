package zupkeyvault.helper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.util.StreamUtils;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;/*
													import com.microsoft.azure.keyvault.KeyVaultClient;
													import com.microsoft.azure.keyvault.KeyVaultClientService;
													import com.microsoft.azure.keyvault.KeyVaultConfiguration;
													import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;
													import com.microsoft.azure.keyvault.models.KeyBundle;
													import com.microsoft.azure.keyvault.webkey.JsonWebKey;
													import com.microsoft.azure.keyvault.webkey.JsonWebKeyType;
													import com.microsoft.azure.storage.CloudStorageAccount;
													import com.microsoft.azure.storage.StorageCredentials;
													import com.microsoft.azure.storage.StorageCredentialsAccountAndKey;
													import com.microsoft.azure.storage.StorageException;
													import com.microsoft.azure.storage.blob.CloudBlobClient;
													import com.microsoft.azure.storage.blob.CloudBlobContainer;*/
//import com.microsoft.windowsazure.Configuration;
import com.microsoft.azure.keyvault.webkey.JsonWebKey;
import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.Base64URL;

public class AzureHelper {

	private final static String AUTH_URI = "https://login.windows.net/common/oauth2/authorize";

	public static Future<AuthenticationResult> getFutureAccessToken(String authority, String resource)
			throws MalformedURLException {
		authority = authority != null ? authority : AUTH_URI;
		ExecutorService service = Executors.newFixedThreadPool(1);
		AuthenticationContext authContext = new AuthenticationContext(authority, true, service);
		return authContext.acquireToken(resource, getClientCredential(), null);
	}

	public static AuthenticationResult getAccessToken(String authority, String resource)
			throws MalformedURLException, InterruptedException, ExecutionException {
		return getFutureAccessToken(authority, resource).get();
	}

	public static ClientCredential getClientCredential() {
		ClientCredential clientCred = new ClientCredential("92d10c4c-3c33-4be8-86d6-920783a99df8",
				"X9trgAYgHj2k0vJ+GdYCvhCmeyVUwAlCcMiBijz+cFg=");
		return clientCred;
	}

	public static String getAccessTokenAsString(String authority, String resource)
			throws MalformedURLException, InterruptedException, ExecutionException {
		return getAccessToken(authority, resource).getAccessToken();
	}
	/*
	 * public static CloudBlobContainer getCloudBlobContainer() throws
	 * StorageException, URISyntaxException { StorageCredentials creds = new
	 * StorageCredentialsAccountAndKey("billingpocsdiag966",
	 * "xBS6GhBlc5bbjFLMGHK7do0/AsZkEg8oqf2R8K53AmhIxiNTnNU27JkYFiksGsydy8vNUCCzP9Pw5L53GIaZFA=="
	 * ); CloudStorageAccount account = new CloudStorageAccount(creds, true);
	 * CloudBlobClient client = account.createCloudBlobClient();
	 * CloudBlobContainer contain = client.getContainerReference("container");
	 * contain.createIfNotExists(); return contain; }
	 */

	/*
	 * private static RsaKey getKey() throws InterruptedException,
	 * ExecutionException, NoSuchAlgorithmException { KeyVaultCredentials
	 * credentials = new ClientSecretKeyVaultCredential(); Configuration config
	 * = KeyVaultConfiguration.configure(null, credentials); KeyVaultClient
	 * keyVaultClient = KeyVaultClientService.create(config); KeyBundle
	 * keyBundle = keyVaultClient.getKeyAsync(
	 * "https://billing-key-vault.vault.azure.net/keys/billing-poc-cripfy/3a7ca86ee9e14138ab825d00e8e2a259"
	 * ).get();
	 * 
	 * JsonWebKey key = keyBundle.getKey(); if
	 * (key.getKty().equals(JsonWebKeyType.RSA)) { // The private key is not
	 * available for KeyVault keys return new RsaKey(key.getKid(),
	 * key.toRSA(false)); } else if (key.getKty().equals(JsonWebKeyType.RSAHSM))
	 * { // The private key is not available for KeyVault keys return new
	 * RsaKey(key.getKid(), key.toRSA(false)); } else { throw new
	 * IllegalArgumentException(String.format("The key type %s is not supported"
	 * , key.getKid())); }return null; }
	 */

	public static void encrypt(final InputStream sourceStream, final ByteArrayOutputStream targetStream, JsonWebKey k)
			throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, k.toRSA().getPublic());
			byte[] es = cipher.doFinal(StreamUtils.copyToByteArray(sourceStream));
			targetStream.write(es);
		} finally {
			targetStream.flush();
			targetStream.close();
			sourceStream.close();
		}
	}

	public static void decrypt(final InputStream encripted, final OutputStream decriptedDestination, JsonWebKey k)
			throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, IOException,
			InvalidKeyException, NoSuchPaddingException {
		// TODO verify type of key etc
		try {

			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
			cipher.init(Cipher.DECRYPT_MODE, k.toRSA().getPublic());
			InputStream inputEntry = new BufferedInputStream(encripted);
			byte[] decryp = cipher.doFinal(StreamUtils.copyToByteArray(inputEntry));
			decriptedDestination.write(decryp);
		} finally {
			encripted.close();
			decriptedDestination.flush();
			decriptedDestination.close();
		}

	}
	
	public static RSAKey convertToRSAKey(JsonWebKey jsonWebKey){
		return new RSAKey.Builder(Base64URL.encode(jsonWebKey.n()), 
				Base64URL.encode(jsonWebKey.e())).keyID(jsonWebKey.kid()).
			keyUse(KeyUse.ENCRYPTION).algorithm(new Algorithm(jsonWebKey.kty().toString())).build();
	}
}
