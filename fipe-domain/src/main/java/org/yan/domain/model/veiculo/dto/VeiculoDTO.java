package org.yan.domain.model.veiculo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VeiculoDTO( Long id, Integer tipoVeiculo, String valor, String marca, String modelo, Integer ano,
                          String combustivel, String codigoFipe, String mesReferencia, String siglaCombustivel,
                          Long anoModeloId) {}