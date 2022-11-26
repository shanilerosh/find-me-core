package com.findmecore.findmecore.errors;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;


@Slf4j
@ControllerAdvice
public class ErrorTranslator {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleNoSuchElementException(RuntimeException ex, NativeWebRequest request) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

}
