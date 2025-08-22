package org.yan.infra;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yan.base.ApiResponse;

@Provider
@ApplicationScoped
public class GenericExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger LOG = LoggerFactory.getLogger(GenericExceptionHandler.class);

    @Override
    public Response toResponse(Exception ex) {
        LOG.error("Ocorreu um erro inesperado na aplicação", ex);

        String mensagem = "Ocorreu um erro interno no servidor. Tente novamente mais tarde.";
        ApiResponse<Void> apiResponse = new ApiResponse<>(mensagem);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(apiResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
