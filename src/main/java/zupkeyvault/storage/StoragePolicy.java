package zupkeyvault.storage;

public class StoragePolicy {
	
	private String kid;
	
	private String blobReference;
	
	private StoragePolicy(){
	}

	private void setKid(String key){
		this.kid = key;
	}
	
	public String getKid(){
		return kid;
	}
	
	private void setBlobReference(String name){
		this.blobReference = name;
	}
	
	public String getBlobReference(){
		return this.blobReference;
	}
	
	public static class Builder{
		private StoragePolicy sto;
		
		public Builder(String blobStorageReference){
			this.sto = new StoragePolicy();
			sto.setBlobReference(blobStorageReference);
		}
		
		public Builder setEncriptationKeyId(String kid){
			sto.setKid(kid);
			return this;
		}
		
		public StoragePolicy build(){
			return sto;
		}
	}
	
	public boolean applyEncryptation(){
		return kid != null;
	}

}
