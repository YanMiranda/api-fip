package org.yan.domain.Marca.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.yan.base.ApiResponse;
import org.yan.domain.Marca.service.MarcaService;

import java.util.Map;

@Path("/cargas/marcas")
@ApplicationScoped
public class MarcaController {

    @Inject
    MarcaService marcaService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response executarCargaManual() {
        int totalCarregado = marcaService.carregarMarcas();
        Map<String, Integer> data = Map.of("totalCarregado", totalCarregado);
        ApiResponse<Map<String, Integer>> apiResponse = new ApiResponse<>(data);

        return Response.ok(apiResponse).type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/fila")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enfileirarCarga() {
        marcaService.enfileirarCargaDeMarcas();
        ApiResponse<Void> apiResponse = new ApiResponse<>("Solicitação de carga de marcas foi enviada para a fila de processamento.");

        return Response.accepted(apiResponse).build();
    }
}