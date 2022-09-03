package co.com.molina.mutante;

public class MutanteRuntimeException extends RuntimeException{

	/**
	 * Serial id.
	 */
	private static final long serialVersionUID = 4668661111234880173L;

	/**
	 * 
	 */
	public MutanteRuntimeException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public MutanteRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MutanteRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public MutanteRuntimeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MutanteRuntimeException(Throwable cause) {
		super(cause);
	}

}
