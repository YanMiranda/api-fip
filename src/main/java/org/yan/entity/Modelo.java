package org.yan.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "MODELOS")
public class Modelo extends PanacheEntityBase {

    @Id
    @Column(name = "mo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "ma_id", nullable = false)
    public Marca marca;

    @Column(name = "mo_codigo", nullable = false, length = 20)
    public String codigo;

    @Column(name = "mo_nome", nullable = false, length = 150)
    public String nome;
}

