package zupkeyvault;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import zupkeyvault.crypt.KeyVaultService;

@RestController
public class VaultOperations {

	private final KeyVaultService vaultService;

	//TODO: make this with magic auto-resolved methods
	private static final String MY_APP_NAME ="exampleAPP";
	
	@Autowired
	public VaultOperations(KeyVaultService vaultService) {
		this.vaultService = vaultService;
	}

	@PostMapping("/vault/encryptation")
	public String encrypt(@RequestParam("plaintext") String plaintext) throws UnsupportedEncodingException{
		byte[] plain = plaintext.getBytes("UTF-8");
		return vaultService.encrypt(plain, MY_APP_NAME);
		
	}

	@PostMapping("/vault/decryption")
	public String decrypt(@RequestParam("encripted") String encripted) throws UnsupportedEncodingException{
		byte[] encriptedBlob = encripted.getBytes("UTF-8");
		return new String((vaultService.decrypt(encriptedBlob, MY_APP_NAME)));
	}
	

	@GetMapping("/vault/keys")
	public ResponseEntity<List<String>> listKeys() {
		return ResponseEntity.ok().body(vaultService.getEncryptationKeys());
	}
	
	@GetMapping("/vault/store/secret/{secretPath}")
	public String getSecret(@PathVariable final String secretPath){
		return vaultService.getSecret(MY_APP_NAME, secretPath);
	}
	
	@PostMapping("/vault/store/secret/{secretPath}")
	public String setSecret(@RequestParam("plaintext") String plaintext, @PathVariable final String secretPath){
		return vaultService.setSecret(MY_APP_NAME, secretPath, plaintext);
	}
	
}
