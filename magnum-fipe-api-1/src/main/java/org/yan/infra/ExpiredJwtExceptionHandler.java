package org.yan.infra;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.yan.base.BodyResponse;
import org.yan.base.Message;
import org.yan.util.ResponseUtil;

@Provider
@ApplicationScoped
public class ExpiredJwtExceptionHandler implements ExceptionMapper<ExpiredJwtException> {

    @Inject
    Message msg;

    @Inject
    ResponseUtil responseUtil;

    @Override
    public Response toResponse(ExpiredJwtException ex) {
        msg.error("Sessão expirada! Favor logar novamente.");
        return responseUtil.buildError400(BodyResponse.getInstanceTokenExpired(msg)).getResponse();
    }
}
