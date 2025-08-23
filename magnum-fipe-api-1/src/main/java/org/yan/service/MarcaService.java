package org.yan.service;

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
import org.yan.domain.model.veiculo.Veiculo;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class MarcaService {

    private static final Logger LOG = LoggerFactory.getLogger(MarcaService.class);
    private static final String CACHE_KEY_MARCAS = "fipe:marcas";

    private final FipeClient fipeClient;
    private final Emitter<List<Marca>> marcaEmitter;
    private final MarcaConverter marcaConverter;
    private final ValueCommands<String, List<MarcaFipeResponse>> cacheMarcas;
    private final VeiculoService veiculoService;

    public MarcaService(@RestClient FipeClient fipeClient,
                        @Channel("marcas-out") Emitter<List<Marca>> marcaEmitter,
                        MarcaConverter marcaConverter,
                        RedisDataSource ds,
                        VeiculoService veiculoService) {
        this.fipeClient = fipeClient;
        this.marcaEmitter = marcaEmitter;
        this.marcaConverter = marcaConverter;
        this.cacheMarcas = ds.value(new TypeReference<>() {});
        this.veiculoService = veiculoService;
    }

    @Transactional
    public String executarCargaCompleta() {
        sincronizarMarcasComFipe();

        Set<String> codigosMarcasAlvo = Set.of("7", "171", "211");
        List<Marca> marcasAlvo = Marca.list("codigo in ?1", codigosMarcasAlvo);

        List<Veiculo> novosVeiculos = marcasAlvo.stream()
                .flatMap(marca -> veiculoService.buscarVeiculosPorModelosApiFipe(marca).stream())
                .toList();

        if (!novosVeiculos.isEmpty()) {
            veiculoService.salvarLista(novosVeiculos);
        }

        String resumo = String.format("Carga concluída. Veículos novos salvos: %d", novosVeiculos.size());
        LOG.info(resumo);
        return resumo;
    }

    public void enfileirarCargaDeMarcas() {
        List<MarcaFipeResponse> marcasDaFipe = buscarMarcasDaFipeComCache();
        List<Marca> marcasParaFila = marcaConverter.toEntityList(marcasDaFipe);

        if (Objects.isNull(marcasParaFila) || marcasParaFila.isEmpty()) {
            LOG.warn("Nenhuma marca encontrada na FIPE para enfileirar.");
            return;
        }

        LOG.info("Enviando {} marcas para a fila...", marcasParaFila.size());
        marcaEmitter.send(marcasParaFila);
    }

    public List<Marca> listarTodas() {
        return Marca.listAll();
    }

    private void sincronizarMarcasComFipe() {
        List<MarcaFipeResponse> marcasDaFipe = buscarMarcasDaFipeComCache();
        if (Objects.isNull(marcasDaFipe) || marcasDaFipe.isEmpty()) {
            LOG.warn("Nenhuma marca encontrada na FIPE para sincronizar.");
            return;
        }

        Set<String> codigosExistentes = Marca.<Marca>listAll().stream()
                .map(Marca::getCodigo)
                .collect(Collectors.toSet());

        List<Marca> novasMarcas = marcasDaFipe.stream()
                .filter(m -> !codigosExistentes.contains(m.codigo()))
                .map(marcaConverter::toEntity)
                .toList();

        if (!novasMarcas.isEmpty()) {
            Marca.persist(novasMarcas);
            LOG.info("{} novas marcas foram persistidas.", novasMarcas.size());
        }
    }


    private List<MarcaFipeResponse> buscarMarcasDaFipeComCache() {
        List<MarcaFipeResponse> cache = cacheMarcas.get(CACHE_KEY_MARCAS);
        if (Objects.nonNull(cache)) return cache;

        LOG.debug("Cache de marcas vazio. Buscando na FIPE...");
        List<MarcaFipeResponse> marcas = fipeClient.listarMarcas();
        if (Objects.nonNull(marcas) && !marcas.isEmpty()) {
            cacheMarcas.setex(CACHE_KEY_MARCAS, Duration.ofHours(24).getSeconds(), marcas);
            LOG.debug("Marcas salvas no cache por 24h.");
        }
        return marcas;
    }

}
