package com.moviemania.movieAPI.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FileExistsException.class)
    public ProblemDetail handleFileExistsException(FileExistsException fileExistsException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,fileExistsException.getMessage());
    }

    @ExceptionHandler(EmptyFileException.class)
    public ProblemDetail handleEmptyFileException(EmptyFileException emptyFileException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,emptyFileException.getMessage());
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ProblemDetail handleMovieNotFoundException(MovieNotFoundException movieNotFoundException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,movieNotFoundException.getMessage());
    }

}
