package net.dkahn.starter.authentication.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dkahn.starter.authentication.dto.FailureDTO;
import net.dkahn.starter.services.security.exception.RestAuthenticationException;
import net.dkahn.starter.tools.date.DateUtils;
import net.dkahn.starter.tools.logger.Log;
import org.slf4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Failure Ajax response
 */
@Component
public class FailureAjaxHandler implements AuthenticationFailureHandler {

    @Log
    private Logger logger = null;

    private final MessageSource messageSource;

    public FailureAjaxHandler(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.debug("failure login");

        FailureDTO failureDTO = buildResponse(exception,request);

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().append(mapper.writeValueAsString(failureDTO));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private FailureDTO buildResponse(AuthenticationException exception, HttpServletRequest request) {

        if(exception instanceof RestAuthenticationException){
            RestAuthenticationException ex = (RestAuthenticationException) exception;
            return FailureDTO.builder()
                .attempts(ex.getAttemps())
                .lockedUntil(DateUtils.asDate(ex.getLockedUntil()))
                .message(messageSource.getMessage(ex.getMessageCode(),null,request.getLocale()))
                .build();
        }


        if(exception instanceof LockedException){
            LockedException ex = (LockedException) exception;
            return FailureDTO.builder()
                    .message(ex.getMessage())
                    .build();
        }


        if(exception instanceof DisabledException){
            return FailureDTO.builder()
                    .disable(true)
                    .message(exception.getMessage())
                    .build();
        }


        if(exception instanceof AccountExpiredException){
            return FailureDTO.builder()
                    .disable(true)
                    .message(exception.getMessage())
                    .build();
        }


        return FailureDTO.builder().message(exception.getMessage()).build();
    }
}
