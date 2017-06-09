package zupkeyvault.storage;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import zupkeyvault.crypt.KeyVaultService;

public class BlobObject implements Serializable{
	
	transient static final String ENCRYPT_REFERENCE = "ENC_KEY_ID"; 
	
	private static final long serialVersionUID = -5093038994261452312L;

	private final Map<String, String> metadata;
	
	private byte[] payload;
	
	private final BlobObjectType objectType;
	
	private final String blobName;
	
	
	//TODO: builder?
	BlobObject(Map<String, String> metadata, byte[] payload, String blobName){
		this.metadata = metadata;
		this.payload = payload;
		this.objectType = classifyObjectType(metadata);
		this.blobName = blobName;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public String getBlobName(){
		return blobName;
	}
	
	public byte[] getPayload() {
		return payload;
	}
	
	protected void decryptPayload(KeyVaultService keyVaultService){
		byte[] decripted = keyVaultService.decrypt(getPayload(), getEncryptReference());
		this.payload = decripted;
	}
	
	protected static byte[] encryptPayload(KeyVaultService keyVaultService, byte[] rawPayload, String kid){
		String cypherText = keyVaultService.encrypt(rawPayload, kid);
		try {
			return cypherText.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new StorageException("UTF-8 is not supported? Really?",e);
		}
	}
	
	public BlobObjectType getObjectType(){
		return objectType;
	}
	
	public boolean isEncrypted(){
		return getObjectType().equals(BlobObjectType.ENCRYPTED);
	}
	
	public String getEncryptReference(){
		if(!isEncrypted())
			throw new StorageException("Current blob isnt encrypted!");
		return metadata.get(ENCRYPT_REFERENCE);
	}

	private BlobObjectType classifyObjectType(Map<String,String> metadata){
		if(metadata != null && metadata.containsKey(ENCRYPT_REFERENCE))
			return BlobObjectType.ENCRYPTED;
		return BlobObjectType.UNCRYPTED;
	}
	
	public enum BlobObjectType{
		ENCRYPTED("ENCRIPTED"),
		UNCRYPTED("UNCRYPTED");
		private final String value;
		
		private BlobObjectType(String value){
			this.value = value;
		}
		
		@Override
		public String toString() {
			return this.value;
		}
		
	}
	
}
