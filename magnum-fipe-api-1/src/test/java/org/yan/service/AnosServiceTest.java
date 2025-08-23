package org.yan.service;

import org.junit.jupiter.api.Test;
import org.yan.client.FipeClient;
import org.yan.domain.model.Modelo.dto.ModeloFipe;
import org.yan.domain.model.anoModelo.dto.AnoFipe;
import org.yan.domain.model.marca.Marca;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AnosServiceTest {

    private static class StubFipeClient implements FipeClient {
        List<AnoFipe> anos;
        StubFipeClient(List<AnoFipe> anos) { this.anos = anos; }
        @Override public java.util.List<org.yan.domain.model.marca.dto.MarcaFipeResponse> listarMarcas() { return java.util.List.of(); }
        @Override public org.yan.domain.model.veiculo.dto.VeiculoFipeResponse buscarVeiculo(String codigoMarca, Integer codigoModelo, String ano) { return null; }
        @Override public org.yan.domain.model.Modelo.dto.ModelosApiResponse listarModelosPorMarca(String codigoMarca) { return null; }
        @Override public java.util.List<AnoFipe> listarAnosPorModelo(String codigoMarca, Integer codigoModelo) { return anos; }
    }

    @Test
    void deve_retornar_empty_quando_lista_anos_for_nula_ou_vazia() {
        AnosService serviceNulo = new AnosService(new StubFipeClient(null));
        Optional<String> vazio1 = serviceNulo.buscarPrimeiroAnoDisponivel(new Marca(), new ModeloFipe(1, "Teste"));
        assertTrue(vazio1.isEmpty());

        AnosService serviceVazio = new AnosService(new StubFipeClient(List.of()));
        Optional<String> vazio2 = serviceVazio.buscarPrimeiroAnoDisponivel(new Marca(), new ModeloFipe(1, "Teste"));
        assertTrue(vazio2.isEmpty());
    }

    @Test
    void deve_retornar_primeiro_codigo_quando_existirem_anos() {
        List<AnoFipe> anos = List.of(new AnoFipe("2008-1", "2008 Gasolina"), new AnoFipe("2009-1", "2009 Gasolina"));
        AnosService service = new AnosService(new StubFipeClient(anos));
        Optional<String> primeiro = service.buscarPrimeiroAnoDisponivel(new Marca(), new ModeloFipe(1, "Teste"));
        assertTrue(primeiro.isPresent());
        assertEquals("2008-1", primeiro.get());
    }
}
