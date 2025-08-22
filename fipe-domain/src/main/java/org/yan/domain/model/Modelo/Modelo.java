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

