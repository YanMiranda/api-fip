package org.yan.domain.model.Modelo.dto;

import org.yan.domain.model.anoModelo.dto.AnoFipe;

import java.util.List;

public record ModelosApiResponse(
        List<ModeloFipe> modelos,
        List<AnoFipe> anos
) {}
