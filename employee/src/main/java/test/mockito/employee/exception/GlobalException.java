package test.mockito.employee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler({UserExistsException.class, UserDoesNotExistException.class})
	public ResponseEntity<?> userExistOrNotHandler(Exception ex)
	{
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.CONFLICT);
	}
}
