/**
 *
 */
package com.biraj.backbase.movie.movieapi.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import com.biraj.backbase.movie.movieapi.bean.ErrorInfo;
import com.biraj.backbase.movie.movieapi.constant.MovieErrorCodeConstant;
import com.biraj.backbase.movie.movieapi.exception.AccessTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


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
