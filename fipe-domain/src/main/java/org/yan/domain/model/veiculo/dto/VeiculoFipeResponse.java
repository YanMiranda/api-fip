package org.yan.domain.model.veiculo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VeiculoFipeResponse(
        @JsonProperty("Valor") String valor,
        @JsonProperty("Marca") String marca,
        @JsonProperty("Modelo") String modelo,
        @JsonProperty("AnoModelo") int anoModelo,
        @JsonProperty("Combustivel") String combustivel,
        @JsonProperty("CodigoFipe") String codigoFipe,
        @JsonProperty("MesReferencia") String mesReferencia,
        @JsonProperty("TipoVeiculo") int tipoVeiculo,
        @JsonProperty("SiglaCombustivel") String siglaCombustivel
) {}