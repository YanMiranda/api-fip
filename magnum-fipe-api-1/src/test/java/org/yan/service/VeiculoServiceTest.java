package org.yan.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yan.client.FipeClient;
import org.yan.domain.model.Modelo.dto.ModeloFipe;
import org.yan.domain.model.Modelo.dto.ModelosApiResponse;
import org.yan.domain.model.marca.Marca;
import org.yan.domain.model.veiculo.Veiculo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VeiculoServiceTest {

    FipeClient fipeClient;
    AnosService anosService;
    VeiculoService service;

    @BeforeEach
    void setup() {
        fipeClient = mock(FipeClient.class);
        anosService = mock(AnosService.class);
        service = new VeiculoService(fipeClient, anosService);
    }

    @Test
    void deve_retornar_lista_vazia_quando_modelos_nao_existirem() {
        Marca marca = new Marca();
        marca.setCodigo("1");
        when(fipeClient.listarModelosPorMarca("1")).thenReturn(null);
        List<Veiculo> v1 = service.buscarVeiculosPorModelosApiFipe(marca);
        assertNotNull(v1);
        assertTrue(v1.isEmpty());

        when(fipeClient.listarModelosPorMarca("1")).thenReturn(new ModelosApiResponse(new ArrayList<>(), new ArrayList<>()));
        List<Veiculo> v2 = service.buscarVeiculosPorModelosApiFipe(marca);
        assertNotNull(v2);
        assertTrue(v2.isEmpty());
    }

    @Test
    void deve_retornar_lista_vazia_quando_primeiro_ano_nao_disponivel() {
        Marca marca = new Marca();
        marca.setCodigo("1");
        List<ModeloFipe> modelos = List.of(new ModeloFipe(10, "A"));
        ModelosApiResponse resp = new ModelosApiResponse(modelos, new ArrayList<>());
        when(fipeClient.listarModelosPorMarca("1")).thenReturn(resp);
        when(anosService.buscarPrimeiroAnoDisponivel(any(), any())).thenReturn(Optional.empty());
        List<Veiculo> v = service.buscarVeiculosPorModelosApiFipe(marca);
        assertNotNull(v);
        assertTrue(v.isEmpty());
    }
}
