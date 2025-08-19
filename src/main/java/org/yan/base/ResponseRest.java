package org.yan.base;

import jakarta.ws.rs.core.Response;

public class ResponseRest {
    private Response response;

    public ResponseRest() {}

    public ResponseRest(Response response) {
        this.response = response;
    }

    public static ResponseRest build(Response response) {
        return new ResponseRest(response);
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}


