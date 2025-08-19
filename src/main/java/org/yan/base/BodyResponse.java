package org.yan.base;

public class BodyResponse {

    private Object result;
    private Object error;
    private Message messages;
    private boolean tokenExpired = false;

    public BodyResponse() {}

    public BodyResponse(Message messages) {
        this.messages = messages;
    }

    public BodyResponse(Object result, Message messages) {
        this.result = result;
        this.messages = messages;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Message getMessages() {
        return messages;
    }

    public void setMessages(Message messages) {
        this.messages = messages;
    }

    public boolean isTokenExpired() {
        return tokenExpired;
    }

    public void setTokenExpired(boolean tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public static BodyResponse getInstanceTokenExpired(Message messages) {
        BodyResponse body = new BodyResponse();
        body.setMessages(messages);
        body.setTokenExpired(true);
        return body;
    }

    public static BodyResponse buildError(Object error, Message messages) {
        BodyResponse bodyResponse = new BodyResponse();
        bodyResponse.setError(error);
        bodyResponse.setMessages(messages);
        return bodyResponse;
    }

}
