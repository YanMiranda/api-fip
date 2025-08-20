package org.yan.domain.AnoModelo;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.yan.domain.Modelo.Modelo;

@Entity
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