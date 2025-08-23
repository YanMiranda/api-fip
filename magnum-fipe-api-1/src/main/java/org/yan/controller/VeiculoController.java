package org.yan.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.yan.base.ApiResponse;
import org.yan.domain.model.veiculo.dto.VeiculoResumoDTO;
import org.yan.service.VeiculoService;

import java.util.List;

@Path("/veiculos")
@ApplicationScoped
public class VeiculoController {

    @Inject
    VeiculoService veiculoService;

    @GET
    @Path("/marca/{codigoMarca}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarVeiculosPorMarca(@PathParam("codigoMarca") Integer codigoMarca) {
        List<VeiculoResumoDTO> veiculos = veiculoService.buscarVeiculosPorMarca(codigoMarca);
        ApiResponse<List<VeiculoResumoDTO>> apiResponse = new ApiResponse<>(veiculos);
        return Response.ok(apiResponse).build();
    }


}
