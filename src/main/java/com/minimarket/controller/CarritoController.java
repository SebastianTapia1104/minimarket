package com.minimarket.controller;



import com.minimarket.entity.Carrito;

import com.minimarket.hateoas.CarritoModelAssembler;

import com.minimarket.service.CarritoService;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.media.Content;

import io.swagger.v3.oas.annotations.media.ExampleObject;

import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.hateoas.CollectionModel;

import org.springframework.hateoas.EntityModel;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;



@RestController

@RequestMapping("/api/carrito")

@Tag(name = "Carrito", description = "Operaciones para agregar, consultar y eliminar productos del carrito con HATEOAS")

@SecurityRequirement(name = "bearerAuth")

public class CarritoController {



    private final CarritoService carritoService;

    private final CarritoModelAssembler carritoAssembler;



    public CarritoController(CarritoService carritoService, CarritoModelAssembler carritoAssembler) {

        this.carritoService = carritoService;

        this.carritoAssembler = carritoAssembler;

    }



    @Operation(

            summary = "Listar productos del carrito",

            description = "Obtiene todos los ítems del carrito con enlaces HATEOAS."

    )

    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Lista del carrito con enlaces _links"),

            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),

            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")

    })

    @GetMapping

    public CollectionModel<EntityModel<Carrito>> listarCarrito() {

        return carritoAssembler.toCollectionModel(carritoService.findAll());

    }



    @Operation(summary = "Obtener ítem del carrito por ID", description = "Retorna un ítem con enlaces self, carrito y producto.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Ítem encontrado con enlaces HATEOAS"),

            @ApiResponse(responseCode = "404", description = "Ítem no encontrado"),

            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),

            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")

    })

    @GetMapping("/{id}")

    public ResponseEntity<EntityModel<Carrito>> obtenerCarritoPorId(

            @Parameter(description = "Identificador del ítem", example = "1", required = true)

            @PathVariable Long id) {

        Carrito carrito = carritoService.findById(id);

        return (carrito != null)

                ? ResponseEntity.ok(carritoAssembler.toModel(carrito))

                : ResponseEntity.notFound().build();

    }



    @Operation(summary = "Agregar producto al carrito", description = "Añade un producto al carrito indicando usuario, producto y cantidad.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Producto agregado con enlaces HATEOAS"),

            @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente"),

            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),

            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")

    })

    @PostMapping

    public EntityModel<Carrito> agregarProductoAlCarrito(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(

                    description = "Datos del ítem a agregar",

                    required = true,

                    content = @Content(

                            schema = @Schema(ref = "#/components/schemas/CarritoRequest"),

                            examples = @ExampleObject(

                                    name = "Agregar al carrito",

                                    value = """

                                            {

                                              "usuario": { "id": 1 },

                                              "producto": { "id": 2 },

                                              "cantidad": 3

                                            }

                                            """

                            )

                    )

            )

            @RequestBody Carrito carrito) {

        return carritoAssembler.toModel(carritoService.save(carrito));

    }



    @Operation(summary = "Actualizar ítem del carrito", description = "Modifica cantidad o producto de un ítem existente.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Ítem actualizado con enlaces HATEOAS"),

            @ApiResponse(responseCode = "404", description = "Ítem no encontrado"),

            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")

    })

    @PutMapping("/{id}")

    public ResponseEntity<EntityModel<Carrito>> actualizarCarrito(

            @Parameter(description = "Identificador del ítem", example = "1", required = true)

            @PathVariable Long id,

            @RequestBody Carrito carrito) {

        Carrito existente = carritoService.findById(id);

        if (existente != null) {

            carrito.setId(id);

            return ResponseEntity.ok(carritoAssembler.toModel(carritoService.save(carrito)));

        }

        return ResponseEntity.notFound().build();

    }



    @Operation(summary = "Eliminar producto del carrito", description = "Elimina un ítem del carrito por su identificador.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "204", description = "Ítem eliminado correctamente"),

            @ApiResponse(responseCode = "404", description = "Ítem no encontrado"),

            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")

    })

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> eliminarProductoDelCarrito(

            @Parameter(description = "Identificador del ítem", example = "1", required = true)

            @PathVariable Long id) {

        Carrito carrito = carritoService.findById(id);

        if (carrito != null) {

            carritoService.deleteById(id);

            return ResponseEntity.noContent().build();

        }

        return ResponseEntity.notFound().build();

    }

}


