package org.jxiang.handler;

import org.jxiang.exceptions.ObjectNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ObjectNotValidException.class)
  public ResponseEntity<Set<String>> handleException(final ObjectNotValidException exception) {
    return ResponseEntity.badRequest().body(exception.getErrorMessages());
  }

}
