package org.yan.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.yan.domain.model.Modelo.dto.ModelosApiResponse;
import org.yan.domain.model.marca.dto.MarcaFipeResponse;
import org.yan.domain.model.veiculo.dto.VeiculoDTO;

import java.util.List;

@Path("/carros")
@RegisterRestClient(configKey = "fipe-api")
@ClientHeaderParam(name = "X-API-KEY", value = "{getApiKey}")
public interface FipeClient {

    default String getApiKey() {
        return ConfigProvider.getConfig().getValue("fipe.api.key", String.class);
    }

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

    @GET
    @Path("/{codigoMarca}/modelos")
    ModelosApiResponse listarModelosPorMarca(@PathParam("codigoMarca") String codigoMarca);
}