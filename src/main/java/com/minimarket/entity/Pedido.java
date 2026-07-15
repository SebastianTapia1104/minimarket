package com.minimarket.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.hateoas.server.core.Relation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Relation(collectionRelation = "pedidos", itemRelation = "pedido")
@Schema(description = "Pedido en línea para retiro en tienda o despacho a domicilio")
public class Pedido {

    public static final String TIPO_RETIRO = "RETIRO";
    public static final String TIPO_DESPACHO = "DESPACHO";
    public static final Set<String> TIPOS_ENTREGA = Set.of(TIPO_RETIRO, TIPO_DESPACHO);

    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_CONFIRMADO = "CONFIRMADO";
    public static final String ESTADO_LISTO = "LISTO";
    public static final String ESTADO_ENTREGADO = "ENTREGADO";
    public static final String ESTADO_CANCELADO = "CANCELADO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private Sucursal sucursal;

    @Column(nullable = false)
    @Schema(description = "RETIRO o DESPACHO", example = "RETIRO")
    private String tipoEntrega;

    @Column
    @Schema(description = "Dirección de despacho (obligatoria si tipo es DESPACHO)")
    private String direccionDespacho;

    @Column(nullable = false)
    private String estado = ESTADO_PENDIENTE;

    @Column(nullable = false)
    private Date fechaCreacion;

    @Column(nullable = false)
    private Double total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoDetalle> detalles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public String getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(String tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public String getDireccionDespacho() {
        return direccionDespacho;
    }

    public void setDireccionDespacho(String direccionDespacho) {
        this.direccionDespacho = direccionDespacho;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<PedidoDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<PedidoDetalle> detalles) {
        this.detalles = detalles;
    }

    public void validarTipoEntrega() {
        if (tipoEntrega == null || !TIPOS_ENTREGA.contains(tipoEntrega)) {
            throw new IllegalArgumentException("Tipo de entrega inválido. Use RETIRO o DESPACHO");
        }
        if (TIPO_DESPACHO.equals(tipoEntrega) && (direccionDespacho == null || direccionDespacho.isBlank())) {
            throw new IllegalArgumentException("La dirección de despacho es obligatoria para pedidos a domicilio");
        }
    }
}
