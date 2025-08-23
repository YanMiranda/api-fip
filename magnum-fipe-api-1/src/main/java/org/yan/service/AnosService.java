package org.yan.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.yan.base.BaseService;
import org.yan.client.FipeClient;
import org.yan.domain.model.Modelo.dto.ModeloFipe;
import org.yan.domain.model.anoModelo.dto.AnoFipe;
import org.yan.domain.model.marca.Marca;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AnosService extends BaseService {

    private final FipeClient fipeClient;

    @Inject
    public AnosService(@RestClient FipeClient fipeClient) {
        this.fipeClient = fipeClient;
    }

    public Optional<String> buscarPrimeiroAnoDisponivel(Marca marca, ModeloFipe modeloFipe) {
        return safeExecute(() -> {
            List<AnoFipe> anos = fipeClient.listarAnosPorModelo(marca.getCodigo(), modeloFipe.codigo());
            if (anos == null || anos.isEmpty()) {
                return null;
            }
            return anos.get(0).codigo();
        });
    }
}
