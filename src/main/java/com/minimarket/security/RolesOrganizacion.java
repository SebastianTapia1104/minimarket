package com.minimarket.security;

import java.util.Set;

/**
 * Roles alineados a la estructura organizativa de MiniMarket Plus
 * y al cliente final.
 */
public final class RolesOrganizacion {

    public static final String GERENTE = "GERENTE";
    public static final String JEFE_TURNO = "JEFE_TURNO";
    public static final String REPONEDOR = "REPONEDOR";
    public static final String CAJERO = "CAJERO";
    public static final String ASISTENTE_CLIENTE = "ASISTENTE_CLIENTE";
    public static final String CLIENTE = "CLIENTE";
    /** Compatibilidad con versiones anteriores del API */
    public static final String EMPLEADO = "EMPLEADO";

    public static final Set<String> TODOS = Set.of(
            GERENTE, JEFE_TURNO, REPONEDOR, CAJERO, ASISTENTE_CLIENTE, CLIENTE, EMPLEADO
    );

    public static final Set<String> VENTAS = Set.of(CAJERO, EMPLEADO);
    public static final Set<String> INVENTARIO = Set.of(REPONEDOR, JEFE_TURNO, GERENTE, EMPLEADO);
    public static final Set<String> REPORTES = Set.of(GERENTE, JEFE_TURNO);
    public static final Set<String> PROMOCIONES = Set.of(GERENTE, JEFE_TURNO);
    public static final Set<String> ATENCION = Set.of(ASISTENTE_CLIENTE, CAJERO, GERENTE, JEFE_TURNO);

    private RolesOrganizacion() {
    }
}
