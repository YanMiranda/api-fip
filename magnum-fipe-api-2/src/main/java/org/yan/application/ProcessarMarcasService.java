package org.yan.application;

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

@ApplicationScoped
public class ProcessarMarcasService {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessarMarcasService.class);

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Transactional
    public void executar(List<Marca> marcas) {
        if (marcas == null || marcas.isEmpty()) {
            LOG.warn("Lista de marcas recebida está vazia. Nenhuma ação será tomada.");
            return;
        }

        List<Marca> marcasParaProcessar = marcas.stream().limit(3).toList();

        LOG.info("Iniciando processamento de um lote limitado de {}/{} marcas recebidas da fila...",
                marcasParaProcessar.size(), marcas.size());

        List<Modelo> todosOsModelosParaPersistir = new ArrayList<>();
        List<String> marcasComErro = new ArrayList<>();

        for (Marca marca : marcasParaProcessar) {
            try {
                ModelosApiResponse resposta = fipeClient.listarModelosPorMarca(marca.getCodigo());

                if (resposta != null && resposta.modelos() != null && !resposta.modelos().isEmpty()) {
                    List<Modelo> modelosDaMarca = resposta.modelos().stream()
                            .map(modeloFipe -> {
                                Modelo modelo = new Modelo();
                                modelo.setCodigo(modeloFipe.codigo());
                                modelo.setNome(modeloFipe.nome());
                                modelo.setMarca(marca);
                                return modelo;
                            })
                            .toList();
                    todosOsModelosParaPersistir.addAll(modelosDaMarca);
                } else {
                    marcasComErro.add(marca.getNome() + " (sem modelos)");
                }
            } catch (Exception e) {
                marcasComErro.add(marca.getNome() + " (falha na API)");
            }
        }

        if (!todosOsModelosParaPersistir.isEmpty()) {
            LOG.info("Persistindo um total de {} modelos...", todosOsModelosParaPersistir.size());
            Modelo.persist(todosOsModelosParaPersistir);
        }

        logarSumario(marcasParaProcessar.size(), todosOsModelosParaPersistir.size(), marcasComErro);
    }

    private void logarSumario(int totalMarcas, int totalModelos, List<String> erros) {
        LOG.info("-------------------- SUMÁRIO DO PROCESSAMENTO --------------------");
        LOG.info("Marcas recebidas para processamento: {}", totalMarcas);
        LOG.info("Novos modelos persistidos no banco de dados: {}", totalModelos);
        LOG.info("Marcas que resultaram em erro ou não retornaram modelos: {}", erros.size());
        if (!erros.isEmpty()) {
            LOG.warn("Marcas com falha: {}", String.join(", ", erros));
        }
        LOG.info("----------------------------------------------------------------");
    }
}