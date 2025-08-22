package org.yan.infra;

import org.yan.exception.BusinessException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.yan.base.ApiResponse;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.enterprise.context.ApplicationScoped;

@Provider
@ApplicationScoped
public class BusinessExceptionHandler implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException ex) {
        ApiResponse<Void> apiResponse = new ApiResponse<>(ex.getMessage());

        return Response.status(Response.Status.CONFLICT)
                .entity(apiResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}