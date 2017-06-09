package zupkeyvault.storage;

import java.io.InputStream;
import java.util.stream.Stream;

public interface StorageService {
	
    void init();

    void store(InputStream inputStream, StoragePolicy storagePolicy);
    
    void store(byte[] blob, StoragePolicy storagePolicy);
    
    void store(BlobObject blob, StoragePolicy storagePolicy);

    Stream<String> listAll();

    byte[] loadPayload(String fileId);
    
    BlobObject loadBlobObject(String fileId);
    
    void delete(String fileId);

}
