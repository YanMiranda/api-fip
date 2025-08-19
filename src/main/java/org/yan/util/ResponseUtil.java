package org.yan.util;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.yan.base.BodyResponse;
import org.yan.base.Message;
import org.yan.base.ResponseRest;

@ApplicationScoped
public class ResponseUtil {

    @Inject
    Message msg;

    public ResponseRest buildError(String message) {
        msg.error(message);
        return build(null, Response.Status.BAD_REQUEST);
    }

    public ResponseRest buildError(String erro, String message) {
        msg.error(message);
        return build(BodyResponse.buildError(erro, msg), Response.Status.BAD_REQUEST);
    }

    public ResponseRest buildError500(Object item) {
        return ResponseRest.build(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(item)
                .type(MediaType.APPLICATION_JSON)
                .build());
    }

    public ResponseRest buildError400(Object item) {
        return ResponseRest.build(Response.status(Response.Status.BAD_REQUEST)
                .entity(item)
                .type(MediaType.APPLICATION_JSON)
                .build());
    }

    public ResponseRest buildSuccess(Object item) {
        return build(item, Response.Status.OK);
    }

    public ResponseRest buildSuccess(String message) {
        msg.success(message);
        return build(null, Response.Status.OK);
    }

    public ResponseRest buildSuccess(Object item, String message) {
        msg.success(message);
        return build(item, Response.Status.OK);
    }

    public ResponseRest buildSuccess() {
        return build(null, Response.Status.OK);
    }

    public ResponseRest buildNotFound() {
        return ResponseRest.build(Response.status(Response.Status.NOT_FOUND).build());
    }

    private ResponseRest build(Object item, Response.Status status) {
        return ResponseRest.build(Response.status(status)
                .entity(new BodyResponse(item, msg))
                .type(MediaType.APPLICATION_JSON)
                .build());
    }
}
