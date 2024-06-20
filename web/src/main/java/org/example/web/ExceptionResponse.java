package org.example.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

//@ControllerAdvice
public class ExceptionResponse {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionResponse.class);

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public String defaultExceptionHandler(Throwable t) {
        logError(t);
        return "Unknown";
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseBody
    public String dataError(Throwable t) {
        logError(t);
        return "dataError";
    }

    private void logError(Throwable t) {
        if (logger.isErrorEnabled()) {
            logger.error("Service error: " + t.getMessage(), t);
        }
    }
}
