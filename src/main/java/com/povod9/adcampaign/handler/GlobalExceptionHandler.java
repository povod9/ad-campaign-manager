package com.povod9.adcampaign.handler;

import com.povod9.adcampaign.dto.ExceptionDto;
import com.povod9.adcampaign.exception.AccessDeniedException;
import com.povod9.adcampaign.exception.InvalidCredentialsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handleIllegalArg(IllegalArgumentException e){
        ExceptionDto exceptionDto = new ExceptionDto("Illegal Argument", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleEntityNotFound(EntityNotFoundException e){
        ExceptionDto exceptionDto = new ExceptionDto("Not Found", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDto);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionDto> handleInvalidCredentials(InvalidCredentialsException e){
        ExceptionDto exceptionDto = new ExceptionDto("Invalid Credentials", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDto> handleAccessDenied(AccessDeniedException e){
        ExceptionDto exceptionDto = new ExceptionDto("Forbidden", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionDto);
    }



}
