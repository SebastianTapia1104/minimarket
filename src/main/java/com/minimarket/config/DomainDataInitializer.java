package com.minimarket.config;

import com.minimarket.entity.*;
import com.minimarket.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Order(2)
public class DomainDataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final SucursalRepository sucursalRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final StockSucursalRepository stockSucursalRepository;
    private final PromocionRepository promocionRepository;

    public DomainDataInitializer(
            CategoriaRepository categoriaRepository,
            SucursalRepository sucursalRepository,
            ProveedorRepository proveedorRepository,
            ProductoRepository productoRepository,
            StockSucursalRepository stockSucursalRepository,
            PromocionRepository promocionRepository
    ) {
        this.categoriaRepository = categoriaRepository;
        this.sucursalRepository = sucursalRepository;
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
        this.stockSucursalRepository = stockSucursalRepository;
        this.promocionRepository = promocionRepository;
    }

    @Override
    public void run(String... args) {
        if (categoriaRepository.count() > 0) {
            return;
        }

        Categoria abarrotes = categoria("Abarrotes");
        Categoria bebidas = categoria("Bebidas");
        Categoria lacteos = categoria("Lácteos y congelados");
        Categoria limpieza = categoria("Artículos de limpieza");
        Categoria cuidado = categoria("Cuidado personal");

        Sucursal providencia = sucursal("Sucursal Providencia", "Av. Providencia 1234", "Providencia");
        Sucursal maipu = sucursal("Sucursal Maipú", "Av. Pajaritos 3200", "Maipú");
        Sucursal nunoa = sucursal("Sucursal Ñuñoa", "Irarrázaval 2500", "Ñuñoa");

        Proveedor cocaCola = proveedor("Coca-Cola", "96.791.230-8", "ventas@coca-cola.cl");
        Proveedor nestle = proveedor("Nestlé", "90.320.000-6", "ventas@nestle.cl");
        Proveedor unilever = proveedor("Unilever", "92.070.000-9", "ventas@unilever.cl");
        Proveedor soprole = proveedor("Soprole", "91.000.000-4", "ventas@soprole.cl");
        Proveedor ccu = proveedor("CCU", "90.413.000-1", "ventas@ccu.cl");

        Producto arroz = producto("Arroz Grado 1 1kg", 1590.0, 80, abarrotes);
        Producto bebida = producto("Bebida Coca-Cola 1.5L", 1890.0, 60, bebidas);
        Producto leche = producto("Leche Entera Soprole 1L", 1190.0, 50, lacteos);
        Producto detergente = producto("Detergente Omo 3L", 5990.0, 30, limpieza);
        Producto shampoo = producto("Shampoo Dove 400ml", 3490.0, 40, cuidado);

        stock(providencia, arroz, 40, 10, nestle);
        stock(maipu, arroz, 25, 10, nestle);
        stock(nunoa, arroz, 15, 10, nestle);

        stock(providencia, bebida, 30, 12, cocaCola);
        stock(maipu, bebida, 20, 12, cocaCola);
        stock(nunoa, bebida, 10, 12, ccu);

        stock(providencia, leche, 25, 8, soprole);
        stock(maipu, leche, 15, 8, soprole);
        stock(nunoa, leche, 10, 8, soprole);

        stock(providencia, detergente, 12, 5, unilever);
        stock(maipu, detergente, 10, 5, unilever);
        stock(nunoa, detergente, 8, 5, unilever);

        stock(providencia, shampoo, 18, 6, unilever);
        stock(maipu, shampoo, 12, 6, unilever);
        stock(nunoa, shampoo, 10, 6, unilever);

        sincronizarStockGlobal(List.of(arroz, bebida, leche, detergente, shampoo));

        Promocion promo = new Promocion();
        promo.setNombre("Verano Refrescante");
        promo.setDescripcion("15% de descuento en bebidas");
        promo.setPorcentajeDescuento(15.0);
        promo.setCategoria(bebidas);
        promo.setFechaInicio(diasDesdeHoy(-1));
        promo.setFechaFin(diasDesdeHoy(30));
        promo.setActiva(true);
        promocionRepository.save(promo);
    }

    private Categoria categoria(String nombre) {
        Categoria c = new Categoria();
        c.setNombre(nombre);
        return categoriaRepository.save(c);
    }

    private Sucursal sucursal(String nombre, String direccion, String comuna) {
        Sucursal s = new Sucursal();
        s.setNombre(nombre);
        s.setDireccion(direccion);
        s.setComuna(comuna);
        s.setRegion("Metropolitana");
        s.setActiva(true);
        return sucursalRepository.save(s);
    }

    private Proveedor proveedor(String nombre, String rut, String email) {
        Proveedor p = new Proveedor();
        p.setNombre(nombre);
        p.setRut(rut);
        p.setEmail(email);
        p.setTelefono("+56910000000");
        return proveedorRepository.save(p);
    }

    private Producto producto(String nombre, double precio, int stock, Categoria categoria) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setCategoria(categoria);
        return productoRepository.save(p);
    }

    private void stock(Sucursal sucursal, Producto producto, int cantidad, int minimo, Proveedor proveedor) {
        StockSucursal s = new StockSucursal();
        s.setSucursal(sucursal);
        s.setProducto(producto);
        s.setCantidad(cantidad);
        s.setStockMinimo(minimo);
        s.setProveedorPreferido(proveedor);
        stockSucursalRepository.save(s);
    }

    private void sincronizarStockGlobal(List<Producto> productos) {
        for (Producto producto : productos) {
            int total = stockSucursalRepository.findByProductoId(producto.getId()).stream()
                    .mapToInt(StockSucursal::getCantidad)
                    .sum();
            producto.setStock(total);
            productoRepository.save(producto);
        }
    }

    private Date diasDesdeHoy(int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, dias);
        return calendar.getTime();
    }
}
