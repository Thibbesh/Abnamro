package nl.abnamro.com.recipes.exception;

import nl.abnamro.com.recipes.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * RestControllerAdvice is global exception handler for recipe web services.
 */
@RestControllerAdvice
public class RecipeExceptionHandler {

    /**
     * exceptionHandler is take care of exception thrown by services or controller.
     * @param exception of  RecipeNotFoundException
     * @return errorResponse
     */
    @ExceptionHandler(RecipeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ErrorResponse> exceptionHandler(RecipeNotFoundException exception) {
        ErrorResponse response =
                new ErrorResponse("Error_Code-0007",
                        "No recipe found with Id " + exception.getId());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * any exception related to wrong input type or empty input type can take care of.
     * @param exception for badRequest
     * @return errorResponse
     */
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponse> badRequestHandler(Exception exception){
        ErrorResponse response =
                new ErrorResponse("Error_Code-0008", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * all the exception which are method not allowed.
     * @param exception for methodNotAllowed
     * @return errorResponse
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ResponseEntity<ErrorResponse> methodNotAllowed(Exception exception){
        ErrorResponse response =
                new ErrorResponse("Error_Code-0009", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Access denied users which is dont have access to endpoints
     * @param exception for access denied.
     * @return errorResponse
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseEntity<ErrorResponse> accessIsDenied(Exception exception){
        ErrorResponse response =
                new ErrorResponse("Error_Code-0010", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

}
