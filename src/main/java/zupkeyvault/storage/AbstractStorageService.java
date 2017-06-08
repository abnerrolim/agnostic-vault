package zupkeyvault.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import com.google.common.collect.Maps;

import zupkeyvault.crypt.KeyVaultService;

public abstract class AbstractStorageService implements StorageService {
	
	private KeyVaultService keyVaultService;

	protected final StorageProperties properties;
	
	public AbstractStorageService(StorageProperties properties, KeyVaultService keyVaultService) {
		this.keyVaultService = keyVaultService;
		this.properties = properties;
		this.init();
	}

	@Override
	public void store(InputStream inputStream, StoragePolicy storagePolicy){
		try{
			store(StreamUtils.copyToByteArray(inputStream), storagePolicy);
		}catch(IOException e){
			throw new StorageException("Something wrong with your inputstream....",e);
		}
	}
	
	@Override
	public void store(byte[] blob, StoragePolicy storagePolicy) {
		Assert.notNull(blob, "Blob can't be null!!");
		Assert.notNull(storagePolicy, "You need set storagepolicy");
		Map<String,String> metadata = Maps.newHashMap();
		byte[] newBlobOnTheBlock;
		if(storagePolicy != null && storagePolicy.applyEncryptation()){
			newBlobOnTheBlock = keyVaultService.encrypt(blob, storagePolicy.getKid());
			//TODO:make this a reference not the uri
			metadata.put(BlobObject.ENCRIPTED_REFERENCE, storagePolicy.getKid());
		}else{
			newBlobOnTheBlock = blob;
		}
		//delegates to impl
		store(new BlobObject(metadata, newBlobOnTheBlock, storagePolicy.getBlobReference()), storagePolicy);
	}
	

}
