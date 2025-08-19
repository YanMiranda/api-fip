package org.yan.infra;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.yan.base.BodyResponse;
import org.yan.base.Message;
import org.yan.base.ResponseRest;
import org.yan.base.TypeMessage;
import org.yan.util.ResponseUtil;

@Provider
@ApplicationScoped
public class GenericExceptionHandler implements ExceptionMapper<Exception> {

    private static final String EXCEPTION_INTERNA = "org.yan.infra";

    @Inject
    Message msg;

    @Inject
    ResponseUtil responseUtil;


    @Override
    public Response toResponse(Exception ex) {
        boolean isExctApp = isExceptionApplication(ex);

        msg.error(isExctApp ? ex.getLocalizedMessage() : TypeMessage.erroInterno);
        BodyResponse body = isExctApp
                ? new BodyResponse(msg)
                : BodyResponse.buildError(ex.getLocalizedMessage(), msg);

        return responseUtil.buildError500(body).getResponse();
    }

    private boolean isExceptionApplication(Throwable ex) {
        return ex.getClass().getCanonicalName().contains(EXCEPTION_INTERNA);
    }
}

