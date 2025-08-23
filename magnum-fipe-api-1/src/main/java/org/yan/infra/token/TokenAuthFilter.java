package org.yan.infra.token;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Objects;

@Provider
@ApplicationScoped
public class TokenAuthFilter implements ContainerRequestFilter {

    @Inject
    @ConfigProperty(name = "auth.token")
    String expectedToken;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString("Authorization");

        if (Objects.isNull(authHeader) || !authHeader.equals("Bearer " + expectedToken)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Token inválido ou ausente").build());
        }
    }
}

