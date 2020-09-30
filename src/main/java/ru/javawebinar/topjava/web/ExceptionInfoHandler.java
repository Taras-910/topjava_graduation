package ru.javawebinar.topjava.web;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.IllegalArgumentException;
import ru.javawebinar.topjava.util.exception.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo notFound(HttpServletRequest req, NotFoundException e) {
        log.error("handleError {} {}", req.getRequestURL(), e);
        return logAndGetErrorInfo(req, e, UNPROCESSABLE_ENTITY);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler({DataIntegrityViolationException.class, IllegalArgumentException.class})
    public ErrorInfo conflict(HttpServletRequest req, Exception e) {
        log.error("conflict {} {}", req.getRequestURL(), e);
        return logAndGetErrorInfo(req, e, CONFLICT);
    }

    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)  // 405
    @ExceptionHandler(MethodNotAllowedException.class)
    public ErrorInfo methodNotAllowed(HttpServletRequest req, Exception e) {
        log.error("conflict {} {}", req.getRequestURL(), e);
        return logAndGetErrorInfo(req, e, METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(value = UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ErrorInfo bindValidationError(HttpServletRequest req, Exception e) {
        log.error("bindValidationError {} {}", req.getRequestURL(), e);
        return logAndGetErrorInfo(req, e, UNPROCESSABLE_ENTITY);
    }

    @ResponseStatus(value = UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class, UnrecognizedPropertyException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, Exception e) {
        log.error("illegalRequestDataError {} {}", req.getRequestURL(), e);
        return logAndGetErrorInfo(req, e, UNPROCESSABLE_ENTITY);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        log.error("handleError {} {}", req.getRequestURL(), e);
        return logAndGetErrorInfo(req, e, INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(BAD_REQUEST)  // 400
    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorInfo wrongRequest(HttpServletRequest req, NoHandlerFoundException e) {
        return logAndGetErrorInfo(req, e, BAD_REQUEST);
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, HttpStatus status, String... details) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        log.warn("{} at request  {}: {}", status, req.getRequestURL(), rootCause.toString());
        if (details.length == 0){
            details = rootCause != null ? new String[]{rootCause.getLocalizedMessage()} : new String[]{rootCause.getClass().getName()};
        }
        return new ErrorInfo(req.getRequestURL(), status, details);
    }
}
