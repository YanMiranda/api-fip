package org.yan.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.yan.base.ApiResponse;
import org.yan.service.MarcaService;
import org.yan.domain.model.marca.Marca;

import java.util.List;

@Path("/marcas")
@ApplicationScoped
public class MarcaController {

    @Inject
    MarcaService marcaService;

    @POST
    @Path("/carga/manual")
    @Produces(MediaType.APPLICATION_JSON)
    public Response executarCargaManual() {
        String sumario = marcaService.executarCargaCompleta();
        ApiResponse<String> apiResponse = new ApiResponse<>(sumario);
        return Response.ok(apiResponse).build();
    }

    @POST
    @Path("/fila/para/api2")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enfileirarCarga() {
        marcaService.enfileirarCargaDeMarcas();
        ApiResponse<Void> apiResponse = new ApiResponse<>("Solicitação de carga de marcas foi enviada para a fila de processamento.");

        return Response.accepted(apiResponse).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarMarcasDoBanco() {
        List<Marca> marcas = marcaService.listarTodas();
        ApiResponse<List<Marca>> apiResponse = new ApiResponse<>(marcas);
        return Response.ok(apiResponse).build();
    }
}