package org.yan.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.yan.base.BaseService;
import org.yan.client.FipeClient;
import org.yan.domain.model.Modelo.Modelo;
import org.yan.domain.model.Modelo.dto.ModeloFipe;
import org.yan.domain.model.Modelo.dto.ModelosApiResponse;
import org.yan.domain.model.marca.Marca;
import org.yan.domain.model.veiculo.Veiculo;
import org.yan.domain.model.veiculo.dto.VeiculoFipeResponse;
import org.yan.domain.model.veiculo.dto.VeiculoResumoDTO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class VeiculoService extends BaseService {

    private final FipeClient fipeClient;
    private final AnosService anosService;

    @Inject
    public VeiculoService(@RestClient FipeClient fipeClient, AnosService anosService) {
        this.fipeClient = fipeClient;
        this.anosService = anosService;
    }

    public List<VeiculoResumoDTO> buscarVeiculosPorMarca(Integer codigoMarca) {
        return safeExecute(() -> {
            Marca marca = (Marca) Marca.find("codigo", codigoMarca).firstResultOptional()
                    .orElseThrow(() -> new IllegalArgumentException("Marca com código " + codigoMarca + " não encontrada no banco de dados."));

            return buscarVeiculosPorModelosBD(marca);
        }).orElse(List.of());
    }

    private List<VeiculoResumoDTO> buscarVeiculosPorModelosBD(Marca marca) {
        List<Veiculo> veiculosEncontrados = Veiculo.list("modelo.marca", marca);

        return veiculosEncontrados.stream()
                .map(veiculo -> new VeiculoResumoDTO(
                        veiculo.getCodigoFipe(),
                        veiculo.getModelo().getNome(),
                        veiculo.getObservacoes()
                )).collect(Collectors.toList());
    }


    public List<Veiculo> buscarVeiculosPorModelosApiFipe(Marca marca) {
        final int limiteModelos = 2;

        return safeExecute(() -> {
            ModelosApiResponse resposta = fipeClient.listarModelosPorMarca(marca.getCodigo());
            if (Objects.isNull(resposta) || Objects.isNull(resposta.modelos()) || resposta.modelos().isEmpty()) {
                return List.<Veiculo>of();
            }

            Optional<String> primeiroAnoOpt = anosService.buscarPrimeiroAnoDisponivel(marca, resposta.modelos().get(0));
            if (primeiroAnoOpt.isEmpty()) {
                return List.<Veiculo>of();
            }
            String primeiroAno = primeiroAnoOpt.get();

            return resposta.modelos().stream()
                    .limit(limiteModelos)
                    .map(modeloFipe -> buscarEConverterVeiculo(marca, modeloFipe, primeiroAno))
                    .flatMap(Optional::stream)
                    .toList();
        }).orElse(List.of());
    }

    private Optional<Veiculo> buscarEConverterVeiculo(Marca marca, ModeloFipe modeloFipe, String ano) {
        return safeExecute(() -> {
            Modelo modelo = findOrCreateModelo(marca, modeloFipe);

            VeiculoFipeResponse veiculoFipe = fipeClient.buscarVeiculo(
                    marca.getCodigo(),
                    modelo.getCodigo(),
                    ano
            );

            if (Objects.isNull(veiculoFipe) || Veiculo.find("codigoFipe", veiculoFipe.codigoFipe()).firstResultOptional().isPresent()) {
                return null;
            }
            return converterVeiculo(veiculoFipe, marca, modelo);
        });
    }

    private Modelo findOrCreateModelo(Marca marca, ModeloFipe modeloFipe) {
        return (Modelo) Modelo.find("codigo = ?1 and marca = ?2", modeloFipe.codigo(), marca).firstResultOptional()
                .orElseGet(() -> {
                    Modelo novo = new Modelo();
                    novo.setCodigo(modeloFipe.codigo());
                    novo.setNome(modeloFipe.nome());
                    novo.setMarca(marca);
                    novo.persist();
                    return novo;
                });
    }

    private Veiculo converterVeiculo(VeiculoFipeResponse dto, Marca marca, Modelo modelo) {
        Veiculo v = new Veiculo();
        v.setMarca(marca);
        v.setModelo(modelo);
        v.setAnoModelo(dto.anoModelo());
        v.setCombustivel(dto.combustivel());
        v.setCodigoFipe(dto.codigoFipe());
        v.setValor(dto.valor());
        return v;
    }

    public void salvarLista(List<Veiculo> veiculos) {
        if(Objects.nonNull(veiculos) && !veiculos.isEmpty()){
            Veiculo.persist(veiculos);
        }
    }
}