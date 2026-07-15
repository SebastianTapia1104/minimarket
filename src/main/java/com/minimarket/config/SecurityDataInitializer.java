package com.minimarket.config;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.security.RolesOrganizacion;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(1)
public class SecurityDataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityDataInitializer(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Rol cliente = ensureRole(RolesOrganizacion.CLIENTE);
        Rol empleado = ensureRole(RolesOrganizacion.EMPLEADO);
        Rol gerente = ensureRole(RolesOrganizacion.GERENTE);
        Rol jefeTurno = ensureRole(RolesOrganizacion.JEFE_TURNO);
        Rol reponedor = ensureRole(RolesOrganizacion.REPONEDOR);
        Rol cajero = ensureRole(RolesOrganizacion.CAJERO);
        Rol asistente = ensureRole(RolesOrganizacion.ASISTENTE_CLIENTE);

        ensureUser("cliente", "Cliente123!", Set.of(cliente), true);
        ensureUser("empleado", "Empleado123!", Set.of(empleado), true);
        ensureUser("gerente", "Gerente123!", Set.of(gerente), true);
        ensureUser("jefe", "Jefe123!", Set.of(jefeTurno), true);
        ensureUser("reponedor", "Reponedor123!", Set.of(reponedor), true);
        ensureUser("cajero", "Cajero123!", Set.of(cajero), true);
        ensureUser("asistente", "Asistente123!", Set.of(asistente), true);
    }

    private Rol ensureRole(String roleName) {
        return rolRepository.findByNombre(roleName).orElseGet(() -> {
            Rol role = new Rol();
            role.setNombre(roleName);
            return rolRepository.save(role);
        });
    }

    private void ensureUser(String username, String rawPassword, Set<Rol> roles, boolean consentimiento) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            return;
        }
        Usuario user = new Usuario();
        user.setUsername(username);
        user.setNombre(capitalize(username));
        user.setApellido("Demo");
        user.setEmail(username + "@minimarket.cl");
        user.setDireccion("Av. Principal 123, Santiago");
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setConsentimientoDatosPersonales(consentimiento);
        user.setRoles(roles);
        usuarioRepository.save(user);
    }

    private String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }
}
