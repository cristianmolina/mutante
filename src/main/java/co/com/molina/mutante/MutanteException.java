package co.com.molina.mutante;

public class MutanteException extends Exception{

	/**
	 * Serial id.
	 */
	private static final long serialVersionUID = 4668661111234880173L;

	/**
	 * 
	 */
	public MutanteException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public MutanteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MutanteException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public MutanteException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MutanteException(Throwable cause) {
		super(cause);
	}

}
