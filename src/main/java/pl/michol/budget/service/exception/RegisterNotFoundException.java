package pl.michol.budget.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RegisterNotFoundException extends RuntimeException{

    public RegisterNotFoundException(String message) {
        super(message);
    }
}
