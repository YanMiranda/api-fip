package org.yan.domain.model.veiculo;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.yan.domain.model.Modelo.Modelo;
import org.yan.domain.model.marca.Marca;

@Entity
@Table(name = "VEICULOS")
@Getter
@Setter
@NoArgsConstructor
public class Veiculo extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MA_ID", nullable = false)
    private Marca marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MO_ID", nullable = false)
    private Modelo modelo;

    @Column(name = "VE_ANO_MODELO", nullable = false)
    private int anoModelo;

    @Column(name = "VE_COMBUSTIVEL", length = 50)
    private String combustivel;

    @Column(name = "VE_CODIGO_FIPE", length = 20, unique = true)
    private String codigoFipe;

    @Column(name = "VE_VALOR", length = 50)
    private String valor;

    @Column(name = "VE_OBSERVACOES", length = 500)
    private String observacoes;
}