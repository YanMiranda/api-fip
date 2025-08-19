package org.yan.infra;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;
import org.yan.base.BodyResponse;
import org.yan.base.Message;
import org.yan.util.ResponseUtil;

@Provider
@ApplicationScoped
public class ValidationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    @Inject
    Message msg;

    @Inject
    ResponseUtil responseUtil;

    @Override
    public Response toResponse(ConstraintViolationException ex) {
        ex.getConstraintViolations().forEach(error -> msg.error(error.getMessage()));
        BodyResponse body = new BodyResponse(msg);
        return responseUtil.buildError400(body).getResponse();
    }
}

