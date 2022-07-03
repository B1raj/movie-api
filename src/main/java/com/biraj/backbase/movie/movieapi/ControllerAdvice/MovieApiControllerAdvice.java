/**
 *
 */
package com.biraj.backbase.movie.movieapi.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import com.biraj.backbase.movie.movieapi.bean.ErrorInfo;
import com.biraj.backbase.movie.movieapi.constant.MovieErrorCodeConstant;
import com.biraj.backbase.movie.movieapi.exception.AccessTokenException;
import com.biraj.backbase.movie.movieapi.exception.AuthenticationException;
import com.biraj.backbase.movie.movieapi.exception.BadRequestException;
import com.biraj.backbase.movie.movieapi.exception.InvalidDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


/**
 * @author biraj
 *
 */

@Slf4j
@ControllerAdvice(basePackages = {"com.biraj.backbase.movie.movieapi.controller"})
public class MovieApiControllerAdvice {


    /**
     * Handler for AccessTokenException.
     *
     * @param req
     * @param exception
     * @return
     */

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessTokenException.class)
    @ResponseBody
    ErrorInfo handleAccessTokenException(HttpServletRequest req, AccessTokenException exception) {
        log.error("Forbidden", exception);
        return createErrorResponse(req, exception.getMessage(), exception.getErrorCode());
    }

    /**
     * Handler for AuthenticationException.
     *
     * @param req
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    ErrorInfo handleAuthenticationException(HttpServletRequest req, AuthenticationException exception) {
        log.error("UNAUTHORIZED_ACCESS_ERROR exception ", exception);
        return createErrorResponse(req, exception.getMessage(), exception.getErrorCode());
    }

    /**
     * Handler for InvalidDataException.
     *
     * @param req
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidDataException.class)
    @ResponseBody
    ErrorInfo handleInvalidDataException(HttpServletRequest req, InvalidDataException exception) {
        log.error("FORBIDDEN", exception);
        return createErrorResponse(req, exception.getMessage(), exception.getErrorCode());
    }

    /**
     * Handler for MethodArgumentTypeMismatchException.
     *
     * @param req
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    ErrorInfo handleInvalidDataException(HttpServletRequest req, MethodArgumentTypeMismatchException exception) {
        log.error("BAD_REQUEST", exception);
        return createErrorResponse(req, "BAD_REQUEST", MovieErrorCodeConstant.BAD_REQUEST);
    }


    /**
     *
     * Handler for BadRequestException.
     *
     * @param req
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    ErrorInfo handleBadRequestException(HttpServletRequest req, BadRequestException exception) {
        log.error("UNAUTHORIZED_ACCESS_ERROR exception ", exception);
        return createErrorResponse(req, exception.getMessage(), exception.getErrorCode());
    }


    /**
     *
     * Handler for InvalidDataAccessApiUsageException.
     *
     * @param req
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseBody
    ErrorInfo handleInvalidDataAccessApiUsageException(HttpServletRequest req, InvalidDataAccessApiUsageException exception) {
        log.error("UNAUTHORIZED_ACCESS_ERROR exception ", exception);
        return createErrorResponse(req, exception.getMessage(), MovieErrorCodeConstant.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ErrorInfo handleUnhandledException(HttpServletRequest req, Throwable exception) {
        log.error("UnhandledException", exception);
        return createErrorResponse(req, "Unexpected error occurred. Please contact the Administrator", "500");
    }

    /**
     * @param req
     * @param errorCode
     * @return
     */
    private ErrorInfo createErrorResponse(HttpServletRequest req, String errorMessage, String errorCode) {
        ErrorInfo errorInfo = null;
        try {
            errorInfo = ErrorInfo.builder().errorCode(errorCode).errorMessage(errorMessage).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return errorInfo;
    }

}
