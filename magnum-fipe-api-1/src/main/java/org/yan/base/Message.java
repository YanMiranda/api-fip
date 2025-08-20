package org.yan.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class Message {

    private List<Alert> itens;

    @JsonIgnore
    private Object targetSource;

    public Message() {
    }

    @PostConstruct
    public void created() {
        itens = new ArrayList<>(1);
    }

    public void warning(final String v) {
        itens.add(Alert.warning(v));
    }

    public void error(final String v) {
        itens.add(Alert.error(v));
    }

    public void success(final String v) {
        itens.add(Alert.success(v));
    }

    public void info(String v) {
        itens.add(Alert.info(v));
    }

    public List<Alert> getList() {
        return itens;
    }

    public void setList(List<Alert> itensList) {
        this.itens = itensList;
    }

    public void clear() {
        itens.clear();
    }
}
