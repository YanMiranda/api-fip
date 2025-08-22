package org.yan.domain.model.veiculo;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;
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
import org.yan.domain.model.anoModelo.AnoModelo;

@Data
@Entity
@NoArgsConstructor
@Table(name = "VEICULOS")
public class Veiculo extends PanacheEntityBase {

    @Id
    @Column(name = "VE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "AM_ID", nullable = false)
    public AnoModelo anoModelo;

    @NotNull
    @Column(name = "VE_TIPO_VEICULO", nullable = false)
    public Integer tipoVeiculo;

    @NotNull
    @Column(name = "VE_VALOR", nullable = false, length = 50)
    public String valor;

    @NotNull
    @Column(name = "VE_MARCA", nullable = false, length = 100)
    public String marca;

    @NotNull
    @Column(name = "VE_MODELO", nullable = false, length = 150)
    public String modelo;

    @NotNull
    @Column(name = "VE_ANO", nullable = false)
    public Integer ano;

    @Column(name = "VE_COMBUSTIVEL", length = 50)
    public String combustivel;

    @Column(name = "VE_CODIGO_FIPE", length = 20)
    public String codigoFipe;

    @Column(name = "VE_MES_REFERENCIA", length = 50)
    public String mesReferencia;

    @Column(name = "VE_SIGLA_COMBUSTIVEL", length = 5)
    public String siglaCombustivel;
}