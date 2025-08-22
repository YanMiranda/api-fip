package org.yan.domain.Marca.service;

import com.fasterxml.jackson.core.type.TypeReference;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
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
import org.yan.domain.model.marca.Marca;
import org.yan.domain.model.marca.dto.MarcaFipeResponse;
import org.yan.exception.BusinessException;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class MarcaService {

    private static final Logger LOG = LoggerFactory.getLogger(MarcaService.class);
    private static final String CACHE_KEY = "fipe:marcas";

    private final FipeClient fipeClient;
    private final Emitter<List<Marca>> marcaEmitter;
    private final MarcaConverter marcaConverter;
    private final ValueCommands<String, List<MarcaFipeResponse>> cache;

    @Inject
    public MarcaService(RedisDataSource ds, @RestClient FipeClient fipeClient,
                        @Channel("marcas-out") Emitter<List<Marca>> marcaEmitter,
                        MarcaConverter marcaConverter) {
        this.fipeClient = fipeClient;
        this.marcaEmitter = marcaEmitter;
        this.marcaConverter = marcaConverter;
        this.cache = ds.value(new TypeReference<List<MarcaFipeResponse>>(){});
    }

    @Transactional
    public int carregarMarcas() throws BusinessException {
        if (Marca.count() > 0) {
            LOG.info("Carga de marcas ignorada, pois a tabela já contém dados.");
            throw new BusinessException("Tabela de Marcas já possui registros, carga ignorada.");
        }

        List<MarcaFipeResponse> marcasDaFipe = buscarMarcasComCache();
        List<Marca> novasMarcas = marcaConverter.toEntityList(marcasDaFipe);

        return persistirMarcas(novasMarcas);
    }

    public void enfileirarCargaDeMarcas() {
        List<MarcaFipeResponse> marcasDaFipe = buscarMarcasComCache();
        List<Marca> marcasParaFila = marcaConverter.toEntityList(marcasDaFipe);

        if (marcasParaFila.isEmpty()) {
            LOG.warn("Nenhuma marca encontrada na FIPE para enfileirar.");
            return;
        }

        LOG.info("Enviando {} marcas para a fila...", marcasParaFila.size());
        marcaEmitter.send(marcasParaFila);
        LOG.info("Envio para a fila concluído.");
    }

    private List<MarcaFipeResponse> buscarMarcasComCache() {
        List<MarcaFipeResponse> marcasEmCache = cache.get(CACHE_KEY);
        if (Objects.nonNull(marcasEmCache)) {
            LOG.info("Marcas encontradas no cache Redis. Nenhuma chamada à API FIPE será feita.");
            return marcasEmCache;
        }

        LOG.info("Cache de marcas vazio. Buscando na API FIPE...");
        List<MarcaFipeResponse> marcasDaFipe = fipeClient.listarMarcas();

        if (Objects.nonNull(marcasDaFipe) && !marcasDaFipe.isEmpty()) {
            cache.setex(CACHE_KEY, Duration.ofHours(24).getSeconds(), marcasDaFipe);
            LOG.info("Lista de marcas salva no cache Redis por 24 horas.");
        }

        return marcasDaFipe;
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