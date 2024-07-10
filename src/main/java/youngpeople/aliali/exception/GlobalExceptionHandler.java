package youngpeople.aliali.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.exception.common.NotMatchedEntitiesException;
import youngpeople.aliali.exception.token.IllegalTokenException;
import youngpeople.aliali.exception.token.NotAuthenticatedException;
import youngpeople.aliali.exception.token.TimeoutTokenException;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Custom Exception
     */
    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity<BasicResDto> handleNotAuthenticatedException() {
        return ResponseEntity
                .status(NotAuthenticatedException.STATUS_CODE)
                .body(BasicResDto.builder().message(NotAuthenticatedException.MESSAGE).build());
    }

    @ExceptionHandler(TimeoutTokenException.class)
    public ResponseEntity<BasicResDto> handleTimeoutTokenException() {
        return ResponseEntity
                .status(TimeoutTokenException.STATUS_CODE)
                .body(BasicResDto.builder().message(TimeoutTokenException.MESSAGE).build());
    }

    @ExceptionHandler(IllegalTokenException.class)
    public ResponseEntity<BasicResDto> handleIllegalTokenException() {
        return ResponseEntity
                .status(IllegalTokenException.STATUS_CODE)
                .body(BasicResDto.builder().message(IllegalTokenException.MESSAGE).build());
    }

    @ExceptionHandler(NotMatchedEntitiesException.class)
    public ResponseEntity<BasicResDto> handleNotMatchedEntitiesException() {
        return ResponseEntity
                .status(NotMatchedEntitiesException.STATUS_CODE)
                .body(BasicResDto.builder().message(NotMatchedEntitiesException.MESSAGE).build());
    }



    /**
     * Existing Exception
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<BasicResDto> handleNoSuchElementException() {
        return ResponseEntity
                .status(471)
                .body(BasicResDto.builder().message("Not Found Entity.").build());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<BasicResDto> handleNoResourceFoundException() {
        return ResponseEntity
                .status(404)
                .body(BasicResDto.builder().message("Wrong Path.").build());
    }



}
