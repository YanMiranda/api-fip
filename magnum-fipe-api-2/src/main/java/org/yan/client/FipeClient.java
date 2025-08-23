package org.yan.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.yan.domain.model.Modelo.dto.ModelosApiResponse;
import org.yan.domain.model.marca.dto.MarcaFipeResponse;
import org.yan.domain.model.veiculo.dto.VeiculoFipeResponse;

import java.util.List;

@Path("/carros/marcas/")
@RegisterRestClient(configKey = "fipe-api")
@ClientHeaderParam(name = "X-API-KEY", value = "{getApiKey}")
public interface FipeClient {

    default String getApiKey() {
        return ConfigProvider.getConfig().getValue("fipe.api.key", String.class);
    }

    @GET
    @Path("/carros/marcas")
    List<MarcaFipeResponse> listarMarcas();

    @GET
    @Path("/carros/marcas/{codigoMarca}/modelos")
    ModelosApiResponse listarModelosPorMarca(@PathParam("codigoMarca") String codigoMarca);

    @GET
    @Path("/carros/marcas/{codigoMarca}/modelos/{codigoModelo}/anos/{ano}")
    VeiculoFipeResponse buscarVeiculo(
            @PathParam("codigoMarca") String codigoMarca,
            @PathParam("codigoModelo") String codigoModelo,
            @PathParam("ano") String ano
    );
}