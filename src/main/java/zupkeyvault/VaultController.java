package zupkeyvault;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import zupkeyvault.crypt.KeyVaultService;
import zupkeyvault.storage.StoragePolicy;
import zupkeyvault.storage.StorageService;

@RestController
public class VaultController {

	private final StorageService storageService;
	private final KeyVaultService vaultService;

	@Autowired
	public VaultController(StorageService storageService, KeyVaultService vaultService) {
		this.storageService = storageService;
		this.vaultService = vaultService;
	}

	@GetMapping("/keys")
	public ResponseEntity<List<String>> listKeys() {
		return ResponseEntity.ok().body(vaultService.getEncryptationKeys());
	}

	@PostMapping("/store/encryptation/{kid}")
	public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable String kid)
			throws NoSuchAlgorithmException, InterruptedException, ExecutionException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		try {
			StoragePolicy policy = new StoragePolicy.Builder(file.getOriginalFilename()).setEncriptationKeyId(kid).build();
			storageService.store(file.getInputStream(), policy);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e);
		}
	}
	
	@GetMapping("/store/{file}/decryption/{kid}")
	public FileSystemResource getFile(@PathVariable  String file, @PathVariable String kid) throws IOException {
			byte[] content = storageService.loadPayload(file);
			File tempFile = Files.createTempFile(null, file).toFile();
			FileOutputStream fileStream = new FileOutputStream(tempFile);
			fileStream.write(content);
			fileStream.flush();
			fileStream.close();
			return new FileSystemResource(tempFile);
	}

}
