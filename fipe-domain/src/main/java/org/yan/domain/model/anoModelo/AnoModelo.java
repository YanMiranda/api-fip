package org.yan.domain.model.anoModelo;

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
import org.yan.domain.model.Modelo.Modelo;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ANOS_MODELO")
public class AnoModelo extends PanacheEntityBase {

    @Id
    @Column(name = "AM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "MO_ID", nullable = false)
    public Modelo modelo;

    @Column(name = "AM_CODIGO", nullable = false, length = 20)
    public String codigo;

    @Column(name = "AM_NOME", nullable = false, length = 100)
    public String nome;
}