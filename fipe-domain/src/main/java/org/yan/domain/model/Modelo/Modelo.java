package org.yan.domain.model.Modelo;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yan.domain.model.marca.Marca;

@Data
@Entity
@NoArgsConstructor
@Table(name = "MODELOS")
public class Modelo extends PanacheEntityBase {

    @Id
    @Column(name = "MO_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "MA_ID", nullable = false)
    public Marca marca;

    @Column(name = "MO_CODIGO", nullable = false)
    public Integer codigo;

    @Column(name = "MO_NOME", nullable = false, length = 150)
    public String nome;
}

