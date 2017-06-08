package zupkeyvault.crypt;

public class KeyVaultException extends RuntimeException {

	private static final long serialVersionUID = 3674164033746663805L;

	public KeyVaultException(String message) {
        super(message);
    }

    public KeyVaultException(String message, Throwable cause) {
        super(message, cause);
    }
}