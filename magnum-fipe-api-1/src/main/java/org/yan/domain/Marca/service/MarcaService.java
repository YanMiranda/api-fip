package org.yan.domain.Marca.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yan.client.FipeClient;
import org.yan.domain.converter.MarcaConverter;
import org.yan.exception.BusinessException;
import org.yan.domain.model.marca.Marca;
import org.yan.domain.model.marca.dto.MarcaFipeResponse;

import java.util.List;

@ApplicationScoped
public class MarcaService {

    private static final Logger LOG = LoggerFactory.getLogger(MarcaService.class);

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Inject
    @Channel("marcas-out")
    Emitter<List<Marca>> marcaEmitter;

    @Inject
    MarcaConverter marcaConverter;

    @Transactional
    public int carregarMarcas() throws BusinessException {
        if (Marca.count() > 0) {
            LOG.info("Carga de marcas ignorada, pois a tabela já contém dados.");
            throw new BusinessException("Tabela de Marcas já possui registros, carga ignorada.");
        }

        List<Marca> novasMarcas = buscarMarcasDaFipe();
        return persistirMarcas(novasMarcas);
    }

    public void enfileirarCargaDeMarcas() {
        List<Marca> marcasParaFila = buscarMarcasDaFipe();

        if (marcasParaFila.isEmpty()) {
            LOG.warn("Nenhuma marca encontrada na FIPE para enfileirar.");
            return;
        }

        LOG.info("Enviando {} marcas para a fila...", marcasParaFila.size());
        marcaEmitter.send(marcasParaFila);
        LOG.info("Envio para a fila concluído.");
    }

    private List<Marca> buscarMarcasDaFipe() {
        LOG.info("Iniciando busca de marcas na API FIPE...");
        List<MarcaFipeResponse> marcasDaFipe = fipeClient.listarMarcas();

        return marcaConverter.toEntityList(marcasDaFipe);
    }

    private int persistirMarcas(List<Marca> marcas) {
        if (marcas.isEmpty()) {
            LOG.warn("Nenhuma marca para persistir.");
            return 0;
        }

        LOG.info("{} marcas encontradas. Iniciando processo de persistência...", marcas.size());
        Marca.persist(marcas);
        LOG.info("Persistência em lote concluída com sucesso.");
        return marcas.size();
    }
}