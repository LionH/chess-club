package org.chesscorp.club.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handle all controller exceptions.
 */
@ControllerAdvice
class GlobalControllerExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        if (logger.isDebugEnabled()) {
            logger.warn(e.getLocalizedMessage(), e);
        } else {
            logger.warn(e.getLocalizedMessage());
        }
    }
}