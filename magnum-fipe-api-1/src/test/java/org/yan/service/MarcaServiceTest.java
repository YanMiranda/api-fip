package org.yan.service;

import com.fasterxml.jackson.core.type.TypeReference;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.Test;
import org.yan.client.FipeClient;
import org.yan.domain.converter.MarcaConverter;
import org.yan.domain.model.marca.Marca;
import org.yan.domain.model.marca.dto.MarcaFipeResponse;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MarcaServiceTest {

    @Test
    void enfileirar_usa_cache_quando_disponivel() {
        // Mocks
        FipeClient fipe = mock(FipeClient.class);
        @SuppressWarnings("unchecked")
        Emitter<List<Marca>> emitter = mock(Emitter.class);
        MarcaConverter converter = new MarcaConverter();
        RedisDataSource ds = mock(RedisDataSource.class);
        @SuppressWarnings("unchecked")
        ValueCommands<String, List<MarcaFipeResponse>> cache = mock(ValueCommands.class);
        when(ds.value(any(TypeReference.class))).thenReturn(cache);

        // Cache já preenchido
        List<MarcaFipeResponse> cacheList = List.of(new MarcaFipeResponse("1", "Marca1"), new MarcaFipeResponse("2", "Marca2"));
        when(cache.get("fipe:marcas")).thenReturn(cacheList);

        // Captura do envio
        AtomicReference<List<Marca>> enviado = new AtomicReference<>();
        doAnswer(inv -> { enviado.set(inv.getArgument(0)); return null; }).when(emitter).send((List<Marca>) any());

        VeiculoService veiculoService = mock(VeiculoService.class);
        MarcaService service = new MarcaService(fipe, emitter, converter, ds, veiculoService);
        service.enfileirarCargaDeMarcas();

        assertNotNull(enviado.get());
        assertEquals(2, enviado.get().size());
        verify(fipe, never()).listarMarcas();
    }

    @Test
    void enfileirar_busca_fipe_e_salva_cache_quando_cache_vazio() {
        // Mocks
        FipeClient fipe = mock(FipeClient.class);
        @SuppressWarnings("unchecked")
        Emitter<List<Marca>> emitter = mock(Emitter.class);
        MarcaConverter converter = new MarcaConverter();
        RedisDataSource ds = mock(RedisDataSource.class);
        @SuppressWarnings("unchecked")
        ValueCommands<String, List<MarcaFipeResponse>> cache = mock(ValueCommands.class);
        when(ds.value(any(TypeReference.class))).thenReturn(cache);

        // Cache vazio -> null
        when(cache.get("fipe:marcas")).thenReturn(null);

        // Fipe retorna lista
        List<MarcaFipeResponse> fipeList = List.of(new MarcaFipeResponse("3", "Marca3"));
        when(fipe.listarMarcas()).thenReturn(fipeList);

        AtomicReference<List<Marca>> enviado = new AtomicReference<>();
        doAnswer(inv -> { enviado.set(inv.getArgument(0)); return null; }).when(emitter).send((List<Marca>) any());

        VeiculoService veiculoService = mock(VeiculoService.class);
        MarcaService service = new MarcaService(fipe, emitter, converter, ds, veiculoService);
        service.enfileirarCargaDeMarcas();

        assertNotNull(enviado.get());
        assertEquals(1, enviado.get().size());
        verify(cache, atLeastOnce()).setex(eq("fipe:marcas"), anyLong(), eq(fipeList));
        verify(fipe, times(1)).listarMarcas();
    }
}
