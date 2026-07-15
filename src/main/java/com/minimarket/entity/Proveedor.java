package com.minimarket.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.hateoas.server.core.Relation;

@Entity
@Relation(collectionRelation = "proveedores", itemRelation = "proveedor")
@Schema(description = "Proveedor de productos del minimarket")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador del proveedor", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Razón social", example = "Coca-Cola Chile")
    private String nombre;

    @Column(nullable = false, unique = true)
    @Schema(description = "RUT", example = "96.791.230-8")
    private String rut;

    @Column(nullable = false)
    @Schema(description = "Correo de contacto", example = "compras@proveedor.cl")
    private String email;

    @Column(nullable = false)
    @Schema(description = "Teléfono", example = "+56912345678")
    private String telefono;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
