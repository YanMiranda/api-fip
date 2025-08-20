package org.yan.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.yan.domain.Marca.dto.MarcaFipeResponse;
import org.yan.domain.Veiculo.dto.VeiculoDTO;

import java.util.List;

@Path("/carros")
@RegisterRestClient(configKey = "fipe-api")
public interface FipeClient {

    @GET
    @Path("/marcas")
    List<MarcaFipeResponse> listarMarcas();

    @GET
    @Path("/marcas/{codigoMarca}/modelos/{codigoModelo}/anos/{ano}")
    VeiculoDTO buscarVeiculo(
            @PathParam("codigoMarca") String codigoMarca,
            @PathParam("codigoModelo") String codigoModelo,
            @PathParam("ano") String ano
    );
}