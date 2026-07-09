package com.minimarket.controller;



import com.minimarket.entity.Producto;

import com.minimarket.hateoas.ProductoModelAssembler;

import com.minimarket.service.ProductoService;

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

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;



@RestController

@RequestMapping("/api/productos")

@Tag(name = "Productos", description = "Gestión completa del catálogo de productos con enlaces HATEOAS")

@SecurityRequirement(name = "bearerAuth")

public class ProductoController {



    private final ProductoService productoService;

    private final ProductoModelAssembler productoAssembler;



    public ProductoController(ProductoService productoService, ProductoModelAssembler productoAssembler) {

        this.productoService = productoService;

        this.productoAssembler = productoAssembler;

    }



    @Operation(

            summary = "Listar productos",

            description = "Obtiene todos los productos con enlaces HATEOAS para navegar entre recursos relacionados."

    )

    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Lista de productos con enlaces _links",

                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProductoResponse"))),

            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),

            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")

    })

    @GetMapping

    public CollectionModel<EntityModel<Producto>> listarProductos() {

        return productoAssembler.toCollectionModel(productoService.findAll());

    }



    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto con enlaces self, productos e inventario.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Producto encontrado con enlaces HATEOAS"),

            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),

            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),

            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")

    })

    @GetMapping("/{id}")

    public ResponseEntity<EntityModel<Producto>> obtenerProductoPorId(

            @Parameter(description = "Identificador del producto", example = "1", required = true)

            @PathVariable Long id) {

        Producto producto = productoService.findById(id);

        return (producto != null)

                ? ResponseEntity.ok(productoAssembler.toModel(producto))

                : ResponseEntity.notFound().build();

    }



    @Operation(summary = "Crear producto", description = "Agrega un producto nuevo al catálogo. Requiere rol GERENTE.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Producto creado con enlaces HATEOAS"),

            @ApiResponse(responseCode = "403", description = "Solo GERENTE puede crear productos"),

            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")

    })

    @PreAuthorize("hasRole('GERENTE')")

    @PostMapping

    public EntityModel<Producto> guardarProducto(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(

                    description = "Datos del producto a registrar",

                    required = true,

                    content = @Content(

                            schema = @Schema(ref = "#/components/schemas/ProductoRequest"),

                            examples = @ExampleObject(

                                    name = "Nuevo producto",

                                    value = """

                                            {

                                              "nombre": "Arroz 1kg",

                                              "precio": 1590.0,

                                              "stock": 50,

                                              "categoria": { "id": 1 }

                                            }

                                            """

                            )

                    )

            )

            @RequestBody Producto producto) {

        return productoAssembler.toModel(productoService.save(producto));

    }



    @Operation(summary = "Actualizar producto", description = "Modifica un producto existente. Requiere rol GERENTE.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Producto actualizado con enlaces HATEOAS"),

            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),

            @ApiResponse(responseCode = "403", description = "Solo GERENTE puede actualizar productos"),

            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")

    })

    @PreAuthorize("hasRole('GERENTE')")

    @PutMapping("/{id}")

    public ResponseEntity<EntityModel<Producto>> actualizarProducto(

            @Parameter(description = "Identificador del producto", example = "1", required = true)

            @PathVariable Long id,

            @RequestBody Producto producto) {

        Producto productoExistente = productoService.findById(id);

        if (productoExistente != null) {

            producto.setId(id);

            return ResponseEntity.ok(productoAssembler.toModel(productoService.save(producto)));

        }

        return ResponseEntity.notFound().build();

    }



    @Operation(summary = "Eliminar producto", description = "Elimina un producto del catálogo. Requiere rol GERENTE.")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),

            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),

            @ApiResponse(responseCode = "403", description = "Solo GERENTE puede eliminar productos"),

            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")

    })

    @PreAuthorize("hasRole('GERENTE')")

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> eliminarProducto(

            @Parameter(description = "Identificador del producto", example = "1", required = true)

            @PathVariable Long id) {

        Producto producto = productoService.findById(id);

        if (producto != null) {

            productoService.deleteById(id);

            return ResponseEntity.noContent().build();

        }

        return ResponseEntity.notFound().build();

    }

}


