package org.yan.domain.converter;

import jakarta.enterprise.context.ApplicationScoped;
import org.yan.domain.model.marca.Marca;
import org.yan.domain.model.marca.dto.MarcaFipeResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class MarcaConverter {

    public List<Marca> toEntityList(List<MarcaFipeResponse> dtos) {
        return Optional.ofNullable(dtos)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public Marca toEntity(MarcaFipeResponse dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        Marca entity = new Marca();
        entity.setCodigo(dto.codigo());
        entity.setNome(dto.nome());
        return entity;
    }
}
