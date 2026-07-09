package com.minimarket.controller;

import com.minimarket.entity.Usuario;
import com.minimarket.hateoas.UsuarioModelAssembler;
import com.minimarket.service.UsuarioService;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Administración de clientes, empleados y roles con enlaces HATEOAS")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler usuarioAssembler;

    public UsuarioController(UsuarioService usuarioService, UsuarioModelAssembler usuarioAssembler) {
        this.usuarioService = usuarioService;
        this.usuarioAssembler = usuarioAssembler;
    }

    @Operation(
            summary = "Listar usuarios registrados",
            description = "Obtiene todos los usuarios del sistema con enlaces HATEOAS. Requiere rol GERENTE."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios con enlaces _links"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Solo GERENTE puede listar usuarios")
    })
    @PreAuthorize("hasRole('GERENTE')")
    @GetMapping
    public CollectionModel<EntityModel<Usuario>> listarUsuarios() {
        return usuarioAssembler.toCollectionModel(usuarioService.findAll());
    }

    @Operation(summary = "Obtener usuario por ID", description = "Retorna un usuario con enlaces self, usuarios y carrito.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado con enlaces HATEOAS"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Solo GERENTE puede consultar usuarios")
    })
    @PreAuthorize("hasRole('GERENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtenerUsuarioPorId(
            @Parameter(description = "Identificador del usuario", example = "1", required = true)
            @PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.map(value -> ResponseEntity.ok(usuarioAssembler.toModel(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Agregar nuevo usuario", description = "Registra un usuario con roles asignados. Requiere rol GERENTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado con enlaces HATEOAS"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Solo GERENTE puede crear usuarios")
    })
    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping
    public EntityModel<Usuario> guardarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario a registrar",
                    required = true,
                    content = @Content(
                            schema = @Schema(ref = "#/components/schemas/UsuarioRequest"),
                            examples = @ExampleObject(
                                    name = "Nuevo cliente",
                                    value = """
                                            {
                                              "username": "nuevo_cliente",
                                              "nombre": "Ana",
                                              "apellido": "Pérez",
                                              "email": "ana@minimarket.cl",
                                              "direccion": "Av. Central 456",
                                              "password": "Cliente123!"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody Usuario usuario) {
        return usuarioAssembler.toModel(usuarioService.save(usuario));
    }

    @Operation(summary = "Actualizar usuario", description = "Modifica los datos de un usuario existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado con enlaces HATEOAS"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Solo GERENTE puede actualizar usuarios")
    })
    @PreAuthorize("hasRole('GERENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(
            @Parameter(description = "Identificador del usuario", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioService.findById(id);
        if (usuarioExistente.isPresent()) {
            usuario.setId(id);
            return ResponseEntity.ok(usuarioAssembler.toModel(usuarioService.save(usuario)));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Solo GERENTE puede eliminar usuarios")
    })
    @PreAuthorize("hasRole('GERENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "Identificador del usuario", example = "1", required = true)
            @PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        if (usuario.isPresent()) {
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
