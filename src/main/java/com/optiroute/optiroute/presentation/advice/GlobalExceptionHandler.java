package com.optiroute.optiroute.presentation.advice;


import com.optiroute.optiroute.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String TIMESTAMP = "timestamp";
    private static final String PATH = "path";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );

        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status/404"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setProperty(PATH, request.getDescription(false));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGlobalException(
            Exception ex, WebRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage()
        );

        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status/500"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setProperty(PATH, request.getDescription(false));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(
       Exception ex, WebRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        problemDetail.setTitle("Bad Request");
        problemDetail.setType(URI.create("https://developer.mozilla.org/fr/docs/Web/HTTP/Reference/Status/400"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setProperty(PATH, request.getDescription(false));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }*/


}
