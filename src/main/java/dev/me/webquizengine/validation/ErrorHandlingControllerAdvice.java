package dev.me.webquizengine.validation;

import dev.me.webquizengine.validation.exceptions.EmailAlreadyTakenException;
import dev.me.webquizengine.validation.exceptions.QuizDoesNotExistsException;
import dev.me.webquizengine.validation.exceptions.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    private final static String EMAIL_TAKEN_MESSAGE = "Email is already taken";

    private final static String QUIZ_DOESNT_EXIST_MESSAGE = "Quiz with the inputted id does not exist";

    private final static String PERMISSION_DENIED_MESSAGE = "You don't have permission to delete quizzes" +
            " that are uploaded by other people";

    @ExceptionHandler(ConstraintViolationException.class)  //handles the custom Constraint
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();

        for (ConstraintViolation constraint : e.getConstraintViolations()) {
            error.getViolations().add(
                    new Violation(constraint.getPropertyPath().toString(), constraint.getMessage())
            );
        }

        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // handles the default @Valid subAnnotations
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse methodArgumentException(MethodArgumentNotValidException e) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorResponse.getViolations().add(
                    new Violation(fieldError.getField(), fieldError.getDefaultMessage())
            );
        }

        return errorResponse;
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ResponseEntity<Object> emailAlreadyTakenException(EmailAlreadyTakenException e) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                 .body(EMAIL_TAKEN_MESSAGE);
    }

    @ExceptionHandler(QuizDoesNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)  //added only for readability
    @ResponseBody
    protected ResponseEntity<Object> quizDoesNotExistsException(QuizDoesNotExistsException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(QUIZ_DOESNT_EXIST_MESSAGE);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)   //added only for readability
    @ResponseBody
    protected ResponseEntity<Object> unauthorizedAccessException(UnauthorizedAccessException e) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(PERMISSION_DENIED_MESSAGE);
    }
}
