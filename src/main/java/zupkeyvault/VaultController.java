package zupkeyvault;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
/*
	@GetMapping("/keys")
	public ResponseEntity<List<Key>> listKeys() {
		return ResponseEntity.ok().body(vaultService.listKeys());
	}
*/
	@PostMapping("/store/encryption/{kid}")
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
	
	/*@GetMapping("/store/{file}/dencryption/{kid}")
	public ResponseEntity getFile(@PathVariable  String file, @PathVariable String kid)
			throws NoSuchAlgorithmException, InterruptedException, ExecutionException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		try {
			Path encriptedFile = null;//storageService.load(file);
			Path decryptedFile  = vaultService.decrypt(encriptedFile, kid);
			return ResponseEntity.ok(decryptedFile);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e);
		}
	}*/

}
