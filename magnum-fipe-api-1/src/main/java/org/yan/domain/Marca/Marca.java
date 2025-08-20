package org.yan.domain.Marca;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MARCAS")
public class Marca extends PanacheEntityBase {

    @Id
    @Column(name = "MA_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "MA_CODIGO", nullable = false, length = 20)
    public String codigo;

    @Column(name = "MA_NOME", nullable = false, length = 100)
    public String nome;
}