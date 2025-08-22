package org.yan.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yan.domain.model.marca.Marca;

import java.util.List;

@ApplicationScoped
public class ProcessarMarcasService {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessarMarcasService.class);

    @Transactional
    public void executar(List<Marca> marcas) {
        if (marcas == null || marcas.isEmpty()) {
            LOG.warn("Lista de marcas recebida para processamento está vazia. Nenhuma ação será tomada.");
            return;
        }

        LOG.info("Iniciando persistência de {} marcas recebidas da fila...", marcas.size());

        Marca.persist(marcas);

        LOG.info("Persistência de {} marcas concluída com sucesso.", marcas.size());
    }
}