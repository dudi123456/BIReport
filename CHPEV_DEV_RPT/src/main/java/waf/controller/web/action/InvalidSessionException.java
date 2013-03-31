package waf.controller.web.action;

public class InvalidSessionException extends Exception implements
		java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6170796145249767608L;

	public InvalidSessionException() {
	}

	public InvalidSessionException(String str) {
		super(str);
	}

}