package zupkeyvault.storage;

import java.io.Serializable;
import java.util.Map;

import com.nimbusds.jose.util.Base64URL;

public class BlobObject implements Serializable{
	
	transient static final String ENCRIPTED_REFERENCE = "ENC_KEY_ID"; 
	
	private static final long serialVersionUID = -5093038994261452312L;

	private final Map<String, String> metadata;
	
	private final Base64URL payload;
	
	private final BlobObjectType objectType;
	
	private final String blobName;
	
	
	//TODO: builder?
	BlobObject(Map<String, String> metadata, byte[] payload, String blobName){
		this.metadata = metadata;
		this.payload = Base64URL.encode(payload);
		this.objectType = classifyObjectType(metadata);
		this.blobName = blobName;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public String getBlobName(){
		return blobName;
	}
	
	public Base64URL getPayload() {
		return payload;
	}
	
	public BlobObjectType getObjectType(){
		return objectType;
	}

	private BlobObjectType classifyObjectType(Map<String,String> metadata){
		if(metadata != null && metadata.containsKey(ENCRIPTED_REFERENCE))
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
