package zupkeyvault.storage;

public class StorageException extends RuntimeException {

	private static final long serialVersionUID = 3674164033746663805L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}