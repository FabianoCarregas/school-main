package br.com.alura.school.user;

public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(Object id) {
		super("Resource not found. id "+ id);
	}

}
