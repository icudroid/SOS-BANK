package net.dkahn.starter.authentication.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dkahn.starter.authentication.dto.FailureDTO;
import net.dkahn.starter.services.security.exception.RestAuthenticationException;
import net.dkahn.starter.tools.date.DateUtils;
import net.dkahn.starter.tools.logger.Log;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
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

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.debug("failure login");

        FailureDTO failureDTO = buildResponse(exception);

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().append(mapper.writeValueAsString(failureDTO));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private FailureDTO buildResponse(AuthenticationException exception) {

        if(exception instanceof RestAuthenticationException){
            RestAuthenticationException ex = (RestAuthenticationException) exception;
            return FailureDTO.builder()
                .attempts(ex.getAttemps())
                .lockedUntil(DateUtils.asDate(ex.getLockedUntil()))
                .message(ex.getMessageCode())
                .build();
        }


        return FailureDTO.builder().message(exception.getMessage()).build();
    }
}
