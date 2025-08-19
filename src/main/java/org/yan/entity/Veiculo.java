package org.yan.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "VEICULOS")
public class Veiculo extends PanacheEntityBase {

    @Id
    @Column(name = "VE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "AM_ID", nullable = false)
    public AnoModelo anoModelo;

    @Column(name = "VE_TIPO_VEICULO", nullable = false)
    public Integer tipoVeiculo;

    @Column(name = "VE_VALOR", nullable = false, length = 50)
    public String valor;

    @Column(name = "VE_MARCA", nullable = false, length = 100)
    public String marca;

    @Column(name = "VE_MODELO", nullable = false, length = 150)
    public String modelo;

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