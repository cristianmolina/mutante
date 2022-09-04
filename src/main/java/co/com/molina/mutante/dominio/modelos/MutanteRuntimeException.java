package co.com.molina.mutante.dominio.modelos;

public class MutanteRuntimeException extends RuntimeException{

	/**
	 * Serial id.
	 */
	private static final long serialVersionUID = 4668661111234880173L;

	private final CodigoValidacionMutante codigoValidacion;
	
	public MutanteRuntimeException() {
		super();
		this.codigoValidacion = CodigoValidacionMutante.ERROR_GENERAL;
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public MutanteRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.codigoValidacion = CodigoValidacionMutante.ERROR_GENERAL;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MutanteRuntimeException(String message, Throwable cause) {
		super(message, cause);
		this.codigoValidacion = CodigoValidacionMutante.ERROR_GENERAL;
	}

	/**
	 * @param message
	 */
	public MutanteRuntimeException(String message) {
		super(message);
		this.codigoValidacion = CodigoValidacionMutante.ERROR_GENERAL;
	}

	/**
	 * @param cause
	 */
	public MutanteRuntimeException(Throwable cause) {
		super(cause);
		this.codigoValidacion = CodigoValidacionMutante.ERROR_GENERAL;
	}
	
	/**
	 * @param message
	 */
	public MutanteRuntimeException(CodigoValidacionMutante codigo) {
		super();
		this.codigoValidacion = codigo;
	}

	public CodigoValidacionMutante getCodigoValidacion() {
		return codigoValidacion;
	}

	
}
