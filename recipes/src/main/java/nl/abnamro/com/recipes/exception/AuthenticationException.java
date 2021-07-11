package nl.abnamro.com.recipes.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String message;

    public AuthenticationException(String message) {
        this.message = message;
    }

}
