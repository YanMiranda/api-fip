package org.yan.domain.Marca.controller;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.yan.client.FipeClient;
import org.yan.domain.Marca.Marca;
import org.yan.domain.Marca.dto.MarcaFipeResponse;

import java.util.List;


@Startup
@ApplicationScoped
public class MarcaInitializer {

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Transactional
    void inicializarMarcas(@Observes StartupEvent ev) {
        long count = Marca.count();

        if (count == 0) {
            List<MarcaFipeResponse> marcas = fipeClient.listarMarcas();
            for (MarcaFipeResponse dto : marcas) {
                Marca marca = new Marca();
                marca.codigo = dto.codigo();
                marca.nome = dto.nome();
                marca.persist();
            }
            System.out.println("✅ Marcas FIPE carregadas com sucesso: " + marcas.size());
        } else {
            System.out.println("ℹ️ Tabela de Marcas já possui registros, carga inicial ignorada.");
        }
    }
}
