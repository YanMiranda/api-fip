package org.yan.service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yan.client.FipeClient;
import org.yan.domain.model.Modelo.Modelo;
import org.yan.domain.model.Modelo.dto.ModelosApiResponse;
import org.yan.domain.model.marca.Marca;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ProcessarMarcasService {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessarMarcasService.class);

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Transactional
    public void executar(List<Marca> marcasDaFila) {
        if (Objects.isNull(marcasDaFila) || marcasDaFila.isEmpty()) {
            LOG.warn("Lista de marcas recebida está vazia. Nenhuma ação será tomada.");
            return;
        }

        List<Marca> marcasParaProcessar = marcasDaFila.stream().limit(1).toList();
        LOG.info("Iniciando processamento de um lote limitado de {}/{} marcas recebidas da fila...",
                marcasParaProcessar.size(), marcasDaFila.size());

        int totalModelosSalvos = 0;
        int marcasSalvas = 0;
        List<String> marcasComErro = new ArrayList<>();

        for (Marca marca : marcasParaProcessar) {
            try {
                ModelosApiResponse resposta = fipeClient.listarModelosPorMarca(marca.getCodigo());

                if (Objects.nonNull(resposta) && Objects.nonNull(resposta.modelos()) && !resposta.modelos().isEmpty()) {

                    marca.persist();
                    marcasSalvas++;

                    List<Modelo> modelosDaMarca = resposta.modelos().stream()
                            .map(modeloFipe -> {
                                Modelo modelo = new Modelo();
                                modelo.setCodigo(modeloFipe.codigo());
                                modelo.setNome(modeloFipe.nome());
                                modelo.setMarca(marca);
                                return modelo;
                            })
                            .toList();

                    Modelo.persist(modelosDaMarca);
                    totalModelosSalvos += modelosDaMarca.size();

                } else {
                    marcasComErro.add(marca.getNome() + " (sem modelos na FIPE)");
                }
            } catch (Exception e) {
                LOG.error("Erro na chamada da API FIPE para a marca {}: {}", marca.getNome(), e.getMessage());
                marcasComErro.add(marca.getNome() + " (falha na API)");
            }
        }

        logarSumario(marcasParaProcessar.size(), marcasSalvas, totalModelosSalvos, marcasComErro);
    }

    private void logarSumario(int totalMarcasProcessadas, int totalMarcasSalvas, int totalModelosSalvos, List<String> erros) {
        LOG.info("-------------------- SUMÁRIO DO PROCESSAMENTO --------------------");
        LOG.info("Marcas recebidas para processamento: {}", totalMarcasProcessadas);
        LOG.info("Marcas novas persistidas no banco: {}", totalMarcasSalvas);
        LOG.info("Modelos novos persistidos no banco: {}", totalModelosSalvos);
        LOG.info("Marcas que resultaram em erro ou não retornaram modelos: {}", erros.size());
        if (!erros.isEmpty()) {
            LOG.warn("Marcas com falha ou sem dados: {}", String.join(", ", erros));
        }
        LOG.info("----------------------------------------------------------------");
    }
}