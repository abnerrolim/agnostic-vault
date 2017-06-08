package zupkeyvault.storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.StorageCredentialsAccountAndKey;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import zupkeyvault.crypt.KeyVaultService;
import zupkeyvault.helper.StreamConvertUtils;

@Component
public class BlobAzureStorage extends AbstractStorageService {
	
	private CloudBlobContainer container;
	
	@Autowired
    public BlobAzureStorage(StorageProperties properties, KeyVaultService keyVault) {
		super(properties, keyVault);
    }
	
	@Override
	public void init() {
		StorageCredentials creds = new StorageCredentialsAccountAndKey(properties.getAccount(),
				properties.getKey());
		try{
			CloudStorageAccount account = new CloudStorageAccount(creds, true);
			CloudBlobClient client = account.createCloudBlobClient();
			CloudBlobContainer contain = client.getContainerReference("container");
			contain.createIfNotExists();
			container = contain;
		}catch(StorageException | URISyntaxException e){
			//TODO:make me better
			throw new zupkeyvault.storage.StorageException(e.getMessage());
		}
	}

	@Override
	public Stream<String> listAll() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public byte[] loadPayload(String fileId){
		return loadBlobObject(fileId).getPayload().decode();
	}

    public BlobObject loadBlobObject(String fileId){
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			CloudBlockBlob blob = container.getBlockBlobReference(fileId);
			blob.download(baos);
			return StreamConvertUtils.byteToObject(baos.toByteArray(), BlobObject.class);
		}catch(URISyntaxException | StorageException | IOException | ClassNotFoundException e) {
			throw new zupkeyvault.storage.StorageException(e.getMessage());//TODO ajeitar
		}finally{
			try {
				baos.flush();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();//TODO
			}
		}
    }

	@Override
	public void delete(String fileId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(BlobObject blob, StoragePolicy storagePolicy) {
		try {
			CloudBlockBlob cbb = container.getBlockBlobReference(storagePolicy.getBlobReference());
			InputStream in = StreamConvertUtils.objectToInputStream(blob);
			cbb.upload(in, in.available());
		} catch (URISyntaxException | StorageException | IOException e) {
			throw new zupkeyvault.storage.StorageException(e.getMessage());//TODO ajeitar
		}
		
	}

}
