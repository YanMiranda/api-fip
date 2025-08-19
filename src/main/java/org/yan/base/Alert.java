package org.yan.base;

public class Alert {
    private AlertType type;
    private String value;

    public Alert(AlertType type, String value) {
        this.type = type;
        this.value = value;
    }

    public AlertType getType() {
        return type;
    }

    public void setType(AlertType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static Alert warning(final String v) {
        return new Alert(AlertType.WARNING, v);
    }

    public static Alert error(final String v) {
        return new Alert(AlertType.ERROR, v);
    }

    public static Alert info(final String v) {
        return new Alert(AlertType.INFO, v);
    }

    public static Alert success(final String v) {
        return new Alert(AlertType.SUCCESS, v);
    }
}
