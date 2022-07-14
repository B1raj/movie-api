package com.biraj.backbase.movie.movieapi.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.biraj.backbase.movie.movieapi.bean.AccessToken;
import com.biraj.backbase.movie.movieapi.bean.ErrorInfo;
import com.biraj.backbase.movie.movieapi.constant.MovieConstant;
import com.biraj.backbase.movie.movieapi.constant.MovieErrorCodeConstant;
import com.biraj.backbase.movie.movieapi.service.AccessFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author birajmishra
 * Global filter for all requestion , for secuity.
 */

@Component
@Slf4j
public class MovieApiFilter implements Filter {

    @Autowired
    private AccessFactory tokenFactory;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        MDC.put("requestUUID", req.getHeader(MovieConstant.UUID));
        String accessToken = req.getHeader(MovieConstant.ACCESS_TOKEN);
        String authorization = req.getHeader(MovieConstant.AUTHORIZATION);
        Boolean isChainingDisabled = false;
        ErrorInfo errorInfo;

        if (authorization != null && !"".equals(authorization.trim())) {
            isChainingDisabled = false;
        } else if (accessToken != null && !"".equals(accessToken.trim())) {
            AccessToken accessTokenObj = null;
            try {
                accessTokenObj = tokenFactory.verifyAccessToken(accessToken);
                req.setAttribute(MovieConstant.ACCESS_TOKEN, accessTokenObj);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.error("MovieApiFilter : doFilter : invalid access Token : {} ", accessToken);
                errorInfo = new ErrorInfo(MovieErrorCodeConstant.ACCESS_TOKEN_INVALID, MovieConstant.RELOGIN);
                res.setStatus(HttpStatus.UNAUTHORIZED.value());
                res.setContentType("application/json");
                res.getWriter().write(errorInfo.toString());
                isChainingDisabled = true;
            }

            if (log.isTraceEnabled() && null != accessTokenObj) {
                log.trace("MovieApiFilter : doFilter : accessTokenObj : " + accessTokenObj);
            }
        }

        if (!isChainingDisabled) {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
    }

}
