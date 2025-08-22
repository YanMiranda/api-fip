package org.yan.infra;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.yan.base.ApiResponse;

import java.util.Map;
import java.util.stream.Collectors;

@Provider
@ApplicationScoped
public class ValidationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        cv -> cv.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));

        ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>("Erro de validação.", errors);

        return Response.status(422)
                .entity(apiResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
