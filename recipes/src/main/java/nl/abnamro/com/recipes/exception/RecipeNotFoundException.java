package nl.abnamro.com.recipes.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private int id;
    private String message;

    public RecipeNotFoundException() {

    }

    public RecipeNotFoundException(int id) {
        this.id = id;
    }

    public RecipeNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}
